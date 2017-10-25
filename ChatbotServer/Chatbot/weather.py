import urllib.request
import urllib.parse
from bs4 import BeautifulSoup

class WeatherRequest():
    def __init__(self):
        self.api = "http://www.kma.go.kr/weather/forecast/mid-term-rss3.jsp"
        values={
            'stnId': '109'
        }
        params = urllib.parse.urlencode(values)
        self.url = self.api + "?" + params

    def requestweather_seoul(self):
        data = urllib.request.urlopen(self.url).read()
        text = data.decode("utf-8")
        soup = BeautifulSoup(text, "html.parser")
        temp = str(soup.find("wf").string)
        print(temp)
        list = temp.split("<br />")
        wf = "오늘의 날씨 입니다. " + list[1] + " " + list[2]
        print(wf)
        return wf