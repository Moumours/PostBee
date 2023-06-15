from rest_framework.serializers import ModelSerializer, SerializerMethodField, PrimaryKeyRelatedField
from api_postBee.models import *
from rest_framework import serializers
import base64
from django.core.exceptions import ValidationError
from django.utils.deconstruct import deconstructible

"""
====================
    POST LIST
====================
"""

class AuthorSerializer(ModelSerializer):
    full_name = SerializerMethodField()

    def get_full_name(self, author):
        return f"{author.first_name} {author.last_name}"

    class Meta:
        model = Account
        fields = ['full_name', 'profile_picture']

class PostListSerializer(ModelSerializer):
    author = AuthorSerializer()
    date = serializers.DateTimeField(format="%Y-%m-%dT%H:%M:%S%z")

    class Meta:
        model = Post
        fields = ['id', 'title', 'date', 'status', 'author']


"""
====================
    POST DETAIL
====================
"""

class CommentSerializer(ModelSerializer):
    author = AuthorSerializer()
    date = serializers.DateTimeField(format="%Y-%m-%dT%H:%M:%S%z")

    class Meta:
        model = Comment
        fields = ['text', 'author', 'date']

class AttachmentSerializer(ModelSerializer):

    def get_type(self, obj):
        type = obj.url.name.split('.')[-1]
        if type in ['jpg', 'jpeg', 'png']:
            return 'image'
        elif type in ['mp4', 'avi', 'mov']:
            return 'video'
        elif type in ['gif']:
            return 'gif'

    type = SerializerMethodField()
    class Meta:
        model = Attachment
        fields = ['url', 'type']

class PostDetailSerializer(ModelSerializer):
    comments = SerializerMethodField()
    attachments = AttachmentSerializer(many=True)

    class Meta:
        model = Post
        fields = ['text', 'comments', 'attachments']

    def get_comments(self, obj):
        comments = obj.comments.order_by('-date')
        serializer = CommentSerializer(comments, many=True)
        return serializer.data


"""
====================
    PUBLISH COMMENT
====================
"""
    
class CommentPublishSerializer(ModelSerializer):
    post = PrimaryKeyRelatedField(queryset=Post.objects.all())
    class Meta:
        model = Comment
        fields = ['text', 'post']


"""
====================
    APPROVE POST
====================
"""

class ApprovePostSerializer(ModelSerializer):
    response = serializers.BooleanField()
    postId = serializers.IntegerField()
    class Meta:
        model = Post
        fields = ['postId', 'response']


"""
====================
    CHANGE MODO
====================
"""

class DeleteAndModoUserSerializer(ModelSerializer):
    class Meta:
        model = Account
        fields = ['id']


"""
====================
    COMMENT DELETE
====================
"""

class DeleteCommentSerializer(ModelSerializer):
    class Meta:
        model = Comment
        fields = ['id']


"""
====================
    USER PROFILE
====================
"""

class UserSerializer(ModelSerializer):
    class Meta:
        model = Account
        fields = ['first_name', 'last_name', 'email', 'ensisaGroup', 'profile_picture', 'is_staff']


"""
====================
    RESET PASSWORD
====================
"""

class ResetPasswordSerializer(ModelSerializer):
    email = serializers.EmailField()
    class Meta:
        model = Account
        fields = ['email']



"""
====================
    CHANGE PASSWORD
====================
"""

class ChangePasswordSerializer(serializers.Serializer):
    old_password = serializers.CharField(required=True)
    new_password = serializers.CharField(required=True)

    class Meta:
        model = Account
        fields = ['old_password', 'new_password']


"""
====================
    POST PUBLISH
====================
"""

@deconstructible
class FileExtensionValidator:
    def __init__(self, allowed_extensions):
        self.allowed_extensions = allowed_extensions

    def __call__(self, value):
        extension = value.name.split('.')[-1]
        if extension not in self.allowed_extensions:
            raise ValidationError(f"Only {', '.join(self.allowed_extensions)} file extensions are allowed.")


class PostPublishSerializer(serializers.Serializer):

    def validate_attachments(self, attachments):
        attachments_extensions = ['jpg', 'jpeg', 'png', 'mp4', 'avi', 'mov', 'gif']
        validate_extension = FileExtensionValidator(allowed_extensions=attachments_extensions)

        for attachment in attachments:
            validate_extension(attachment)

        return attachments

    attachments = serializers.ListField(
        child=serializers.FileField(allow_empty_file=False, use_url=False),
        write_only=True, required=False, allow_null=True
    )
    title = serializers.CharField(max_length=100)
    text = serializers.CharField()

    class Meta:
        model = Post
        fields = ['title', 'text', 'attachments']

    def create(self, validated_data):
        attachments_data = validated_data.pop('attachments', [])

        post = Post.objects.create(**validated_data)

        for attachment_data in attachments_data:
            Attachment.objects.create(post=post, url=attachment_data)
            
        return post