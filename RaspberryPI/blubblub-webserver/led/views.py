from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from .led import *
from authenticate.models import TokenRegister
import sys, time


# Create your views here.
@csrf_exempt
def ledOnView(request):
    token = request.POST['token']
    objects = TokenRegister.objects.all()
    tokenList = [ t.token for t in objects]
    print(tokenList)
    result = 0
    if token in tokenList:
        TurnOn()
        result = 1
    context={'result':result}
    return render(request, 'led/ledOn.html', context)

@csrf_exempt
def ledOffView(request):
    token = request.POST['token']
    objects = TokenRegister.objects.all()
    tokenList = [ t.token for t in objects]
    print(tokenList)
    result = 0
    if token in tokenList:
        TurnOff()
        result = 1
    context={'result':result}
    return render(request, 'led/ledOff.html', context)
