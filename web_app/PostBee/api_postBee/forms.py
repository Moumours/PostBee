from django import forms
from django.contrib.auth.forms import UserCreationForm, UserChangeForm, AuthenticationForm
from .models import Account, Comment, Post

class RegisterForm(UserCreationForm):
    #Register form asking for FirstName, LastName, Email, Password1, Password2, and the group (Student or Teacher or staff)
    first_name = forms.CharField(max_length=30, required=True, help_text='Required.')
    last_name = forms.CharField(max_length=30, required=True, help_text='Required.')
    email = forms.EmailField(max_length=254, help_text='Required. Inform a valid email address.')
    ensisaGroup = forms.ChoiceField(choices=[('0', 'Student'), ('1', 'Teacher'), ('2', 'Staff')], required=True, help_text='Required.')

    class Meta:
        model = Account
        fields = ('first_name', 'last_name', 'email', 'ensisaGroup', 'password1', 'password2')

    def save(self, commit=True):
        user = super(RegisterForm, self).save(commit=False)
        user.email = self.cleaned_data['email']
        if commit:
            user.save()
        return user

class CommentForm(forms.Form):
    #Comment form asking for the comment content
    post_id = forms.IntegerField(required=True, help_text='Required.')
    author_id = forms.IntegerField(required=True, help_text='Required.')
    content = forms.CharField(max_length=1000, required=True, help_text='Required.')

    class Meta:
        model = Comment
        fields = ('post_id', 'author_id', 'content')
    
    def save(self, commit=True):
        comment = super(CommentForm, self).save(commit=False)
        comment.post_id = self.cleaned_data['post_id']
        comment.author_id = self.cleaned_data['author_id']
        comment.content = self.cleaned_data['content']
        if commit:
            comment.save()
        return comment

class PublishPost(forms.Form):
    #Publish form asking for the post content
    title = forms.CharField(max_length=100, required=True, help_text='Required.')
    author_id = forms.IntegerField(required=True, help_text='Required.')
    content = forms.CharField(max_length=1000, required=True, help_text='Required.')

    class Meta:
        model = Post
        fields = ('author_id', 'content', 'title')
    
    def save(self, commit=True):
        post = super(PublishPost, self).save(commit=False)
        post.author_id = self.cleaned_data['author_id']
        post.content = self.cleaned_data['content']
        post.title = self.cleaned_data['title']
        if commit:
            post.save()
        return post