# MIT License
# Copyright (c) 2019 JetsonHacks
# See license
# Using a CSI camera (such as the Raspberry Pi Version 2) connected to a 
# NVIDIA Jetson Nano Developer Kit using OpenCV
# Drivers for the camera and OpenCV are included in the base image

import cv2
import time

# gstreamer_pipeline returns a GStreamer pipeline for capturing from the CSI camera
# Defaults to 1280x720 @ 60fps 
# Flip the image by setting the flip_method (most common values: 0 and 2)
# display_width and display_height determine the size of the window on the screen

def gstreamer_pipeline (capture_width=1280, capture_height=720, display_width=500, display_height=500, framerate=60, flip_method=0) :   
    return ('nvarguscamerasrc ! ' 
    'video/x-raw(memory:NVMM), '
    'width=(int)%d, height=(int)%d, '
    'format=(string)NV12, framerate=(fraction)%d/1 ! '
    'nvvidconv flip-method=%d ! '
    'video/x-raw, width=(int)%d, height=(int)%d, format=(string)BGRx ! '
    'videoconvert ! '
    'video/x-raw, format=(string)BGR ! appsink'  % (capture_width,capture_height,framerate,flip_method,display_width,display_height))

def show_camera():
    # To flip the image, modify the flip_method parameter (0 and 2 are the most common)
    print gstreamer_pipeline(flip_method=0)
    cap = cv2.VideoCapture(gstreamer_pipeline(flip_method=2), cv2.CAP_GSTREAMER)

    if cap.isOpened():
	# allow time for flash
        time.sleep(5)
        ret_val, img = cap.read();
        cv2.imwrite('fruit_img.jpg', img)
 
	# resize image
	#resized = cv2.resize(img, (500, 500))
    	#cv2.imwrite('fruit_img.jpg', img)

        cap.release()
        cv2.destroyAllWindows()
    else:
        print 'Unable to open camera'


if __name__ == '__main__':
    show_camera()
