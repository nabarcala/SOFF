import time
import board
import busio
from adafruit_as726x import Adafruit_AS726x
 
i2c = busio.I2C(board.SCL, board.SDA)
sensor = Adafruit_AS726x(i2c)

# Initialize I2C bus and sensor.
i2c = busio.I2C(board.SCL, board.SDA)
sensor = Adafruit_AS726x(i2c)

sensor.conversion_mode = sensor.MODE_2

while True:
    # Wait for data to be ready
    while not sensor.data_ready:
        time.sleep(.1)
 
    #plot plot the data
    print("\n")
    print("V: " + str(sensor.violet))
    print("B: " + str(sensor.blue))
    print("G: " + str(sensor.green))
    print("Y: " + str(sensor.yellow))
    print("O: " + str(sensor.orange))
    print("R: " + str(sensor.red))
    
 
    time.sleep(1)