from django.test import TestCase
from api_postBee.models import Account
from django.urls import reverse
from rest_framework import status
from rest_framework.test import APIClient

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


class PostListTestCase(TestCase):
    def setUp(self):
        self.client = APIClient()
        self.user = Account.objects.create_user(
            email='test@example.com',
            password='testpassword'
        )

        # Create a user and retrieve the access token
        response = self.client.post(reverse('login'), data={'email': 'test@example.com', 'password': 'testpassword'})
        self.assertEqual(response.status_code, 200)
        self.access_token = response.data.get('access')

    def test_get_post_list(self):
        # Set the access token in the Authorization header
        self.client.credentials(HTTP_AUTHORIZATION=f'Bearer {self.access_token}')

        # Make a GET request to retrieve the list of posts
        response = self.client.get(reverse('post-list'))

        # Check the response status code
        self.assertEqual(response.status_code, 200)

        # Print the list of posts
        posts = response.data
        for post in posts:
            print(f"Post ID: {post['id']}")
            print(f"Title: {post['title']}")
            print(f"Date: {post['date']}")
            print(f"Status: {post['status']}")
            print()  # Print an empty line between posts
