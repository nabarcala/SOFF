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



#change coutn to increase number of data points

count = 50



#stores color data

violet = []

blue = []

green = []

yellow = []

orange = []

red = []



#stores percentage of colord ata

percent_violet = []

percent_blue = []

percent_green = []

percent_yellow = []

percent_orange = []

percent_red= []



#stores averages

avg_violet = 0.000

avg_blue = 0.000

avg_green = 0.000

avg_yellow = 0.000

avg_orange = 0.000

avg_red = 0.000



#blinking lights to indicate the ripeness detector is about to start

sensor.driver_led = True

time.sleep(.5)

sensor.driver_led = False

time.sleep(.5)

sensor.driver_led = True

time.sleep(.5)

sensor.driver_led = False

time.sleep(.5)

sensor.driver_led = True

time.sleep(.5)

sensor.driver_led = False

time.sleep(.5)



#while True:

for i in range(count):

    sensor.driver_led = True

    # Wait for data to be ready

    while not sensor.data_ready:

        time.sleep(0)

        

    #plot plot the data

    v =sensor.violet

    b = sensor.blue

    g = sensor.green

    y = sensor.yellow

    o = sensor.orange

    r = sensor.red

    #print("\n")

    #print("V: " + str(sensor.violet))

    #print("B: " + str(sensor.blue))

    #print("G: " + str(sensor.green))

    #print("Y: " + str(sensor.yellow))

    #print("O: " + str(sensor.orange))

    #print("R: " + str(sensor.red))

    

    

    violet.append(v)

    blue.append(b)

    green.append(g)

    yellow.append(y)

    orange.append(o)

    red.append(r)

    

    total = violet[i]+blue[i]+green[i]+yellow[i]+orange[i]+red[i]

    

    percent_violet.append(violet[i]/total)

    percent_blue.append(blue[i]/total)

    percent_green.append(green[i]/total)

    percent_yellow.append(yellow[i]/total)

    percent_orange.append(orange[i]/total)

    percent_red.append(red[i]/total)



    #time.sleep(1)

sensor.driver_led = False

for i in range(count):

    avg_violet = avg_violet + percent_violet[i]

    avg_blue = avg_blue + percent_blue[i]

    avg_green = avg_green + percent_green[i]

    avg_yellow = avg_yellow + percent_yellow[i]

    avg_orange = avg_orange + percent_orange[i]

    avg_red = avg_red + percent_red[i]

    

avg_violet = avg_violet/count

avg_blue = avg_blue/count

avg_green = avg_green/count

avg_yellow = avg_yellow/count

avg_orange = avg_orange/count

avg_red = avg_red/count



print("Violet")

print("\n")

print(avg_violet)

print("\n")

print("Blue")

print("\n")

print(avg_blue)

print("\n")

print("Green")

print("\n")

print(avg_green)

print("\n")

print("Yellow")

print("\n")

print(avg_yellow)

print("\n")

print("Orange")

print("\n")

print(avg_orange)

print("\n")

print("Red")

print("\n")

print(avg_red)



#baseline for avocado testing

file2 = open("Ripe.txt","w+")

if avg_green >= .30:
    file2.write("Banana is Still Green")

else:
    file2.write("Banana is ready to eat")

file2.close()
