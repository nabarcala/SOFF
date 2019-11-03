# SOFF - Slices Only Fresh Fruit
Senior engineering design team project for the SOFF (Slices Only Fresh Fruit) device

## Project Summary:
Many elderly and disabled people have trouble slicing their fruit into reasonable sizes. This can lead them into not eating the fruit on time and can result in wasted food. The purpose of this project is to create a device that can detect the ripeness of the fruit, alert the user of the fruit’s state, then slice the fruit for them.

## Project Members
Natacha Barcala,
Floriano Villeda,
Jorge Mayora

## Advisor 
Hari Kalva

## Running the Classification
For this project, the classification is being ran on a Jetson Nano. As long as a camera is attached and ready, then the following code can be ran to take a single picture and classify the picture using a retrained network. Keep in mind the available model is based on images of fruit. More details on this available at [Fruit-Classification](Fruit-Classification/README.md).

Linux: set the script with executable permission by running chmod command. 
```
$ chmod +x start.sh
$ ./start.sh
```

Note: The ```start.sh``` script is called from the driver that is mentioned below.

## Connecting to the App via Bluetooth
Details at [Communication](Communication/README.md).

The ```driver.py``` is the driver for this project. This file is designed to begin all the features of the SOFF device.

We can connect the Jetson Nano to an Android app via Bluetooth. The Jetson recieves commands in the form of strings in order to start the classification, take a picture of a fruit, check the ripeness, and/or slice a fruit.

The file ```driver.py``` is used to connect the two devices. To run this file, use Python 2.
```
$ sudo python driver.py
```
(If there are permission issues with ```/usr/lib/python2.7/dist-packages/bluetooth/bluez.py```, run the file with ```sudo```.)

We are running this script with sudo to account for permissions that the other features need. E.g. the color sensor needs sudo permissions in order to get the color values to determine ripeness.

## Possible Errors
Because of deprecated modules on BlueZ5, this error similar to the one showed below might appear:
```
Traceback (most recent call last):
  File "test.py", line 7, in <module>
    advertise_service(server_sock, "SampleServer",service_classes=[SERIAL_PORT_CLASS], profiles=[SERIAL_PORT_PROFILE])
  File "/usr/lib/python2.7/dist-packages/bluetooth/bluez.py", line 176, in advertise_service 
    raise BluetoothError (str (e)) 
  bluetooth.btcommon.BluetoothError: (2, 'No such file or directory')
```
After some research, this error was because of the sdptool. Running the Bluetooth daemon in compatibility mode should fix that error. [This solution can fix this error.](https://raspberrypi.stackexchange.com/questions/41776/failed-to-connect-to-sdp-server-on-ffffff000000-no-such-file-or-directory/42262) 

However, if running ```sdptool browse local``` has the following output:
```
Failed to connect to SDP server on FF:FF:FF:00:00:00: No such file or directory
```
Here are some steps to fixing this error:

Stop bluetooth first
```
$ /etc/init.d/bluetooth stop
```
Status check
```
$ /etc/init.d/bluetooth status
```
Run bluetooth in compatibility mode (don't forget ampersand, otherwise prompt won't turn up)
```
$ sudo /usr/lib/bluetooth/bluetoothd --compat&
```
start bluetooth again
```
$ sudo /etc/init.d/bluetooth start
```
again try sdpbrowse
```
$ sdptool browse local
```
and change the permissions 
```
$ sudo chmod 777 /var/run/sdp
```

Things should work now. [Also, this forum has a step-by-step solution in case the error is different.](https://bbs.archlinux.org/viewtopic.php?id=204079) has some steps that corrected the error. 

Note: These change may not stick when the Jetson Nano reboots. In that case, these commands will have to be run everytime it powers up again.

More details at the [Communication's README](Communication/README.md).

# Running the fix as a script file
The file ```bt-compat.sh``` contains the commands that fix the error mentioned in the previous section. Because these commands require administrative permission, we are very interested in being able to run this script as root with no password.

[This ask has a step by step solution](https://askubuntu.com/questions/167847/how-to-run-bash-script-as-root-with-no-password). Here is the breakdown:

Make the file owned by root and group root:
```
sudo chown root.root bt-compat.sh
```
Make it executable for all and writable only by root:
```
sudo chmod 4755 bt-compat.sh
```
Add these two lines at the end of your sudoers file.
```
Cmnd_Alias        CMDS = /path/to/your/script/bt-compat.sh

<username>  ALL=NOPASSWD: CMDS
```
This should allow the script to run without asking for a password as long as it is run with a sudo in front.
```
sudo bt-compat.sh
```

## Color Sensor Section
Using a color sensor to detect ripeness in fruits that begin to change color, such as avocados and peaches.

Adafruit installation packages for linux:
https://circuitpython.readthedocs.io/projects/as726x/en/latest/

The file ```colorsensor.py``` takes color values that are valuable in determining ripeness in fruits that begin to change color as they ripen. This file is called from the ```driver.py``` when the command is recieved from the app.

## Instructions to connect Arduino nano with Jetson Nano
http://blog.rareschool.com/2019/05/five-steps-to-connect-jetson-nano-and.html
## Connecting color sensor to Jetson
https://learn.adafruit.com/adafruit-as7262-6-channel-visible-light-sensor/circuitpython-wiring-test

## Interesting Links
Several links are included below that will be helpful as we continue to develop this project.

Bluetooth:
* https://www.raspberrypi.org/forums/viewtopic.php?t=133263
* https://learn.adafruit.com/install-bluez-on-the-raspberry-pi/installation
* https://circuitdigest.com/microcontroller-projects/controlling-raspberry-pi-gpio-using-android-app-over-bluetooth

GPIO:
* https://www.jetsonhacks.com/2019/06/07/jetson-nano-gpio/

## Acknowledgments
* [abdullahsadiq on Github](https://github.com/abdullahsadiq/jetson-fruits-classification), who has code on the classification of fruits using Tensorflow on Jetson Nano. This helped build the foundation of this project.
* [dusty-nv on Github with the Hello AI World module](https://github.com/dusty-nv/jetson-inference) with steps to help familiarize ourselves with machine learning and inference with TensorRT. As stated in the README: "The inference portion of Hello AI World - which includes coding your own image classification application for C++ or Python, object detection, and live camera demos - can be run on your Jetson in roughly two hours or less, while transfer learning is best left to leave running overnight."
* [Controlling Raspberry Pi GPIO using Android App over Bluetooth](https://circuitdigest.com/microcontroller-projects/controlling-raspberry-pi-gpio-using-android-app-over-bluetooth) has some basics on connecting to devices via Bluetooth using the terminal. It also has example code to connect to a bluetooth device.
