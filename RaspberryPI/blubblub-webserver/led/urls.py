from django.conf.urls import url
from . import views

urlpatterns = [
	url(r'^ledOn/$', views.ledOnView, name='ledOn'),
	url(r'^ledOff/$', views.ledOffView, name='ledOff'),
]
