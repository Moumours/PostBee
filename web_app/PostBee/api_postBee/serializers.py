from rest_framework.serializers import ModelSerializer, SerializerMethodField
from api_postBee.models import Post, Account

class AuthorSerializer(ModelSerializer):
    full_name = SerializerMethodField()

    def get_full_name(self, author):
        return f"{author.first_name} {author.last_name}"

    class Meta:
        model = Account
        fields = ['full_name']

class PostListSerializer(ModelSerializer):
    author = AuthorSerializer()

    class Meta:
        model = Post
        fields = ['id', 'title', 'date', 'status', 'author']


class PostDetailSerializer(ModelSerializer):
    class Meta:
        model = Post
        fields = ['text']