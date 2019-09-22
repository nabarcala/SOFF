# SOFF
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
For this project, the classification is being ran on a Jetson Nano. As long as a camera is attached and ready, then the following code can be ran to take a single picture and classify the picture using a retrained network. Keep in mind the available model is based on images of fruit.
```
./start.sh
```
