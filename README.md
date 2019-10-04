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
For this project, the classification is being ran on a Jetson Nano. As long as a camera is attached and ready, then the following code can be ran to take a single picture and classify the picture using a retrained network. Keep in mind the available model is based on images of fruit. More details on this available [here](https://github.com/nabarcala/SOFF/blob/master/Fruit-Classification/README.md).

(Linux: set the script with executable permission by running chmod command)
```
$ chmod +x start.sh
$ ./start.sh
```

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
$ /usr/lib/bluetooth/bluetoothd --compat&
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

## Interesting Links
Several links are included below that will be helpful as we continue to develop this project.
* Bluetooth:
https://www.raspberrypi.org/forums/viewtopic.php?t=133263
https://learn.adafruit.com/install-bluez-on-the-raspberry-pi/installation

* GPIO:
https://www.jetsonhacks.com/2019/06/07/jetson-nano-gpio/

## Acknowledgments
* [abdullahsadiq on Github](https://github.com/abdullahsadiq/jetson-fruits-classification), who has code on the classification of fruits using Tensorflow on Jetson Nano. This helped build the foundation of this project.
* [dusty-nv on Github with the Hello AI World module](https://github.com/dusty-nv/jetson-inference) with steps to help familiarize ourselves with machine learning and inference with TensorRT. As stated in the README: "The inference portion of Hello AI World - which includes coding your own image classification application for C++ or Python, object detection, and live camera demos - can be run on your Jetson in roughly two hours or less, while transfer learning is best left to leave running overnight."
