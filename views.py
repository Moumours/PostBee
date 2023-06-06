from django.shortcuts import render, redirect
from django.http import JsonResponse
from django.contrib.auth.forms import UserCreationForm
from django.contrib import messages

from django.template.loader import render_to_string
from django.contrib.sites.shortcuts import get_current_site
from django.utils.http import urlsafe_base64_encode, urlsafe_base64_decode
from django.utils.encoding import force_bytes, force_str
from django.core.mail import EmailMessage
from django.contrib.auth import get_user_model

from .web_app.PostBee.web_postBee.forms import RegisterForm

from .web_app.PostBee.web_postBee.tokens import account_activation_token

import json

def index(request):
    return render(request, 'web_postBee/index.html', {'message': messages.get_messages(request)})

def register(request):
    if request.method == 'POST':
        form = RegisterForm(request.POST)
        if form.is_valid():
            user = form.save(commit=False)
            user.is_active = False
            user.save()
            for field in user._meta.fields:
                field_name = field.name
                field_value = getattr(user, field_name)
                print(f"{field_name}: {field_value}")
            # activateEmail(request, user)
            return redirect('index')
        
        else:
            for error in list(form.errors):
                messages.error(request, error)
    else :
        form = RegisterForm()
    
    return render(request, 'web_postBee/register.html', {'form': form})

def activateEmail(request, user):
    print("User name = "+user.username)
    mail_subject = 'Activate your user account.'
    message = render_to_string('web_postBee/template_activate_account.html', {
        'user': user,
        'domain': get_current_site(request).domain,
        'uid': urlsafe_base64_encode(force_bytes(user.pk)),
        'token': account_activation_token.make_token(user),
        'protocol': 'https' if request.is_secure() else 'http'
    })
    email = EmailMessage(mail_subject, message, to=['marc.proux@uha.fr'])
    if email.send():
        messages.success(request, f'Dear <b>{user}</b>, please go to you email marc.proux@uha.fr inbox and click on \
            received activation link to confirm and complete the registration. <b>Note:</b> Check your spam folder.')
    else:
        messages.error(request, f'Problem sending confirmation email to marc.proux@uha.fr, check if you typed it correctly.')

def activate(request, uidb64, token):
    User = get_user_model()
    try:
        uid = force_str(urlsafe_base64_decode(uidb64))
        user = User.objects.get(pk=uid)
    except(TypeError, ValueError, OverflowError, User.DoesNotExist):
        user = None

    if user is not None and account_activation_token.check_token(user, token):
        print("I will activate the user account : "+user.username)
        user.is_active = True
        user.save()

        print("Thank you for your email confirmation. Now you can login your account.")
        messages.success(request, 'Thank you for your email confirmation. Now you can login your account.')
        return redirect('index')
    else:
        messages.error(request, 'Activation link is invalid!')
    
    return redirect('index')

def test(request):
    print("I am in test")
    if request.method=='POST':
        received_json_data = json.loads(request.body.decode("utf-8"))
        print(received_json_data)
    return JsonResponse({'redirect':'sucess'})

