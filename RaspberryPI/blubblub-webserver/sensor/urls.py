from django.conf.urls import url
from . import views

urlpatterns = [
	url(r'^sensor/$', views.showData, name='showData'),
]
