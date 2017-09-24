import socket
import json
from db import *
from botengine import make_reply
from fcmRequest import FCMRequest
from dataThread import DataThread

HOST = ''
PORT = 8002
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))

fcm = FCMRequest()
fcm.setTokenList(fetchTokenTable())

dt = DataThread()
dt.start()


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
            if dict['secret'] == 'Beyond_Imagination':
                result = insertToTokenTable(str(dict['token']))
                if result == 1:
                    fcm.addToken(dict['token'])
        elif dict['type'] == 'message':
            print("사용자", dict['message'])
            reply = make_reply(dict['message'])
            print("챗봇", reply)
            fcm.sendMessageToSingleDevice(reply, dict['token'])
        elif dict['type'] == 'setting':
            dt.setFeedingCycle(dict['feedcycle'])
            dt.setMaxTemperature(dict['maxtemp'])
            dt.setMinTemperature(dict['mintemp'])
            dt.setMinIlluminance(dict['minillum'])


        #print(dict['type'], dict['secret'], dict['token'])

    conn.close()