from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from .motor import run
from motor.models import FeedingTime
from authenticate.models import TokenRegister
import sys, time, os
from sensor.data import Data

# Create your views here.

@csrf_exempt
def feeding(request):
    token = request.POST['token']
    objects = TokenRegister.objects.all()
    tokenList = [t.token for t in objects]
    print(tokenList)
    result = 0
    if token in tokenList:
        t = time.localtime()
        Data.time = t.tm_hour*60 + t.tm_min

        feedingtime = FeedingTime.objects.get(pk=1)
        feedingtime.lasttime = Data.time
        feedingtime.save()

        run()
        result = 1
    print(Data.time, t.tm_hour*60 + t.tm_min)
    context={'time':result}
    return render(request, 'motor/feeding.html', context)
