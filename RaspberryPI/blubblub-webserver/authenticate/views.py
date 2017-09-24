from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from authenticate.models import TokenRegister
import sys, time


# Create your views here.
@csrf_exempt
def register(request):
    secret = request.POST['secret']
    token = request.POST['token']
    print(secret)
    print(token)
    result = 0
    if secret == "Beyond_Imagination":
        try:
            TokenRegister(token=request.POST['token']).save()
            result = 1
        except Exception as e:
            print("already registerd", e.args[0])
    context = {"result":result}
    return render(request, 'authenticate/register.html', context)
