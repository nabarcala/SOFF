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
```
./start.sh
```

## Interesting Links
Several links are included below that will be helpful as we continue to develop this project.
* Bluetooth:
https://www.raspberrypi.org/forums/viewtopic.php?t=133263

* GPIO:
https://www.jetsonhacks.com/2019/06/07/jetson-nano-gpio/

## Acknowledgments
* [abdullahsadiq on Github](https://github.com/abdullahsadiq/jetson-fruits-classification), who has code on the classification of fruits using Tensorflow on Jetson Nano. This helped build the foundation of this project.
* [dusty-nv on Github with the Hello AI World module](https://github.com/dusty-nv/jetson-inference) with steps to help familiarize ourselves with machine learning and inference with TensorRT. As stated in the README: "The inference portion of Hello AI World - which includes coding your own image classification application for C++ or Python, object detection, and live camera demos - can be run on your Jetson in roughly two hours or less, while transfer learning is best left to leave running overnight."
