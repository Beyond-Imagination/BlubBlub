import urllib.request
from bs4 import BeautifulSoup
from data import Data


class Crawling():

    def __init__(self):
        self.url = "http://163.152.219.170:8000/sensor/"

    def getData(self):
        """크롤링을 통해 어항 상태값을 얻어냄"""
        res = urllib.request.urlopen(self.url)
        soup = BeautifulSoup(res, "html.parser")

        list = soup.select("p")
        print(list)
        Data.temperature = float(list[0].string)
        Data.illuminance = int(list[1].string)
        Data.turbidity = float(list[2].string)
        Data.time = int(list[3].string)
