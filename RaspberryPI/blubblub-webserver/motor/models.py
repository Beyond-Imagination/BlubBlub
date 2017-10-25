from django.db import models

# Create your models here.
class FeedingTime(models.Model):
    lasttime = models.IntegerField()

    def __str__(self):
        return self.lasttime

if FeedingTime.objects.count() == 0:
    t = FeedingTime(lasttime = 0)
    t.save()
