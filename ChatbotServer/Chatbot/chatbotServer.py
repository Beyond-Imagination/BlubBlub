import socket
from threading import Thread
import json
from db import TokenDB
from botengine import Chatbot
from fcmRequest import FCMRequest
from dataThread import DataThread


class ChatbotServer(Thread):

    def __init__(self):
        Thread.__init__(self)
        self.HOST = ''
        self.PORT = 8002

        self.db = TokenDB()

        self.fcm = FCMRequest()
        self.fcm.setTokenList(self.db.fetchTokenTable())

        self.chatbot = Chatbot()

        self.dt = DataThread()
        self.dt.start()


    def run(self):
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

            conn.close()

    def receiveToken(self, dict):
        if dict['type'] == 'token':
            if dict['secret'] == 'Beyond_Imagination':
                result = self.db.insertToTokenTable(str(dict['token']))
                if result == 1:
                    self.fcm.addToken(dict['token'])

    def receiveMessage(self, dict):
        print("사용자", dict['message'])
        reply = self.chatbot.make_reply(dict['message'])
        print("챗봇", reply)
        self.fcm.sendTalkingMessage(reply, dict['token'])

    def receiveSetting(self, dict):
        self.dt.setFeedingCycle(dict['feedcycle'])
        self.dt.setMaxTemperature(dict['maxtemp'])
        self.dt.setMinTemperature(dict['mintemp'])
        self.dt.setMinIlluminance(dict['minillum'])


chatbotServer = ChatbotServer()
chatbotServer.start()

while True:
    if not chatbotServer.is_alive():
        chatbotServer = ChatbotServer()
        chatbotServer.start()
    if not chatbotServer.dt.is_alive():
        chatbotServer.dt = DataThread()
        chatbotServer.dt.start()