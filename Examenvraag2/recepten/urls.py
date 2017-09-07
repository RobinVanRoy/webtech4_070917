from django.conf.urls import url
from recepten import views

app_name = 'recepten'
urlpatterns = [
    url(r'^$', views.index, name='index'),
    # ex: everything/car/1  --> detailview
    url(r'^car/(?P<pk>[0-9]+)/$', views.CarDetailView.as_view(), name='cardetail'),
    # ex: everything/carbrand/1  --> detailview
    url(r'^carbrand/(?P<pk>[0-9]+)/$', views.CarbrandDetailView.as_view(), name='carbranddetail'),
    # ex: everything/car/create  --> createview
    url(r'^car/create/$', views.CarCreateView.as_view(), name='carcreate'),
    # ex: everything/carbrand/create  --> createview
    url(r'^carbrand/create/$', views.CarbrandCreateView.as_view(), name='carbrandcreate'),
    # ex: everything/car/update  --> updateview
    url(r'^car/update/(?P<pk>[0-9]+)/$', views.CarUpdateView.as_view(), name='carupdate'),
    # ex: everything/car/delete/1  --> deleteview
    url(r'^car/delete/(?P<pk>[0-9]+)/$', views.CarDeleteView.as_view(), name='cardelete')
]
