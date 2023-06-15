from django import forms
from django.contrib.auth.forms import UserCreationForm, UserChangeForm, AuthenticationForm
from .models import Account, Comment, Post

class RegisterForm(UserCreationForm):

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