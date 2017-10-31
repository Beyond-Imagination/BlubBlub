from threading import Thread
from crawling import Crawling
from fcmRequest import FCMRequest
from data import Data
from db import SettingDB
import time


class DataThread(Thread):
    """라즈베리파이에서 어항상태를 얻어내고 상태변화를 관리하는 쓰레드"""

    def __init__(self):
        Thread.__init__(self)
        self.settingdb = SettingDB()
        setting = self.settingdb.fetchSettingTable()
        setting = setting[0]
        self.crawling = Crawling()
        self.fcm = FCMRequest()
        self.feedingCycle = setting[0]
        self.maxTemperature = setting[1]
        self.minTemperature = setting[2]
        self.maxIlluminance = setting[3]
        self.minIlluminance = setting[4]
        self.lastRequestTime_Temperature = setting[5]
        self.lastRequestTime_Illuminance = setting[6]
        self.lastRequestTime_Turbidity = setting[7]
        self.lastRequestTime_Feeding = setting[8]

    def run(self):
        """크롤링을 통해 상태정보를 얻어낸 후
           각각의 값들을 사용자가 보낸 설정값과 비교
           설정값을 벗어낫을시 fcm을 통해 메시지 전송"""

        while True:
            self.crawling.getData()

            t = time.localtime()
            print(t)
            current = t.tm_hour*60 + t.tm_min
            t = time.time()

            print("현재시간 : " +str(current))
            print(t)
            print(abs(Data.time - current),self.feedingCycle,abs(Data.time - current) > self.feedingCycle)
            if abs(Data.time - current) > self.feedingCycle and (t - self.lastRequestTime_Feeding) > 3600:
                self.fcm.sendStateMessage(0)
                self.lastRequestTime_Feeding = t
                self.settingdb.updateSettingTable("feeding_lt",self.lastRequestTime_Feeding)

            if Data.temperature > self.maxTemperature and (t - self.lastRequestTime_Temperature) > 3600:
                self.fcm.sendStateMessage(1)
                self.lastRequestTime_Temperature = t
                self.settingdb.updateSettingTable("temp_lt", self.lastRequestTime_Temperature)
            elif Data.temperature < self.minTemperature and (t - self.lastRequestTime_Temperature) > 3600:
                self.fcm.sendStateMessage(2)
                self.lastRequestTime_Temperature = t
                self.settingdb.updateSettingTable("temp_lt", self.lastRequestTime_Temperature)

            if Data.illuminance < self.minIlluminance and (t - self.lastRequestTime_Illuminance) > 3600:
                self.fcm.sendStateMessage(3)
                self.lastRequestTime_Illuminance = t
                self.settingdb.updateSettingTable("illum_lt", self.lastRequestTime_Illuminance)
            elif Data.illuminance > self.maxIlluminance and (t - self.lastRequestTime_Illuminance) > 3600:
                self.fcm.sendStateMessage(4)
                self.lastRequestTime_Illuminance = t
                self.settingdb.updateSettingTable("illum_lt", self.lastRequestTime_Illuminance)

            if Data.turbidity == 2 and (t - self.lastRequestTime_Turbidity) > 3600:
                self.fcm.sendStateMessage(5)
                self.lastRequestTime_Turbidity = t
                self.settingdb.updateSettingTable("turb_lt", self.lastRequestTime_Turbidity)

            time.sleep(5)


#사용자가 보낸 세팅값 설정 함수
    def setFeedingCycle(self, cycle):
        print("before changing feeding cycle", self.feedingCycle)
        self.feedingCycle = cycle * 60
        self.settingdb.updateSettingTable("feeding",self.feedingCycle)
        print("after changing feeding cycle", self.feedingCycle)

    def setMaxTemperature(self, max):
        print("before changing max temperature", self.maxTemperature)
        self.maxTemperature = max
        self.settingdb.updateSettingTable("maxtemp", self.maxTemperature)
        print("after changing max temperature", self.maxTemperature)

    def setMinTemperature(self, min):
        print("before changing min temperature", self.minTemperature)
        self.minTemperature = min
        self.settingdb.updateSettingTable("mintemp", self.minTemperature)
        print("after changing min temperature", self.minTemperature)

    def setMinIlluminance(self, min):
        print("before changing min illuminance", self.minIlluminance)
        self.minIlluminance = min
        self.settingdb.updateSettingTable("minillum", self.minIlluminance)
        print("after changing min illuminance", self.minIlluminance)

    def setMaxIlluminance(self, max):
        print("before changing max illuminance", self.maxIlluminance)
        self.maxIlluminance = max
        self.settingdb.updateSettingTable("maxillum", self.maxIlluminance)
        print("after changing miax illuminance", self.maxIlluminance)