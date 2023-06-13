from rest_framework.views import APIView
from rest_framework.viewsets import ReadOnlyModelViewSet
import json
from django.conf import settings
from django.http import FileResponse, HttpRequest, HttpResponse
from django.views.decorators.cache import cache_control
from django.views.decorators.http import require_GET
from django.core.mail import EmailMultiAlternatives
from django.contrib.sites.shortcuts import get_current_site
from django.template.loader import render_to_string
from django.utils.http import urlsafe_base64_encode, urlsafe_base64_decode
from django.utils.encoding import force_bytes, force_str
from django.shortcuts import get_object_or_404, render
from rest_framework.response import Response
from rest_framework import status
from django.contrib.auth import authenticate, login, logout
from rest_framework.views import APIView
from rest_framework_simplejwt.tokens import RefreshToken
from rest_framework.permissions import IsAuthenticated
from django.utils import timezone
from rest_framework_simplejwt.token_blacklist.models import BlacklistedToken, OutstandingToken
from rest_framework.parsers import MultiPartParser
from django.utils.html import strip_tags
from django.conf.urls.static import static
from django.contrib.staticfiles import finders
from email.mime.image import MIMEImage

from api_postBee.forms import RegisterForm
from api_postBee.tokens import account_activation_token, password_reset_token
from api_postBee.models import *
from api_postBee.serializers import *

from django.db.models import F


class IndexView(APIView):
    def get(self, request, format=None):
        return Response({'message': 'Hello, world!'}, status=status.HTTP_200_OK)

class LoginView(APIView):
    def post(self, request, format=None):
        print("Data :"+str(request.data))
        # first_key = json.loads(list(request.data.keys())[0])
        # print("More :"+str(first_key))
        # = <QueryDict: {'{"password":"putaindemdp","email":"alexandre.caux@uha.fr"}': ['']}>
        # print the first key of the dict
        # print("Data :"+str(request.data.keys()[0]))
        email = request.data.get('email')
        password = request.data.get('password')
        print("Email :"+str(email))
        print("Password :"+str(password))

        user = authenticate(request, email=email, password=password)
        if user is not None:
            login(request, user)
            token = LoginView.get_tokens_for_user(user)
            return Response(token, status=status.HTTP_200_OK)
        else:
            return Response({'error': 'Invalid credentials'}, status=status.HTTP_400_BAD_REQUEST)
        
    def get_tokens_for_user(user):
        refresh = RefreshToken.for_user(user)

        return {
            'refresh': str(refresh),
            'access': str(refresh.access_token),
        }


class RegisterView(APIView):
    def post(self, request, format=None):
        if request.method == 'POST':
            print("Data :"+str(request.data))
            # json_data = json.loads(request.body)
            # print(json_data)
            oldAccount = Account.objects.filter(email=request.data.get('email')).first()
            if oldAccount and oldAccount.is_active==False:
                oldAccount.delete()
            form = RegisterForm(request.data)
            if form.is_valid():
                user = form.save(commit=False)
                user.is_active = False
                user.save()
                self.activate_email(request, user)
                response_data = {
                    'success': True,
                    'message': 'User registration successful.'
                }
                return Response(response_data, status=status.HTTP_201_CREATED)
            else:
                errors = {field: errors[0] for field, errors in form.errors.items()}
                response_data = {
                    'success': False,
                    'errors': errors
                }
                return Response(response_data, status=status.HTTP_400_BAD_REQUEST)
        else:
            response_data = {
                'success': False,
                'errors': 'Invalid request method.'
            }
            return Response(response_data, status=status.HTTP_405_METHOD_NOT_ALLOWED)


    def activate_email(self, request, user):
        mail_subject = 'Activation de votre compte.'
        html_message = render_to_string('api_postBee/template_activate_account.html', {
            'name': str(user.first_name)+" "+str(user.last_name),
            'domain': get_current_site(request).domain,
            'uid': urlsafe_base64_encode(force_bytes(user.pk)),
            'token': account_activation_token.make_token(user),
            'protocol': 'https' if request.is_secure() else 'http'
        })
        message = strip_tags(html_message)
        email = EmailMultiAlternatives(mail_subject, message, to=[user.email])
        email.attach_alternative(html_message, "text/html")
        email.mixed_subtype = 'related'

        for img in ['logo_ensisa.png', 'logo_line.png']:
            image = MIMEImage(open(finders.find('api_postBee/imgs/'+img), 'rb').read())
            image.add_header('Content-ID', '<{}>'.format(img))
            email.attach(image)

        if email.send():
            Response({'message': 'Confirmation email sent.'}, status=status.HTTP_200_OK)
        else:
            Response({'message': 'Confirmation email not sent.'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class ActivateAccount(APIView):
    def get(self, request, uidb64, token):
        User = Account
        try:
            uid = force_str(urlsafe_base64_decode(uidb64))
            user = User.objects.get(pk=uid)
        except (TypeError, ValueError, OverflowError, User.DoesNotExist):
            user = None

        if user is not None and account_activation_token.check_token(user, token):
            # print("I will activate the user account: " + user.username)
            user.is_active = True
            user.save()

            # print("Thank you for your email confirmation. Now you can login to your account.")
            return render(request, 'api_postBee/registerComplete.html')
        else:
            # print("Activation link is invalid!")
            return Response({'message': 'Activation link is invalid!'}, status=status.HTTP_400_BAD_REQUEST)



class PostList(ReadOnlyModelViewSet):
    serializer_class = PostListSerializer
    # permission_classes = [IsAuthenticated]

    def get_queryset(self):
        queryset = Post.objects.all()
        type = self.request.query_params.get('type', None)
        amount = self.request.query_params.get('amount', 10)

        # User is staff status and filter moderate is true
        if type == 'moderate':# and self.request.user.is_staff:
            print('Moderate is true and user is staff')
            queryset = queryset.filter(status='0').order_by('-date')[:int(amount)]

        # elif type == 'own':
        #     # print('Moderate is false')
        #     queryset = queryset.filter(author=self.request.user).order_by('-date')[:int(amount)]
        
        else:
            queryset = queryset.filter(status='1').order_by('-date')[:int(amount)]
        
        return queryset

    def list(self, request, *args, **kwargs):
        queryset = self.filter_queryset(self.get_queryset())
        serializer = self.get_serializer(queryset, many=True)
        return Response(serializer.data)

class PostDetail(ReadOnlyModelViewSet):
    serializer_class = PostDetailSerializer
    # permission_classes = [IsAuthenticated]

    def get_queryset(self):
        id = self.request.query_params.get('id')
        if id is None:
            return Response({'error': 'Post ID is required'}, status=status.HTTP_400_BAD_REQUEST)
        queryset = get_object_or_404(Post.objects.all(), id=id)
        print("Post status : " + queryset.status)
        # if not queryset.status == '1' and (not self.request.user.is_staff or queryset.author != self.request.user):
        #     print('Post not found')
            # return Response({'error': 'Post not found'}, status=status.HTTP_404_NOT_FOUND)
        return queryset
    
    def list(self, request, *args, **kwargs):
        queryset = self.filter_queryset(self.get_queryset())
        serializer = self.get_serializer(queryset)
        return Response(serializer.data)
        
class PublishPost(APIView):
    permission_classes = [IsAuthenticated]
    parser_classes = [MultiPartParser]

    def post(self, request, format=None):
        if request.method == 'POST':
            serializer = PostPublishSerializer(data=request.data)
            # print(request.data)
            if serializer.is_valid():
                print("serializer : " + str(serializer))
                serializer.save(author = request.user)
                response_data = {
                    'success': True,
                    'data': serializer.data
                }
                return Response(response_data, status=status.HTTP_201_CREATED)
            else:
                response_data = {
                    'success': False,
                    'errors': serializer.errors
                }
                return Response(response_data, status=status.HTTP_400_BAD_REQUEST)
            
        else:
            response_data = {
                'success': False,
                'errors': 'Invalid request method.'
            }
            return Response(response_data, status=status.HTTP_405_METHOD_NOT_ALLOWED)  
        

class PublishComment(APIView):
    # permission_classes = [IsAuthenticated]

    def post(self, request, format=None):
        if request.method == 'POST':
            serializer = CommentPublishSerializer(data=request.data)
            if serializer.is_valid():
                content = serializer.validated_data['text']
                post_id = serializer.validated_data['post'].id
                # print('id = ' + str(post_id))
                # user = request.user  # Assuming authentication is configured and user is available in the request
                user = Account.objects.get(email="marc.proux@uha.fr")
                post = Post.objects.get(id=post_id)
                if post is None or post.status == '0' or post.status == '2':
                    return Response({'error': 'Post not found'}, status=status.HTTP_404_NOT_FOUND)
                comment = Comment.objects.create(post=post, author=user, text=content)
                response_data = {
                    'success': True,
                    'message': 'Comment created successfully.',
                }
                return Response(response_data, status=status.HTTP_201_CREATED)
            else:
                response_data = {
                    'success': False,
                    'errors': serializer.errors
                }
                return Response(response_data, status=status.HTTP_400_BAD_REQUEST)
        
        else:
            response_data = {
                'success': False,
                'errors': 'Invalid request method.'
            }
            return Response(response_data, status=status.HTTP_405_METHOD_NOT_ALLOWED)


class ApprovePost(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request, format=None):
        if request.method == 'POST':
            print("request.data : " + str(request.data))
            serializer = ApprovePostSerializer(data=request.data)
            if not self.request.user.is_staff:
                return Response({'error': 'You are not authorized to perform this action.'}, status=403)
            if serializer.is_valid():
                id = request.data.get('postId')
                approve_status = request.data.get('response')
                if id is None or approve_status is None:
                    return Response({'error': 'Post ID and approve status are required'}, status=status.HTTP_400_BAD_REQUEST)
                post = get_object_or_404(Post, id=id, status='0')

                if approve_status == 'true':
                    post.status = '1'  # Approve the post
                    post.date = timezone.now()  # Set the date to now
                    post.save()
                    return Response({'success': True, 'message': 'Post approved successfully.'}, status=200)
                else:
                    post.status = '2'
                    post.save()
                    return Response({'success': True, 'message': 'Post rejected successfully.'}, status=200)
            else:
                response_data = {
                    'success': False,
                    'errors': serializer.errors
                }
                return Response(response_data, status=status.HTTP_400_BAD_REQUEST)
        else:
            response_data = {
                'success': False,
                'errors': 'Invalid request method.'
            }
            return Response(response_data, status=status.HTTP_405_METHOD_NOT_ALLOWED)
        
class DeleteUser(APIView):
    # permission_classes = [IsAuthenticated]

    def post(self, request, format=None):
        if request.method == 'POST':
            serializers = DeleteAndModoUserSerializer(data=request.data)
            # if not self.request.user.is_staff:
            #     return Response({'error': 'You are not authorized to perform this action.'}, status=403)
            
            if serializers.is_valid():
                id = request.data.get('userId')
                if id is None:
                    return Response({'error': 'User ID is required'}, status=status.HTTP_400_BAD_REQUEST)
                user = get_object_or_404(Account, id=id)
                user.delete()
                return Response({'success': True, 'message': 'User deleted successfully.'}, status=200)
            else:
                response_data = {
                    'success': False,
                    'errors': serializers.errors
                }
                return Response(response_data, status=status.HTTP_400_BAD_REQUEST)
        else:
            response_data = {
                'success': False,
                'errors': 'Invalid request method.'
            }
            return Response(response_data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

class AddModo(APIView):
    # permission_classes = [IsAuthenticated]

    def post(self, request, format=None):
        if request.method == 'POST':
            serializers = DeleteAndModoUserSerializer(data=request.data)
            # if not self.request.user.is_staff:
            #     return Response({'error': 'You are not authorized to perform this action.'}, status=403)
            
            if serializers.is_valid():
                print(request.data)
                id = request.data.get('userId')
                print("id = " + str(id))
                if id is None:
                    return Response({'error': 'User ID is required'}, status=status.HTTP_400_BAD_REQUEST)
                user = get_object_or_404(Account, id=id)
                user.is_staff = True
                user.save()
                return Response({'success': True, 'message': 'User added as moderator successfully.'}, status=200)
            else:
                response_data = {
                    'success': False,
                    'errors': serializers.errors
                }
                return Response(response_data, status=status.HTTP_400_BAD_REQUEST)
        else:
            response_data = {
                'success': False,
                'errors': 'Invalid request method.'
            }
            return Response(response_data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

class DeleteComment(APIView):
    # permission_classes = [IsAuthenticated]

    def post(self, request, format=None):
        if request.method == 'POST':
            serializers = DeleteCommentSerializer(data=request.data)
            # if not self.request.user.is_staff:
            #     return Response({'error': 'You are not authorized to perform this action.'}, status=403)
            if serializers.is_valid():
                print(request.data)
                id = request.data.get('id')
                if id is None:
                    return Response({'error': 'Comment ID is required'}, status=status.HTTP_400_BAD_REQUEST)
                comment = get_object_or_404(Comment, id=id)
                comment.delete()
                return Response({'success': True, 'message': 'Comment deleted successfully.'}, status=200)
            else:
                response_data = {
                    'success': False,
                    'errors': serializers.errors
                }
                return Response(response_data, status=status.HTTP_400_BAD_REQUEST)

class UserView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request, format=None):
        if request.method == 'GET':
            user = self.request.user
            serializer = UserSerializer(user)
            return Response(serializer.data, status=status.HTTP_200_OK)
        else:
            response_data = {
                'success': False,
                'errors': 'Invalid request method.'
            }
            return Response(response_data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

class LogoutView(APIView):
    permission_classes = (IsAuthenticated,)

    def post(self, request, *args, **kwargs):
        if self.request.data.get('all'):
            token = RefreshToken.for_user(request.user)
            for token in OutstandingToken.objects.filter(user=request.user):
                _, _ = BlacklistedToken.objects.get_or_create(token=token)
            return Response({"status": "OK, goodbye, all refresh tokens blacklisted"})
        refresh_token = self.request.data.get('refresh_token')
        token = RefreshToken(token=refresh_token)
        token.blacklist()
        return Response({"status": "OK, goodbye"})

@require_GET
@cache_control(max_age=60 * 60 * 24, immutable=True, public=True)  # one day
def favicon(request: HttpRequest) -> HttpResponse:
    file = (settings.BASE_DIR / "static" / "favicon.png").open("rb")
    return FileResponse(file)

class ResetPassword(APIView):
    serializer_class = ResetPasswordSerializer

    def post(self, request, format=None):
        if request.method == 'POST':
            serializer = self.serializer_class(data=request.data)
            if serializer.is_valid():
                email = serializer.data.get('email')
                user = Account.objects.get(email=email)
                if user:
                    mail_subject = 'Réinitialiser votre mot de passe.'
                    html_message = render_to_string('api_postBee/template_reset_password.html', {
                        'name': str(user.first_name)+" "+str(user.last_name),
                        'domain': get_current_site(request).domain,
                        'uid': urlsafe_base64_encode(force_bytes(user.pk)),
                        'token': account_activation_token.make_token(user),
                        'protocol': 'https' if request.is_secure() else 'http'
                    })
                    message = strip_tags(html_message)
                    email = EmailMultiAlternatives(mail_subject, message, to=[email])
                    email.attach_alternative(html_message, "text/html")
                    email.mixed_subtype = 'related'

                    for img in ['logo_ensisa.png', 'logo_line.png']:
                        image = MIMEImage(open(finders.find('api_postBee/imgs/'+img), 'rb').read())
                        image.add_header('Content-ID', '<{}>'.format(img))
                        email.attach(image)

                    if email.send():
                        return Response({'success': True, 'message': 'Reset email sent.'}, status=status.HTTP_200_OK)
                    else:
                        return Response({'message': 'Reset email not sent.'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
                else:
                    return Response({'success': False, 'errors': 'User with this email does not exist.'}, status=status.HTTP_400_BAD_REQUEST)
            else:
                return Response({'success': False, 'errors': serializer.errors}, status=status.HTTP_400_BAD_REQUEST)


class ResetPasswordConfirm(APIView):
    def get(self, request, uidb64, token):
        User = Account
        try:
            uid = force_str(urlsafe_base64_decode(uidb64))
            user = User.objects.get(pk=uid)
        except (TypeError, ValueError, OverflowError, User.DoesNotExist):
            user = None
        
        if user is not None and password_reset_token.check_token(user, token):
            return render(request, 'api_postBee/resetPassword.html', {
                'uidb64': uidb64,
                'token': token
            })
        else:
            return Response({'success': False, 'errors': 'Credentials are invalid'}, status=status.HTTP_400_BAD_REQUEST)
    
    def post(self, request, uidb64, token):
        User = Account
        try:
            uid = force_str(urlsafe_base64_decode(uidb64))
            user = User.objects.get(pk=uid)
            print("user = " + str(user))
        except (TypeError, ValueError, OverflowError, User.DoesNotExist):
            user = None
        
        if user is not None and password_reset_token.check_token(user, token):
            password = request.data.get('new_password')
            confirm_password = request.data.get('confirm_password')
            if password == confirm_password:
                user.set_password(password)
                user.save()
                return render(request, 'api_postBee/resetComplete.html')
            else:
                return Response({'success': False, 'errors': 'Passwords do not match.'}, status=status.HTTP_400_BAD_REQUEST)
        else:
            return Response({'success': False, 'errors': 'Credentials are invalid'}, status=status.HTTP_400_BAD_REQUEST)
            

class UsersLists(ReadOnlyModelViewSet):
    permission_classes = [IsAuthenticated]
    serializer_class = UserSerializer

    def get_queryset(self):
        queryset = Account.objects.all()
        amount = self.request.query_params.get('amount', 10)
        return queryset.order_by(F('last_name').asc(nulls_last=True))[:amount]
    
    def list(self, request, *args, **kwargs):
        print("list")
        if not self.request.user.is_staff:
            return Response({'success': False, 'errors': 'You do not have permission to perform this action.'}, status=status.HTTP_403_FORBIDDEN)
        queryset = self.filter_queryset(self.get_queryset())
        serializer = self.get_serializer(queryset, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
class ChangePassword(APIView):
    permission_classes = [IsAuthenticated]

    def post(self, request, format=None):
        if request.method == 'POST':
            serializer = ChangePasswordSerializer(data=request.data)
            if serializer.is_valid():
                user = self.request.user
                if user.check_password(serializer.data.get('old_password')):
                    user.set_password(serializer.data.get('new_password'))
                    user.save()
                    return Response({'success': True, 'message': 'Password changed successfully.'}, status=status.HTTP_200_OK)
                else:
                    return Response({'success': False, 'errors': 'Old password is incorrect.'}, status=status.HTTP_400_BAD_REQUEST)
            else:
                return Response({'success': False, 'errors': serializer.errors}, status=status.HTTP_400_BAD_REQUEST)
        else:
            response_data = {
                'success': False,
                'errors': 'Invalid request method.'
            }
            return Response(response_data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

class Test(APIView):
    def get(self, request, format=None):
        return render(request, 'api_postBee/template_activate_account.html')