from __future__ import unicode_literals
from django.db import models


class Ingredient(models.Model):
    name = models.CharField(max_length=50)

    def __str__(self):
        return self.name


class Recept(models.Model):
    name = models.CharField(max_length=50)
    ingredienten = models.ForeignKey(Ingredient)
    calorien = models.FloatField()
    time = models.FloatField()

    def __str__(self):
        return self.name
