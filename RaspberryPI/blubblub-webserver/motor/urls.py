from django.conf.urls import url
from . import views

urlpatterns = [
	url(r'^feeding/$', views.feeding, name='feeding'),
]
