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


# class PostPublishSerializer(ModelSerializer):
#     class Meta:
#         model = Post
#         fields = ['title', 'text']
    
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

class ResetPasswordSerializer(ModelSerializer):
    email = serializers.EmailField()
    class Meta:
        model = Account
        fields = ['email']

class TokenRefreshSerializer(serializers.Serializer):
    refresh = serializers.CharField()

class ChangePasswordSerializer(serializers.Serializer):
    old_password = serializers.CharField(required=True)
    new_password = serializers.CharField(required=True)

    class Meta:
        model = Account
        fields = ['old_password', 'new_password']

class ImageSerializer(serializers.ModelSerializer):
    image = serializers.ImageField(max_length=None, use_url=True)
    class Meta:
        model = Image
        fields = "__all__"

class VideoSerializer(serializers.ModelSerializer):
    video = serializers.FileField(max_length=None, use_url=True)
    class Meta:
        model = Video
        fields = "__all__"


class PostPublishSerializer(serializers.Serializer):
    images = serializers.ListField(
        child=serializers.ImageField(allow_empty_file=False, use_url=False),
        write_only=True, required=False, allow_null=True
    )
    videos = serializers.ListField(
        child=serializers.FileField(allow_empty_file=False, use_url=False),
        write_only=True, required=False, allow_null=True
    )
    title = serializers.CharField(max_length=100)
    text = serializers.CharField()

    class Meta:
        model = Post
        fields = ['title', 'text', 'images', 'videos']

    def create(self, validated_data):
        images_data = validated_data.pop('images', [])
        videos_data = validated_data.pop('videos', [])

        post = Post.objects.create(**validated_data)

        for image_data in images_data:
            Image.objects.create(post=post, image=image_data)

        for video_data in videos_data:
            Video.objects.create(post=post, video=video_data)
            
        return post