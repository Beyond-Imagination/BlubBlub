from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from .data import Data
from .sensors import Sensors
import sys, time

sensorThread = Sensors()
sensorThread.start()


# Create your views here.
@csrf_exempt
def showData(request):
    global sensorThread
    if not sensorThread.is_alive():
        sensorThread = Sensors()
        sensorThread.start()
    context={'temperature':Data.temperature, 'illuminance':Data.illuminance,\
    'turbidity':Data.turbidity, 'time':Data.time}
    return render(request, 'sensor/sensorData.html', context)
