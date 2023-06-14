from django.contrib.auth.models import AbstractUser, UserManager
from django.db import models
from django.utils import timezone
from django.dispatch import receiver
import os

# CustomUserManager using email, password and automatically generated username
class CustomUserManager(UserManager):
    def create_user(self, email, password, **extra_fields):
        if not email:
            raise ValueError('Email must be set')
        if not password:
            raise ValueError('Password must be set')
        user = self.model(email=self.normalize_email(email), **extra_fields)
        user.set_password(password)
        user.save()
        return user
    
    def create_superuser(self, email, password, **extra_fields):
        if not email:
            raise ValueError('Email must be set')
        if not password:
            raise ValueError('Password must be set')
        user = self.model(email=self.normalize_email(email), **extra_fields)
        user.set_password(password)
        user.is_staff = True
        user.is_superuser = True
        user.save()
        return user


class Account(AbstractUser):
    SCHOOL_GROUP_CHOICES = [
        ('0', 'Student'),
        ('1', 'Teacher'),
        ('2', 'Staff'),
    ]

    email = models.EmailField(unique=True)
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    ensisaGroup = models.CharField(max_length=10, choices=SCHOOL_GROUP_CHOICES, default='0')
    username = models.CharField(max_length=150, unique=False, blank=True)
    profile_picture = models.IntegerField(default=0)

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['first_name', 'last_name', 'ensisaGroup']

    objects = CustomUserManager()

    def save(self, *args, **kwargs):
        if not self.username:
            self.username = f'{self.first_name.lower()}.{self.last_name.lower()}'
        super().save(*args, **kwargs)

class Post(models.Model):
    DEF_STATUS = [
        ('0', 'En attente de validation'),
        ('1', 'En ligne'),
        ('2', 'Archivé')
    ]

    title = models.CharField(max_length=100)
    author = models.ForeignKey(Account, on_delete=models.CASCADE)
    date = models.DateTimeField(default=timezone.now)
    text = models.TextField()
    status = models.CharField(max_length=1, choices=DEF_STATUS, default='0')

    def __str__(self):
        return self.title
    
class Comment(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='comments')
    author = models.ForeignKey(Account, on_delete=models.CASCADE)
    date = models.DateTimeField(default=timezone.now)
    text = models.CharField(max_length=250)

    def __str__(self):
        return self.text
    
class Image(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='images')
    image = models.ImageField(upload_to='images/')

    def __str__(self):
        return self.image.name

class Video(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name='videos')
    video = models.FileField(upload_to='videos/')

    def __str__(self):
        return self.video.name

@receiver(models.signals.post_delete, sender=Image)
def post_save_image(sender, instance, *args, **kwargs):
    if instance.image:
        if os.path.isfile(instance.image.path):
            os.remove(instance.image.path)