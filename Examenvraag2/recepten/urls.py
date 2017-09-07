from django.conf.urls import url
from recepten import views

app_name = 'recepten'
urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^create/$', views.create, name='create'),
    url(r'^recept/create/$', views.createrecept, name='createrecept'),
]
