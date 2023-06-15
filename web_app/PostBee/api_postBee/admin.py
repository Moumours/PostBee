from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import Account, Post, Comment, Attachment

class ProfileInline(admin.TabularInline):
    model = Post
    extra = 0

class CommentInline(admin.TabularInline):
    model = Comment
    extra = 0

class AttachmentInline(admin.TabularInline):
    model = Attachment
    extra = 0

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

    fieldsets = (
        ('ID', {'fields': ('title', 'author', 'date')}),
        ('Content', {'fields': ('text',)}),
        ('Status', {'fields': ('status',)}),
    )

    inlines = [

        CommentInline,
        AttachmentInline,
    ]


admin.site.register(Account, AccountAdmin)
admin.site.register(Post, PostAdmin)
admin.site.register(Comment)
admin.site.register(Attachment)