import RPi.GPIO as GPIO
from threading import Thread
import time

class FeedingMotor(Thread):

    def __init__(self):
        Thread.__init__(self)
        GPIO.setmode(GPIO.BCM)
        self.pins = [12,16,20,21]
        for p in self.pins:
            GPIO.setup(p, GPIO.OUT)
            GPIO.output(p, GPIO.LOW)
        self.FULL_STEP = 4
        self.signal_full = [
            [GPIO.HIGH, GPIO.LOW, GPIO.LOW, GPIO.LOW],
            [GPIO.LOW, GPIO.HIGH, GPIO.LOW, GPIO.LOW],
            [GPIO.LOW, GPIO.LOW, GPIO.HIGH, GPIO.LOW],
            [GPIO.LOW, GPIO.LOW, GPIO.LOW, GPIO.HIGH],
        ]
        #self.steps = self.FULL_STEP
        self.steps = 4
        self.signal = self.signal_full

    def __run__(self):
        try:
            for i in range(512):
                for step in range(self.steps):
                    for k in range(4):
                        GPIO.output(self.pins[k], self.signal[step][k])
                    time.sleep(0.01)
        except KeyboardInterrupt:
            print("\nInterrupted")


if __name__ == "__main__":
    m = FeedingMotor()
    m.start()
