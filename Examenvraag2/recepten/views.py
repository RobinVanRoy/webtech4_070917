from django.shortcuts import render
from pymongo import MongoClient


def connection():
    # Create a new connection to a single MongoDB instance at host:port.
    connection = MongoClient('localhost', 27017)
    # Create and get a data base (db) from this connection
    db = connection.test
    # Create and get a collection named "recepten" from the data base named "db" from the connection named "connection"
    collection = db.recepten
    connection.close()
    return collection


def index(request):
    recepten = connection()
    recepten = recepten.sort('$name', -1).sort('$calorien', -1)
    context = {'recepten': recepten}
    print(context)
    return render(request, 'recepten/index.html', context)


