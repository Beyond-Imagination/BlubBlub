import RPi.GPIO as GPIO
import time
import threading


GPIO.setmode(GPIO.BCM)
pins = [12,16,20,21] #IN1, IN2, IN3, IN4
for p in pins:
    GPIO.setup(p, GPIO.OUT)
    GPIO.output(p, GPIO.LOW)

FULL_STEP = 4

signal_full = [
          [GPIO.HIGH, GPIO.LOW, GPIO.LOW, GPIO.LOW],
          [GPIO.LOW, GPIO.HIGH, GPIO.LOW, GPIO.LOW],
          [GPIO.LOW, GPIO.LOW, GPIO.HIGH, GPIO.LOW],
          [GPIO.LOW, GPIO.LOW, GPIO.LOW, GPIO.HIGH]
          ]

#stepping mode and direction

def runMotor():
    steps = FULL_STEP
    signal = signal_full
    try:
        # 1 cycle = 4 step for FULL
        # 1 cycle = 8 step for HALF
        # 1 rev = 512 cycle
        for i in range(512):
            for step in range(steps):
                for k in range(4):
                    GPIO.output(pins[k], signal[step][k])
                time.sleep(0.01)

    except KeyboardInterrupt:
        print("\nInterrupted!")

def run():
    m = threading.Thread(target = runMotor(), args = ())
