import RPi.GPIO as GPIO

GPIO.setmode(GPIO.BCM)
GPIO.setup(25, GPIO.OUT)

def TurnOn():
    GPIO.output(25,True)

def TurnOff():
    GPIO.output(25,False)
