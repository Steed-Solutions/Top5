from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name="home"),
    path('<int:page_number>/', views.home, name="home"),
    path('profile/', views.profile, name="profile"),
    path('browse/', views.browse, name="browse"),
    path('saved/<int:page_number>/', views.saved, name="saved"),
    path('posts/<str:post_title_id>/', views.post, name="post"),
    path('auth/', views.credentials, name='credentials'),
    path('logout/', views.logout, name="logout"),
    path('comingSoon/', views.comingSoon, name='comingSoon'),
    path('invalid_page/', views.invalid, name="invalid")
]
