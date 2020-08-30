from django.urls import path
from . import views

urlpatterns = [
    path('', views.credentials, name='credentials'),
    path('comingSoon', views.comingSoon, name='comingSoon')
]
