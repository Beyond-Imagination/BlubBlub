from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from .data import Data
from .motor import run
from .feeding import FeedingMotor
import sys, time


# Create your views here.
@csrf_exempt
def showData(request):
    context={'temperature':Data.temperature, 'illuminance':Data.illuminance,\
    'turbidity':Data.turbidity, 'time':Data.time}
    return render(request, 'sensor/sensorData.html', context)

@csrf_exempt
def feeding(request):
    run()
    t = time.localtime()
    Data.time = t.tm_hour*60 + t.tm_min
    context={'time':1}
    return render(request, 'sensor/feeding.html', context)
    
