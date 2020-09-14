from django.urls import path
from . import views

urlpatterns = [
    path('', views.credentials, name='credentials'),
    path('dashboard', views.dashboard, name='dashboard'),
    path('logout', views.logout, name="logout"),
    path('myDash', views.myDash, name='myDash'),
    path('categoryDash/<category_id>', views.categoryDash, name='categoryDash'),
]
