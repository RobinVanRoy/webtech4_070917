from django.shortcuts import render
from pymongo import MongoClient


def connection():
    # mongo1 = pymongo.Connection('127.0.0.1')


    # Create a new connection to a single MongoDB instance at host:port.
    connection = MongoClient('localhost', 27017)
    # print connection.database_names()
    # Create and get a data base (db) from this connection
    db = connection.receptenDB
    # Create and get a collection named "recepten" from the data base named "db" from the connection named "connection"
    collection = db.recepten
    connection.close()
    return collection


def index(request):
    collection = connection()
    recepten = collection.find().sort('$name', -1).sort('$calorien', -1)
    context = {'recepten': recepten}
    # print(context)
    return render(request, 'recepten/index.html', context)


def create(request):
    return render(request, 'recepten/create.html')


def createrecept(request):
    name = request.POST.get('input_naam')
    time = request.POST.get('input_tijd')
    calorien = request.POST.get('input_calorien')
    ingredienten = request.POST.get('input_ingredienten')

    connection = MongoClient('localhost', 27017)
    db = connection.receptenDB

    db.recepten.insert_one({
        "name": name,
        "calorien": calorien,
        "time": time,
        "ingredienten": ingredienten
    })

    collection = db.recepten
    
    recepten = collection.find().sort('$name', -1).sort('$calorien', -1)
    context = {'recepten': recepten}
    return render(request, 'recepten/index.html', context)



