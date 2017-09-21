import urllib.request
from bs4 import BeautifulSoup
from data import Data


class Crawling():
    url = "http://163.152.219.170:8000/sensor/"

    def __init__(self):
        self.res = urllib.request.urlopen(self.url)

    def getData(self):
        soup = BeautifulSoup(self.res, "html.parser")

        list = soup.select("p")
        Data.temperature = list[0].string
        Data.illuminance = list[1].string
        Data.turbidity = list[2].string

