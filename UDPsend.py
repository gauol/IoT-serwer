import random
import socket
import time

UDP_IP = "127.0.0.1"
UDP_PORT = 90

print("UDP target IP:", UDP_IP)
print("UDP target port:", UDP_PORT)

sock = socket.socket(socket.AF_INET,  # Internet
                     socket.SOCK_DGRAM)  # UDP
zmiennapamietajaca = 1
while 1:
    temp1 = round(random.uniform(-15, 35), 2)
    temp2 = round(random.uniform(-15, 35), 2)
    wiadomosc = "<SEN=LAZIENKA:TEMP1={0}:TEMP2={1}>".format(temp1, temp2)
    print("SEN=LAZIENKA:TEMP1={0}:TEMP2={1}".format(temp1, temp2))
    sock.sendto(wiadomosc, (UDP_IP, UDP_PORT))
    zmiennapamietajaca = zmiennapamietajaca+1
    time.sleep(3)