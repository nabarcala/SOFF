!/bin/bash

# Stop bluetooth first
sudo /etc/init.d/bluetooth stop

# Status check
#/etc/init.d/bluetooth status

# Run bluetooth in compatibility mode (don't forget ampersand, otherwise prompt won't turn up)
sudo /usr/lib/bluetooth/bluetoothd --compat&

# start bluetooth again
sudo /etc/init.d/bluetooth start

# again try sdpbrowse
# sdptool browse local

# check
#systemctl status bluetooth.service

# and change the permissions
sudo chmod 777 /var/run/sdp

cd SOFF/

