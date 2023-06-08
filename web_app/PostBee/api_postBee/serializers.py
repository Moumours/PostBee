from rest_framework.serializers import ModelSerializer
from api_postBee.models import Post

class PostSerializer(ModelSerializer):
    class Meta:
        model = Post
        fields = ['id', 'title', 'date', 'status']