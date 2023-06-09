from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import Account, Post, Comment, Image, Video

class AccountAdmin(UserAdmin):
    list_display = ('email', 'first_name', 'last_name', 'ensisaGroup')

    fieldsets = (
        ('ID', {'fields': ('email', 'password')}),
        ('Personal info', {'fields': ('first_name', 'last_name', 'ensisaGroup', 'profile_picture')}),
        ('Permissions', {'fields': ('is_active', 'is_staff', 'is_superuser', 'groups', 'user_permissions')}),
        ('Important dates', {'fields': ('last_login', 'date_joined')}),
    )

    add_fieldsets = (
        ('ID', {'fields': ('email', 'password1', 'password2')}),
        ('Personal info', {'fields': ('first_name', 'last_name', 'ensisaGroup', 'profile_picture')}),
        ('Permissions', {'fields': ('is_active', 'is_staff', 'is_superuser', 'groups', 'user_permissions')}),
    )

admin.site.register(Account, AccountAdmin)
admin.site.register(Post)
admin.site.register(Comment)
admin.site.register(Image)
admin.site.register(Video)