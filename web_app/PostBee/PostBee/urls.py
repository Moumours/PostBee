"""PostBee URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include
from rest_framework_simplejwt.views import (TokenObtainPairView, TokenRefreshView)
from rest_framework import routers
from api_postBee import views as core_views
from django.conf import settings
from django.conf.urls.static import static

from api_postBee.views import *

postRouter = routers.SimpleRouter()
postRouter.register('posts', PostList, basename='post')
postRouter.register('post', PostDetail, basename='postDetail')
postRouter.register('users', UsersLists, basename='usersLists')

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/token', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('refresh_token', TokenRefreshView.as_view(), name='token_refresh'),
    path('login', LoginView.as_view(), name='login'),
    path('register', RegisterView.as_view(), name='register'),
    path('activate/<uidb64>/<token>', ActivateAccount.as_view(), name='activate'),
    path('', IndexView.as_view(), name='index'),
    path('', include(postRouter.urls)),
    path('publish', PublishPost.as_view(), name='publish'),
    path('comment', PublishComment.as_view(), name='comment'),
    path('approve', ApprovePost.as_view(), name='approve'),
    path('delete_user', DeleteUser.as_view(), name='delete_user'),
    path('add_modo', AddModo.as_view(), name='add_modo'),
    path('delete_comment', DeleteComment.as_view(), name='delete_comment'),
    path('user_info', UserView.as_view(), name='get_user'),
    path('logout', LogoutView.as_view(), name='logout'),
    path("favicon.ico", core_views.favicon),
    path('reset_password', ResetPassword.as_view(), name='reset_password'),
    path('reset_password/confirm/<uidb64>/<token>', ResetPasswordConfirm.as_view(), name='reset_password_confirm'),
    path('change_password', ChangePassword.as_view(), name='change_password'),
    path('test', Test.as_view(), name='test'),
]+ static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
