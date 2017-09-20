from django.shortcuts import render
from django.views.decorators.csrf import ensure_csrf_cookie
from .data import Data
from .motor import run
from .feeding import FeedingMotor
import sys


# Create your views here.
@ensure_csrf_cookie
def showData(request):
    context={'temperature':Data.temperature, 'illuminance':Data.illuminance,\
    'turbidity':Data.turbidity}
    return render(request, 'sensor/sensorData.html', context)

@ensure_csrf_cookie
def feeding(request):
#    runMotor()
#    motor = FeedingMotor()
    run()
#    motor.start()
    context={'time':1}
    return render(request, 'sensor/feeding.html', context)
    
