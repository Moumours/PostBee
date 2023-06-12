from rest_framework.serializers import ModelSerializer, SerializerMethodField, PrimaryKeyRelatedField
from api_postBee.models import *
from rest_framework import serializers

class AuthorSerializer(ModelSerializer):
    full_name = SerializerMethodField()

    def get_full_name(self, author):
        return f"{author.first_name} {author.last_name}"

    class Meta:
        model = Account
        fields = ['full_name']

class CommentSerializer(ModelSerializer):
    author = AuthorSerializer()

    class Meta:
        model = Comment
        fields = ['text', 'author', 'date']

class PostListSerializer(ModelSerializer):
    author = AuthorSerializer()

    class Meta:
        model = Post
        fields = ['id', 'title', 'date', 'status', 'author']


class PostDetailSerializer(ModelSerializer):
    comments = SerializerMethodField()

    class Meta:
        model = Post
        fields = ['text', 'comments']

    def get_comments(self, obj):
        comments = obj.comments.order_by('-date')  # Sort comments by date in descending order
        serializer = CommentSerializer(comments, many=True)
        return serializer.data


class PostPublishSerializer(ModelSerializer):
    class Meta:
        model = Post
        fields = ['title', 'text']
    
class CommentPublishSerializer(ModelSerializer):
    post = PrimaryKeyRelatedField(queryset=Post.objects.all())
    class Meta:
        model = Comment
        fields = ['text', 'post']

class ApprovePostSerializer(ModelSerializer):
    response = serializers.BooleanField()
    postId = serializers.IntegerField()
    class Meta:
        model = Post
        fields = ['postId', 'response']

class DeleteAndModoUserSerializer(ModelSerializer):
    class Meta:
        model = Account
        fields = ['id']

class DeleteCommentSerializer(ModelSerializer):
    class Meta:
        model = Comment
        fields = ['id']

class UserSerializer(ModelSerializer):
    class Meta:
        model = Account
        fields = ['first_name', 'last_name', 'email', 'ensisaGroup', 'profile_picture', 'is_staff']