# Classifying Fruit
This project's code was created and tested for the NVIDIA Jetson Nano. 
The model that is used to classify the image of the fruit was retrained using the official Tensorflow tutorial for retraining. It can be found [at their official Tensorflow website](https://github.com/nabarcala/SOFF.git).

## Capture an Image
The first step is to have an image ready for the model to classify. This project is using a Raspberry Pi camera connected to the Jetson Nano. Ensure that your camera is corrected connected to your device. Assuming your current directory is ```Fruit-Classification```, run the following code:
```
python camera.py
```
This will take a single image, called ```fruit_img.jpg```, that will be saved in your current directory.

## Classify your Image
The available classifications for this model are: 

* Apple
* Beetroot
* Dates
* Mango
* Orange
* Pomegranate

A database of these images are located in ```Fruit-Classification/fruits-dataset```.

To run the model given, make sure that you  are in the ```Fruit-Classification``` directory. Open the terminal and run the following code:

```
python3 Image-Classification/label_image.py --graph=Image-Classification/output_graph.pb --labels=Image-Classification/output_labels.txt --input_layer=Placeholder --output_layer=final_result --image=fruit_img.jpg
```

After the model has run, you should see output like this:
```
apple 0.9856818
orange 0.005912187
dates 0.002886865
pomegranate 0.0026501939
mango 0.0014653547
```
This indicates that the image has been identified as an apple.

## Acknowledgments
* [abdullahsadiq on Github](https://github.com/abdullahsadiq/jetson-fruits-classification), who has code on the classification of fruits using Tensorflow on Jetson Nano. This helped build the foundation of this project.
