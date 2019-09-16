import pygame
from pygame.locals import *
# import image
import sys
import cv2

# activate camera: 0 for native, 1 for external webcame (The Logitech webcam)
camera = cv2.VideoCapture(1)

# Take 10 pictures
for i in range(2):
    val, image = camera.read()
    # Save to folder t
    cv2.imwrite('test_images/fruit' + str(i) + '.png', image)
del(camera)