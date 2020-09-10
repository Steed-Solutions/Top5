from django.urls import path
from . import views

urlpatterns = [
    path('', views.home, name="home"),
    path('profile/', views.profile, name="profile"),
    path('browse/', views.browse, name="browse"),
    path('saved/', views.saved, name="saved"),
    path('auth/', views.credentials, name='credentials'),
    path('logout/', views.logout, name="logout"),
    path('comingSoon/', views.comingSoon, name='comingSoon')
]
