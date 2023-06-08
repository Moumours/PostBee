from rest_framework.serializers import ModelSerializer
from api_postBee.models import Post

class PostListSerializer(ModelSerializer):
    class Meta:
        model = Post
        fields = ['id', 'title', 'date', 'status', 'author']

class PostDetailSerializer(ModelSerializer):
    class Meta:
        model = Post
        fields = ['text']