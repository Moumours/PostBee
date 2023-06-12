from django.test import TestCase
from api_postBee.models import Account
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient
import time

from api_postBee.models import Account, Post, Comment

# class LoginViewTest(TestCase):
#     def setUp(self):
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword'
#         )

#     def test_login(self):
#         url = reverse('login')  # Assuming you've set up the URL for the login view
#         data = {
#             'email': 'test@example.com',
#             'password': 'testpassword'
#         }
#         response = self.client.post(url, data)
#         self.assertEqual(response.status_code, 200)
#         self.assertTrue('access' in response.data)
#         #print the token
#         print(response.data)

# class RegisterViewTest(TestCase):
#     def setUp(self):
#         self.client = APIClient()

#     def test_register(self):
#         url = reverse('register')
#         data = {
#             'first_name': 'John',
#             'last_name': 'Doe',
#             'email': 'prouxmarc@gmail.com',
#             'ensisaGroup': '0',
#             'password1': 'xy4828ic',
#             'password2': 'xy4828ic'
#         }
#         response = self.client.post(url, data, format='json')
#         self.assertEqual(response.status_code, status.HTTP_201_CREATED)
#         self.assertEqual(response.data['success'], True)
#         self.assertEqual(response.data['message'], 'User registration successful.')

# class PostViewTest(TestCase):
#     def setUp(self):
#         self.url = reverse('post-list')
    
#     def test_get(self):
#         response = self.client.get(self.url)
#         self.assertEqual(response.status_code, status.HTTP_200_OK)


# class PostListTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup=0,
#         )

#         # Create some posts fields = ('author_id', 'content', 'title')
#         post1 = Post.objects.create(author=self.user, text='test content', title='test title')
#         post2 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='1')
#         post3 = Post.objects.create(author=self.user, text='test content 3', title='test title 3', status='1')

#         # Create a user and retrieve the access token
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.assertEqual(response.status_code, 200)
#         self.access_token = response.data.get('access')

#     def test_get_post_list(self):
#         print("test_get_post_list")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get(reverse('post-list'))
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print()  # Print an empty line between posts
#         # Check the response status code
#         self.assertEqual(response.status_code, 200)


# class GetPostListNoModo(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup=1,
#             is_staff=False,
#             first_name='John',
#             last_name='Doe',
#         )

#         # Create some posts fields = ('author_id', 'content', 'title')
#         post1 = Post.objects.create(author=self.user, text='test content', title='test title')
#         post2 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='1')
#         post3 = Post.objects.create(author=self.user, text='test content 3', title='test title 3', status='1')

#         # Create a user and retrieve the access token
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.assertEqual(response.status_code, 200)
#         self.access_token = response.data.get('access')
#         print(self.user.first_name+ " " + self.user.last_name+" is logged in")
#         print("School group : " + str(self.user.ensisaGroup))
    
#     def test_no_modo_normal_access(self):
#         print("Test no modo noraml access")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?amount=3')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print()  # Print an empty line between posts
#         # Check the response status code
#         self.assertEqual(response.status_code, 200)

#     def test_no_modo_modo_access(self):
#         print("Test no modo modo access")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?amount=3&modo=True')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print(f"Modo: {post['author']}")
#             print()


# class GetPostListModo(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='1',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )

#         # Create some posts fields = ('author_id', 'content', 'title')
#         post1 = Post.objects.create(author=self.user, text='test content', title='test title', status='0')
#         post2 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='1')
#         post3 = Post.objects.create(author=self.user, text='test content 3', title='test title 3', status='1')

#         # Create a user and retrieve the access token
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.assertEqual(response.status_code, 200)
#         self.access_token = response.data.get('access')
    
#     def test_modo_normal_access(self):
#         print("Test no modo noramle access")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?amount=3')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print()  # Print an empty line between posts
#         # Check the response status code
#         self.assertEqual(response.status_code, 200)

#     def test_no_modo_modo_access(self):
#         print("Test no modo modo access")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?moderate=True&amount=3')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Author: {post['author']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print()


# class GetPostDetail(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup=0,
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )

#         # Create some posts fields = ('author_id', 'content', 'title')
#         post1 = Post.objects.create(author=self.user, text='test content', title='test title', status='0')
#         post2 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='1')
#         post3 = Post.objects.create(author=self.user, text='test content 3', title='test title 3', status='1')

#         # Create a user and retrieve the access token
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.assertEqual(response.status_code, 200)
#         self.access_token = response.data.get('access')
    
#     def test_post_detail(self):
#         print("Test post detail")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/post/?id=2')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         post = response.data
#         print(f"Post content: {post['text']}")
#         print()

# class PublishPostTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )
    
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.assertEqual(response.status_code, 200)
#         self.access_token = response.data.get('access')
#         print(self.user.first_name+ " " + self.user.last_name+" is logged in")
#         print("School group : " + str(self.user.ensisaGroup))

#     def test_publish_post(self):
#         # self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         data = {'title': 'test title', 'text': 'test content'}
#         response = self.client.post('/publish', data=data)

#         print("response" + str(response))
    

#         print("Test no modo noraml access")
#         # Set the access token in the Authorization header
#         # self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         print("Test no modo modo access")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?moderate=True&amount=3')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Author: {post['author']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print()
#         self.assertEqual(response.status_code, 200)


# class GetPostDetailV2(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup=0,
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )

#         # Create some posts fields = ('author_id', 'content', 'title')
#         post1 = Post.objects.create(author=self.user, text='test content', title='test title', status='0')
#         post2 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='1')
#         comment1 = Comment.objects.create(author=self.user, text='test comment1', post=post2)
#         comment2 = Comment.objects.create(author=self.user, text='test comment2', post=post2)
#         comment3 = Comment.objects.create(author=self.user, text='test comment3', post=post2)
#         post3 = Post.objects.create(author=self.user, text='test content 3', title='test title 3', status='1')
#         comment4 = Comment.objects.create(author=self.user, text='test comment4', post=post3)

#         # Create a user and retrieve the access token
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.assertEqual(response.status_code, 200)
#         self.access_token = response.data.get('access')
    
#     def test_post_detail(self):
#         print("Test post detail")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/post/?id=2')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         post = response.data
#         print(f"Post content: {post['text']}")
#         print(f"Post comments: {post['comments']}")
#         print()

# class CommentPostTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )
    
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.assertEqual(response.status_code, 200)
#         self.access_token = response.data.get('access')
#         # print(self.user.first_name+ " " + self.user.last_name+" is logged in")
#         # print("School group : " + str(self.user.ensisaGroup))

#         post1 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='1')

#     def test_comment_post(self):
#         # print("Test comment post")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         data = {'post': 1, 'text': 'test comment'}

#         response = self.client.post('/comment', data=data)

#         # print("response" + str(response))
#         self.assertEqual(response.status_code, 201)
    
#         print("Test post detail")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/post/?id=1')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         post = response.data
#         print(f"Post content: {post['text']}")
#         print(f"Post comments: {post['comments']}")
#         print()

# class ApprovePostTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.access_token = response.data.get('access')
#         post1 = Post.objects.create(author=self.user, text='test content 1', title='test title 1', status='0')
#         post2 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='0')
#         post3 = Post.objects.create(author=self.user, text='test content 3', title='test title 3', status='1')
#         post4 = Post.objects.create(author=self.user, text='test content 4', title='test title 4', status='1')

#     def test_modo_normal_access(self):
#         print("\nPost list :\n")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?amount=7')

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print()  # Print an empty line between posts
#         # Check the response status code
#         # self.assertEqual(response.status_code, 200)

#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')
#         print("\nApproving post :\n")
#         url = '/approve'
#         data = {'postId': 1, 'response': 'true'}
#         response = self.client.post(url, data=data)
#         print("response" + str(response))
#         print("Data : " + str(response.data))

#         print("\nPost list :\n")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?amount=7')

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print()  # Print an empty line between posts

#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')
#         print("\Refusing post :\n")
#         url = '/approve'
#         data = {'postId': 2, 'decision': 'False'}
#         response = self.client.post(url, data=data)
#         print("response" + str(response))
#         print("Data : " + str(response.data))

#         print("\nPost list :\n")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?amount=7')

#         posts = response.data
#         for post in posts:
#             print(f"Post ID: {post['id']}")
#             print(f"Title: {post['title']}")
#             print(f"Date: {post['date']}")
#             print(f"Status: {post['status']}")
#             print()  # Print an empty line between posts
        
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')
#         print("\nApproving post already approved :\n")
#         url = '/approve'
#         data = {'postId': 3, 'decision': 'True'}
#         response = self.client.post(url, data=data)
#         print("response" + str(response))
#         print("Data : " + str(response.data))

#         print("\nStatus conclusion :\n")
#         # print the status of all post in the database
#         posts = Post.objects.all()
#         for post in posts:
#             print(f"Post ID: {post.id}")
#             print(f"Title: {post.title}")
#             print(f"Date: {post.date}")
#             print(f"Status: {post.status}")
#             print()


# class DeleteUserTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         userAdmin = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=False,
#             first_name='John',
#             last_name='Doe',
#         )
#         user1 = Account.objects.create_user(
#             email='delete@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=False,
#             first_name='Mike',
#             last_name='Anderson',
#         )

#         response = self.client.post(reverse('login'), data={'email': userAdmin.email, 'password': 'testpassword'})
#         self.access_token = response.data.get('access')

#     def test_delete_user(self):
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         print("\nUser list :\n")
#         users = Account.objects.all()
#         for user in users:
#             print(f"User ID: {user.id}")
#             print(f"Email: {user.email}")
#             print(f"First name: {user.first_name}")
#             print(f"Last name: {user.last_name}")
#             print(f"Is staff: {user.is_staff}")
#             print()
        
#         print("\nDeleting user :\n")
#         url = '/delete_user'
#         data = {'userId': 2}
#         response = self.client.post(url, data=data)
#         print("response" + str(response))
#         print("Data : " + str(response.data))

#         print("\nUser list :\n")
#         users = Account.objects.all()
#         for user in users:
#             print(f"User ID: {user.id}")
#             print(f"Email: {user.email}")
#             print(f"First name: {user.first_name}")
#             print(f"Last name: {user.last_name}")
#             print(f"Is staff: {user.is_staff}")
#             print()

# class AddModoTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         userAdmin = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )
#         user1 = Account.objects.create_user(
#             email='delete@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=False,
#             first_name='Mike',
#             last_name='Anderson',
#         )

#         response = self.client.post(reverse('login'), data={'email': userAdmin.email, 'password': 'testpassword'})
#         self.access_token = response.data.get('access')

#     def test_delete_user(self):
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         print("\nUser list :\n")
#         users = Account.objects.all()
#         for user in users:
#             print(f"User ID: {user.id}")
#             print(f"Email: {user.email}")
#             print(f"First name: {user.first_name}")
#             print(f"Last name: {user.last_name}")
#             print(f"Is staff: {user.is_staff}")
#             print()
        
#         print("\nAdding Modo :\n")
#         url = '/add_modo'
#         data = {'userId': 2}
#         response = self.client.post(url, data=data)
#         print("response" + str(response))
#         print("Data : " + str(response.data))

#         print("\nUser list :\n")
#         users = Account.objects.all()
#         for user in users:
#             print(f"User ID: {user.id}")
#             print(f"Email: {user.email}")
#             print(f"First name: {user.first_name}")
#             print(f"Last name: {user.last_name}")
#             print(f"Is staff: {user.is_staff}")
#             print()

# class DeleteCommentTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )
#         response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
#         self.access_token = response.data.get('access')
#         post1 = Post.objects.create(author=self.user, text='test content 1', title='test title 1', status='0')
#         comment1 = Comment.objects.create(author=self.user, text='test comment 1', post=post1)
#         comment2 = Comment.objects.create(author=self.user, text='test comment 2', post=post1)
#         comment3 = Comment.objects.create(author=self.user, text='test comment 3', post=post1)

#     def test_delete_comment(self):
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         print("Post details :\n")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/post/?id=1')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         post = response.data
#         print(f"Post content: {post['text']}")
#         comments = post['comments']
#         for comment in comments:
#             print(f"Comment Author: {comment['author']['full_name']}")
#             print(f"Comment content: {comment['text']}")
#             print()
        
#         print("\nDeleting comment :\n")
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')
#         url = '/delete_comment'
#         data = {'id': 2}
#         response = self.client.post(url, data=data)
#         print("response" + str(response))
#         print("Data : " + str(response.data))

#         print("\nPost details :\n")
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')
#         response = self.client.get('/post/?id=1')
#         print("response" + str(response))
#         print("response.data" + str(response.data))

#         post = response.data
#         print(f"Post content: {post['text']}")
#         comments = post['comments']
#         for comment in comments:
#             print(f"Comment Author: {comment['author']['full_name']}")
#             print(f"Comment content: {comment['text']}")
#             print()

# class GetUserInfoTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )

#         response = self.client.post(reverse('login'), data={'email': self.user.email, 'password': 'testpassword'})
#         self.access_token = response.data.get('access')

#     def test_get_user_info(self):
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         print("\nUser info :\n")
#         url = '/user_info'
#         response = self.client.get(url)
#         print("response" + str(response))
#         print("Data : " + str(response.data))
#         datas = response.data

# class RefreshTokenTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )

#         response = self.client.post(reverse('login'), data={'email': self.user.email, 'password': 'testpassword'})
#         self.access_token = response.data.get('access')
    
#     def test_refresh_token(self):
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         print("\nRefresh token :\n")
#         url = '/refresh_token'
#         response = self.client.get(url)
#         print("response" + str(response))
#         print("Data : " + str(response.data))
#         datas = response.data
#         self.assertNotEqual(datas['access'], self.access_token)

# class LogoutTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='test@example.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )
#         response = self.client.post(reverse('login'), data={'email': self.user.email, 'password': 'testpassword'})
#         self.access_token = response.data.get('access')
#         self.refresh_token = response.data.get('refresh')

#         post1 = Post.objects.create(author=self.user, text='test content', title='test title', status='0')
#         post2 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='1')
#         post3 = Post.objects.create(author=self.user, text='test content 3', title='test title 3', status='1')

#     def test_logout(self):
#         print("\nPost list :\n")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?amount=7')
#         print("response" + str(response))
#         print("Data : " + str(response.data))

#         posts = response.data
#         # for post in posts:
#         #     print(f"Post ID: {post['id']}")
#         #     print(f"Title: {post['title']}")
#         #     print(f"Date: {post['date']}")
#         #     print(f"Status: {post['status']}")
#         #     print()  # Print an empty line between posts
#         # Check the response status code
#         # self.assertEqual(response.status_code, 200)

#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         print("\nLogout :\n")
#         url = '/logout'
#         response = self.client.post(url, data={'refresh': self.refresh_token})
#         print("response" + str(response))
#         print("Data : " + str(response.data))
#         # datas = response.data
#         # self.assertEqual(datas['detail'], 'Successfully logged out.')
#         # self.assertEqual(response.status_code, status.HTTP_200_OK)

#         print("\nPost list :\n")
#         # Set the access token in the Authorization header
#         self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

#         # Make a GET request to retrieve the list of posts
#         response = self.client.get('/posts/?amount=7')
#         print("response" + str(response))
#         print("Data : " + str(response.data))

#         posts = response.data
#         # for post in posts:
#         #     print(f"Post ID: {post['id']}")
#         #     print(f"Title: {post['title']}")
#         #     print(f"Date: {post['date']}")
#         #     print(f"Status: {post['status']}")
#         #     print()  # Print an empty line between posts
#         # Check the response status code
#         # self.assertEqual(response.status_code, 200)

# class ResetPasswordTestCase(TestCase):
#     def setUp(self):
#         self.client = APIClient()
#         self.user = Account.objects.create_user(
#             email='prouxmarc@gmail.com',
#             password='testpassword',
#             ensisaGroup='0',
#             is_staff=True,
#             first_name='John',
#             last_name='Doe',
#         )
    
#     def test_reset_password(self):
#         print("\nReset password :\n")
#         url = '/reset_password'
#         response = self.client.post(url, data={'email': self.user.email})
#         print("response" + str(response))
#         print("Data : " + str(response.data))
#         datas = response.data
#         # self.assertEqual(datas['detail'], 'Password reset e-mail has been sent.')
#         self.assertEqual(response.status_code, status.HTTP_200_OK)