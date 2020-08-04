from django.urls import path
from . import views

urlpatterns = [
    path('', views.credentials, name='credentials'),
    path('dashboard/', views.dashboard, name='dashboard'),
    path('/', views.logout, name="logout"),
    path('myDash/', views.myDash, name='myDash'),
    path('foodDash/<category_id>/', views.foodDash, name='foodDash'),
]
