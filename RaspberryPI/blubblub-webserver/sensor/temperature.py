import os
import time
#import RPi.GPIO as GPIO
from threading import Thread
from .data import Data

#####temperature#####
os.system('modprobe w1-gpio')
os.system('modprobe w1-therm')

class TemperatureSensor(Thread):

    def __init__(self):
        Thread.__init__(self)
        self.temp_sensor = '/sys/bus/w1/devices/28-8000001fa18a/w1_slave'

    def temp_raw(self):
        f = open(self.temp_sensor,'r')
        lines = f.readlines()
        f.close()
        return lines

    def read_temp(self):
        lines = self.temp_raw()
        while lines[0].strip()[-3:] != 'YES':
            time.sleep(0.2)
            lines = self.temp_raw()
        temp_output = lines[1].find('t=')
        if temp_output != -1:
            temp_string = lines[1].strip()[temp_output+2:]
            temp_c = float(temp_string)/1000.0
            Data.temperature = temp_c
            print(temp_c,Data.temperature)
            return temp_c

    def run(self):
        while True:
            self.read_temp()
            time.sleep(60)
