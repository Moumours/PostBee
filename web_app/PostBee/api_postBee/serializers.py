from rest_framework.serializers import ModelSerializer, SerializerMethodField, PrimaryKeyRelatedField
from api_postBee.models import *

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
    class Meta:
        model = Post
        fields = '__all__'