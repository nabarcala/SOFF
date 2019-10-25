from bluetooth import *
import base64

import subprocess32

# insert at 1, 0 is the script path (or '' in REPL)
sys.path.insert(1, '/home/nb-jetson/SOFF/Fruit-Classification/Image-Classification')
import camera

def scan():
    #camera.show_camera()
    #need to add this function to label_image
    subprocess32.call(['./start.sh'])
    #s = label_image.runmain()
    file1 = open("Fruit-Classification/Image-Classification/fruit.txt","r")
    s = file1.read()
    file1.close()
    return(s)

def getimage():
    image = "Fruit-Classification/Image-Classification/fruit_img.jpg"
    with open(image,"rb") as image_file:
        image1 = base64.b64encode(image_file.read())
        return (image1)

def cut1():
    print("Cutting Fruit.....")
    
#start Bluetooth stuff
server_sock = BluetoothSocket( RFCOMM )
server_sock.bind(("", PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]

uuid = "00001101-0000-1000-8000-00805F9B34FB"

advertise_service( server_sock, "BTS",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
                    )
                   
print("Waiting for connection on RFCOMM channel %d" % port)

client_sock, client_info = server_sock.accept()
print("Accepted connection from ", client_info)

#try:
while True:
        data = client_sock.recv(1024)
	print("received [%s]" % data.decode("utf-8"))
        #if app sends "stop" == 0: break
        if data.decode("utf-8") == "stop":
            print("Stopping: Breaking conection.....")
            break

        #if app sends "scan"
        elif data.decode("utf-8") == "scan":
            #gets the name of the fruit
           print("Scanning fruit.....") 
           result = scan()
           image = getimage()

           #sends the fruit Classification
           print("Sending fruit result.....")
	   client_sock.send("Image")
           client_sock.send(result)
	   client_sock.send("end")

           #sends the image
           print("Sending image to App.....")
           #client_sock.send(image)

	   # Test later
	   #client_sock.put("Fruit-Classification/Image-Classification/fruit_img.jpg")
	   #subprocess32.call(["ussp-push", "client_info[0]", "Fruit-Classification/Image-Classification/fruit_img.jpg", "fruit_img.jpg"])

	   print("Sent image to App")
        
        #if app sends "cut1"
        elif data.decode == "cut1":
            cut1()
             
#except IOError:
    #pass

print("Disconnected")

client_sock.close()
server_sock.close()
print("All Closed")
