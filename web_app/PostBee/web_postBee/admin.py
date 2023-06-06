from django.contrib import admin
from django.contrib.auth import get_user_model
from django.contrib.auth.admin import UserAdmin

from .forms import RegisterForm, ChangeUserForm
from .models import Account

class PostUserAdmin(UserAdmin):
    add_form = RegisterForm
    form = ChangeUserForm
    model = Account
    list_display = ['email', 'is_active', 'first_name', 'last_name', 'ensisaGroup']

    def add_view(self, request, form_url='', extra_context=None):
        self.form = self.add_form
        return super().add_view(request, form_url=form_url, extra_context=extra_context)

    def change_view(self, request, object_id, form_url='', extra_context=None):
        self.form = self.form
        return super().change_view(request, object_id, form_url=form_url, extra_context=extra_context)

# Register your models here.

admin.site.register(Account, PostUserAdmin)
