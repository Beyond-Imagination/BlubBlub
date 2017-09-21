from threading import Thread
from crawling import Crawling
from fcmRequest import FCMRequest
from data import Data
import time

class DataThread(Thread):

    def __init__(self):
        Thread.__init__(self)
        self.crawling = Crawling()
        self.fcm = FCMRequest()

    def run(self):
        while True:
            self.crawling.getData()
    
            if Data:
                FCMRequest.sendStateMessage(0)
            elif Data.temperature > 30:
                FCMRequest.sendStateMessage(1)
            elif Data.temperature < 20:
                FCMRequest.sendStateMessage(2)
            elif Data.illuminance < 20:
                FCMRequest.sendStateMessage(3)
            elif Data.turbidity == 2:
                FCMRequest.sendStateMessage(4)

            time.sleep(10)

