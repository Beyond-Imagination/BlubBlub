import socket
import json

HOST = ''
PORT = 8002
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))

while True:
    s.listen(1)

    conn, addr = s.accept()
    print('Connected by', addr)
    while True:
        data = conn.recv(2048)
        if not data:
            break

        data = data.decode()

        print(data,"---------------")

        dict = json.loads(data)
        print(dict)

    conn.close()