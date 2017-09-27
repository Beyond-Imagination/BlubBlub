from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from .motor import run
from authenticate.models import TokenRegister
import sys, time


# Create your views here.

@csrf_exempt
def feeding(request):
    token = request.POST['token']
    objects = TokenRegister.objects.all()
    tokenList = [ t.token for t in objects]
    print(tokenList)
    result = 0
    if token in tokenList:
        run()
        result = 1
    t = time.localtime()
    Data.time = t.tm_hour*60 + t.tm_min
    context={'time':result}
    return render(request, 'motor/feeding.html', context)
