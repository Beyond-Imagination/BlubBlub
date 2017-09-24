from threading import Thread
from crawling import Crawling
from fcmRequest import FCMRequest
from data import Data
import time, math


class DataThread(Thread):

    def __init__(self):
        Thread.__init__(self)
        self.crawling = Crawling()
        self.fcm = FCMRequest()
        self.lastRequestTime_Feeding = 0
        self.lastRequestTime_Temperature = 0
        self.lastRequestTime_Illuminance = 0
        self.lastRequestTime_Turbidity = 0
        self.feedingCycle = 720
        self.maxTemperature = 30
        self.minTemperature = 20
        self.minIlluminance = 20

    def run(self):
        while True:
            self.crawling.getData()

            t = time.localtime()
            current = t.tm_hour + t.tm_min
            t = time.time()

            if abs(Data.time - current) > self.feedingCycle and (t - self.lastRequestTime_Feeding) > 3600:
                self.fcm.sendStateMessage(0)
                self.lastRequestTime_Feeding = t
            elif Data.temperature > self.maxTemperature and (t - self.lastRequestTime_Temperature) > 3600:
                self.fcm.sendStateMessage(1)
                self.lastRequestTime_Temperature = t
            elif Data.temperature < self.minTemperature and (t - self.lastRequestTime_Temperature) > 3600:
                self.fcm.sendStateMessage(2)
                self.lastRequestTime_Temperature = t
            elif Data.illuminance < self.minIlluminance and (t - self.lastRequestTime_Illuminance) > 3600:
                self.fcm.sendStateMessage(3)
                self.lastRequestTime_Illuminance = t
            elif Data.turbidity == 2 and (t - self.lastRequestTime_Turbidity) > 3600:
                self.fcm.sendStateMessage(4)
                self.lastRequestTime_Turbidity = t

            time.sleep(10)

    def setFeedingCycle(self, cycle):
        print("before changing feeding cycle", self.feedingCycle)
        self.feedingCycle = cycle * 60
        print("after changing feeding cycle", self.feedingCycle)

    def setMaxTemperature(self, max):
        print("before changing max temperature", self.maxTemperature)
        self.maxTemperature = max
        print("after changing max temperature", self.maxTemperature)

    def setMinTemperature(self, min):
        print("before changing min temperature", self.maxTemperature)
        self.minTemperature = min
        print("after changing min temperature", self.maxTemperature)

    def setMinIlluminance(self, min):
        print("before changing min illuminance", self.minIlluminance)
        self.minIlluminance = min
        print("after changing min illuminance", self.minIlluminance)