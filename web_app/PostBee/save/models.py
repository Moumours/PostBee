from django.db import models
from django.utils import timezone
from django.contrib.auth.models import AbstractUser
from django.contrib.auth import get_user_model

# Create your models here.

class User(AbstractUser):
    DEF_GROUP = [
        ('0', 'Student'),
        ('1', 'Teacher'),
        ('2', 'Staff')
    ]
    
    email = models.EmailField(unique=True)
    group = models.CharField(max_length=1, choices=DEF_GROUP, default='0')
    
    def __str__(self):
        return self.email

# class Post(models.Model):
#     DEF_STATUS = [
#         ('0', 'En attente de validation'),
#         ('1', 'En ligne'),
#         ('2', 'Archivé')
#     ]

#     title = models.CharField(max_length=100)
#     author = models.ForeignKey(get_user_model(), on_delete=models.CASCADE)
#     date = models.DateTimeField(default=timezone.now)
#     text = models.TextField()
#     status = models.CharField(max_length=1, choices=DEF_STATUS, default='wating')

#     def __str__(self):
#         return self.title
    
# class Comment(models.Model):
#     post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='comments')
#     author = models.ForeignKey(get_user_model(), on_delete=models.CASCADE)
#     date = models.DateTimeField(default=timezone.now)
#     text = models.CharField(max_length=250)

#     def __str__(self):
#         return self.text
    
# class Image(models.Model):
#     post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='images')
#     image = models.ImageField(upload_to='images/')

#     def __str__(self):
#         return self.image.name

# class Video(models.Model):
#     post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='videos')
#     video = models.FileField(upload_to='videos/')

#     def __str__(self):
#         return self.video.name

# class ProfilePicture(models.Model):
#     user = models.OneToOneField(get_user_model(), on_delete=models.CASCADE)
#     image = models.ImageField(upload_to='profile_pictures/')

#     def __str__(self):
#         return self.image.name