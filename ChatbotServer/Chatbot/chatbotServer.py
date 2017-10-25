import socket
from threading import Thread
import json
from db import TokenDB
from botengine import Chatbot
from fcmRequest import FCMRequest
from dataThread import DataThread
from weather import WeatherRequest


class ChatbotServer(Thread):
    """프로그램의 메인 쓰레드
       안드로이드와의 통신을 담당하며 소켓통신을 이용함
       """

    def __init__(self):
        Thread.__init__(self)
        self.HOST = ''
        self.PORT = 8002


        self.fcm = FCMRequest()

        self.chatbot = Chatbot()

        self.dt = DataThread()
        self.dt.start()

        self.weather = WeatherRequest()


    def run(self):
        self.db = TokenDB()
        self.fcm.setTokenList(self.db.fetchTokenTable())

        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.bind((self.HOST, self.PORT))

        while True:
            s.listen(1)

            conn, addr = s.accept()
            print('Connected by', addr)

            while True:
                data = conn.recv(2048)
                if not data:
                    break

                data = data.decode()
                dict = json.loads(data)

                print(dict)

                if dict['type'] == 'token':
                    self.receiveToken(dict)
                elif dict['type'] == 'message':
                    self.receiveMessage(dict)
                elif dict['type'] == 'setting':
                    self.receiveSetting(dict)
                elif dict['type'] == 'weather':
                    self.requestweather(dict)


            conn.close()

    def receiveToken(self, dict):
        """dict의 secret 값이 약속한 string이 맞는지 확인 맞다면 db에 저장한다."""
        if dict['secret'] == 'Beyond_Imagination':
            result = self.db.insertToTokenTable(str(dict['token']))
            if result == 1:
                self.fcm.addToken(dict['token'])

    def receiveMessage(self, dict):
        """사용자의 메시지에 맞는 응답을 생성후 FCM을 통해 해당 안드로이기기로 전송"""
        print("사용자", dict['message'])
        reply = self.chatbot.make_reply(dict['message'])
        print("챗봇", reply)
        self.fcm.sendTalkingMessage(reply, dict['token'])

    def receiveSetting(self, dict):
        """사용자에게 받은 세팅값을 저장"""
        self.dt.setFeedingCycle(dict['feedcycle'])
        self.dt.setMaxTemperature(dict['maxtemp'])
        self.dt.setMinTemperature(dict['mintemp'])
        self.dt.setMaxIlluminance(dict['maxillum'])
        self.dt.setMinIlluminance(dict['minillum'])

    def requestweather(self, dict):
        weather = self.weather.requestweather_seoul()
        self.fcm.sendweathermessage(weather, dict['token'])


chatbotServer = ChatbotServer()
chatbotServer.start()

while True:
    """예상치 못한 쓰레드 종료시 재실행"""
    if not chatbotServer.is_alive():
        chatbotServer = ChatbotServer()
        chatbotServer.start()
    if not chatbotServer.dt.is_alive():
        chatbotServer.dt = DataThread()
        chatbotServer.dt.start()