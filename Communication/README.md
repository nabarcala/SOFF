
## Connecting to the App via Bluetooth
As of right now, we are testing on connecting the Jetson Nano with an Android app via Bluetooth in order to send data back and forth. The file ```test.py``` is used to connect the two devices. To run this file, use Python 2.
```
$ python test.py
```
If there are permission issues with ```/usr/lib/python2.7/dist-packages/bluetooth/bluez.py```, run the file with ```sudo```.

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

## Interesting Links
Several links are included below that will be helpful as we continue to develop this project.

Bluetooth:
* https://www.raspberrypi.org/forums/viewtopic.php?t=133263
* https://learn.adafruit.com/install-bluez-on-the-raspberry-pi/installation
* https://circuitdigest.com/microcontroller-projects/controlling-raspberry-pi-gpio-using-android-app-over-bluetooth
