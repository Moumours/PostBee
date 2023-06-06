# from django.db import models
# from django.contrib.auth.models import AbstractUser

# # Create your models here.
# class PostUser(AbstractUser):
#     GROUP_DEF=[
#         ('0', 'Student'),
#         ('1', 'Teacher'),
#         ('2', 'Staff')
#     ]
#     email = models.EmailField(unique=True)
#     ensisaGroup = models.CharField(max_length=1, choices=GROUP_DEF, default='0')

#     def __str__(self):
#         return self.email
    

from django.db import models
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager

#manager for our custom model 
class MyAccountManager(BaseUserManager):
    """
        This is a manager for Account class 
    """
    def create_user(self, email, first_name, last_name, school_group, password=None):
        if not email:
            raise ValueError("Users must have an Emaill address")
        if not  first_name:
            raise ValueError("Users must have a First name")
        if not last_name:
            raise ValueError("Users must have a Last name")
        if not school_group:
            raise ValueError("Users must have a School group")
        user  = self.model(
                email=self.normalize_email(email),
                first_name=self.first_name,
                last_name=self.last_name,
                school_group=self.school_group,
            )

        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, email, first_name, last_name, school_group, password):
        user = self.create_user(
                email=self.normalize_email(email),
                password=password,
                first_name=self.first_name,
                last_name=self.last_name,
                school_group=self.school_group,
            )
        user.is_admin = True
        user.is_staff=True
        user.is_superuser=True
        user.save(using=self._db)
        return user

class Account(AbstractBaseUser):
    
    """
      Custom user class inheriting AbstractBaseUser class 
    """
    
    GROUP_DEF=[
        ('0', 'Student'),
        ('1', 'Teacher'),
        ('2', 'Staff')
    ]

    email                = models.EmailField(verbose_name='email', max_length=60, unique=True)
    first_name           = models.CharField(max_length=30)
    last_name            = models.CharField(max_length=30)
    date_joined          = models.DateTimeField(verbose_name='date joined', auto_now_add=True)
    last_login           = models.DateTimeField(verbose_name="last login", auto_now=True)
    school_group         = models.CharField(max_length=1, choices=GROUP_DEF, default='0')
    is_admin             = models.BooleanField(default=False)
    is_active            = models.BooleanField(default=False)
    is_staff             = models.BooleanField(default=False)
    is_superuser         = models.BooleanField(default=False)

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = ['email', 'first_name', 'last_name', 'school_group']

    objects = MyAccountManager()

    def __str__(self):
        return self.email

    def has_perm(self, perm, obj=None):
        return self.is_admin
    def has_module_perms(self, app_label ):
        return True