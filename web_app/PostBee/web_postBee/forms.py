from django import forms
from django.contrib.auth.forms import UserCreationForm, UserChangeForm, AuthenticationForm
from .models import Account

class UserAuthenticationForm(AuthenticationForm):
    email = forms.EmailField(widget=forms.TextInput(attrs={'autofocus': True}))

class RegisterForm(UserCreationForm):
    #Register form asking for FirstName, LastName, Email, Password1, Password2, and the group (Student or Teacher or staff)
    first_name = forms.CharField(max_length=30, required=True, help_text='Required.')
    last_name = forms.CharField(max_length=30, required=True, help_text='Required.')
    email = forms.EmailField(max_length=254, help_text='Required. Inform a valid email address.')
    ensisaGroup = forms.ChoiceField(choices=[('0', 'Student'), ('1', 'Teacher'), ('2', 'Staff')], required=True, help_text='Required.')

    class Meta:
        model = Account
        fields = ('first_name', 'last_name', 'email', 'ensisaGroup', 'password1', 'password2', )

    def save(self, commit=True):
        user = super(RegisterForm, self).save(commit=False)
        user.email = self.cleaned_data['email']
        user.ensisaGroup = self.cleaned_data['group']
        if commit:
            user.save()
        return user

class ChangeUserForm(UserChangeForm):
    #ChangeUserForm asking for FirstName, LastName, Email, and the group (Student or Teacher or staff)
    first_name = forms.CharField(max_length=30, required=True, help_text='Required.')
    last_name = forms.CharField(max_length=30, required=True, help_text='Required.')
    email = forms.EmailField(max_length=254, help_text='Required. Inform a valid email address.')
    ensisaGroup = forms.ChoiceField(choices=[('0', 'Student'), ('1', 'Teacher'), ('2', 'Staff')], required=True, help_text='Required.')

    class Meta:
        model = Account
        fields = ('first_name', 'last_name', 'email', 'ensisaGroup', )

    def save(self, commit=True):
        user = super(ChangeUserForm, self).save(commit=False)
        user.email = self.cleaned_data['email']
        user.ensisaGroup = self.cleaned_data['group']
        if commit:
            user.save()
        return user