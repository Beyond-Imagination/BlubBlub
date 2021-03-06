import serial
import re
import time
from threading import Thread
from .data import Data

port = "/dev/ttyUSB0"
serialFromArduino = serial.Serial(port,9600)
serialFromArduino.flushInput()

class Sensors(Thread):

	def __init__(self):
		Thread.__init__(self)

	def sensing(self):
		input = serialFromArduino.readline()
		input = input.decode("utf-8").strip()
		list_input = input.split('=')

		if list_input[0]=='CDS':
			Data.illuminance = list_input[-1]
		elif list_input[0]=='TUR':
			Data.turbidity = list_input[-1]
		elif list_input[0]=='TEMP':
			Data.temperature = list_input[-1]
			serialFromArduino.flushInput()
	def run(self):
		while True:
			print("Temperature :",Data.temperature,end="  ")
			print("Illuminance :",Data.illuminance,end="  ")
			print("Turbidity :",Data.turbidity,end="  ")
			print("FeedingTime :",Data.time)
			print("-"*70)
			self.sensing()
			time.sleep(1)
