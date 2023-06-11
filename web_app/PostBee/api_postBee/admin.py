from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import Account, Post, Comment, Image, Video

class ProfileInline(admin.TabularInline):
    model = Post
    extra = 0

class CommentInline(admin.TabularInline):
    model = Comment
    extra = 0

class ImageInline(admin.TabularInline):
    model = Image
    extra = 0

class VideoInline(admin.TabularInline):
    model = Video
    extra = 0



# Customizing the admin page for Account and display in it the Post that the user has created
class AccountAdmin(UserAdmin):
    list_display = ('email', 'first_name', 'last_name', 'ensisaGroup', "_posts")

    fieldsets = (
        ('ID', {'fields': ('email', 'password')}),
        ('Personal info', {'fields': ('first_name', 'last_name', 'ensisaGroup', 'profile_picture')}),
        ('Permissions', {'fields': ('is_active', 'is_staff', 'is_superuser')}),
        ('Important dates', {'fields': ('last_login', 'date_joined')}),
    )

    inlines = [
        ProfileInline,
    ]

    add_fieldsets = (
        ('ID', {'fields': ('email', 'password1', 'password2')}),
        ('Personal info', {'fields': ('first_name', 'last_name', 'ensisaGroup', 'profile_picture')}),
        ('Permissions', {'fields': ('is_active', 'is_staff', 'is_superuser')}),
    )

    def _posts(self, obj):
        return obj.post_set.count()

class PostAdmin(admin.ModelAdmin):
    list_display = ('title', 'author', 'date', 'status')

    inlines = [
        ImageInline,
        VideoInline,
        CommentInline,
    ]


admin.site.register(Account, AccountAdmin)
admin.site.register(Post, PostAdmin)
admin.site.register(Comment)
admin.site.register(Image)
admin.site.register(Video)