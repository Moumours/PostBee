from django.test import TestCase
from api_postBee.models import Account
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient

from api_postBee.models import Account, Post

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
#             school_group=0,
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
#             school_group=0,
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
#             print()


class GetPostListModo(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.user = Account.objects.create_user(
            email='test@example.com',
            password='testpassword',
            school_group=0,
            is_staff=True,
            first_name='John',
            last_name='Doe',
        )

        # Create some posts fields = ('author_id', 'content', 'title')
        post1 = Post.objects.create(author=self.user, text='test content', title='test title', status='0')
        post2 = Post.objects.create(author=self.user, text='test content 2', title='test title 2', status='1')
        post3 = Post.objects.create(author=self.user, text='test content 3', title='test title 3', status='1')

        # Create a user and retrieve the access token
        response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
        self.assertEqual(response.status_code, 200)
        self.access_token = response.data.get('access')
    
    def test_modo_normal_access(self):
        print("Test no modo noramle access")
        # Set the access token in the Authorization header
        self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

        # Make a GET request to retrieve the list of posts
        response = self.client.get('/posts/?amount=3')
        print("response" + str(response))
        print("response.data" + str(response.data))

        posts = response.data
        for post in posts:
            print(f"Post ID: {post['id']}")
            print(f"Title: {post['title']}")
            print(f"Date: {post['date']}")
            print(f"Status: {post['status']}")
            print()  # Print an empty line between posts
        # Check the response status code
        self.assertEqual(response.status_code, 200)

    def test_no_modo_modo_access(self):
        print("Test no modo modo access")
        # Set the access token in the Authorization header
        self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

        # Make a GET request to retrieve the list of posts
        response = self.client.get('/posts/?moderate=True&amount=3')
        print("response" + str(response))
        print("response.data" + str(response.data))

        posts = response.data
        for post in posts:
            print(f"Post ID: {post['id']}")
            print(f"Title: {post['title']}")
            print(f"Date: {post['date']}")
            print(f"Status: {post['status']}")
            print()