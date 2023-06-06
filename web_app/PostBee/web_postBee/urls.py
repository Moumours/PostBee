from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('register', views.register, name='register'),
    path('activate/<uidb64>/<token>', views.activate, name='activate'),
    # path('test', views.test, name='test'),
    path('login', views.login, name='login'),
]