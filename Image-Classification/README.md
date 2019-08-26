# Getting Started
These instructions will get a copy of the project up and running on your local machine.

## Prerequisites
Python 3.6.* is needed as that is the version of Python 3 that does not encounter errors with Tensorflow (as of the day of this upload). The specific version used here is Python 3.6.8, which can be found at:
https://www.python.org/downloads/release/python-368/
Tensorflow and OpenCV can be installed using pip:
```
pip3 install --upgrade tensorflow
pip3 install --upgrade opencv-python
```
A large database of images is necessary. A large set of images is available at https://www.kaggle.com/moltean/fruits.

## Retraining
Seperate the test and training images. Create two folders called training_images and testing_images. Ensure they are created inside of the working repository.
In the training_images folder, create directories for each classification you'd like and make sure to download as many images for each type. E.g. Image-Classification\training_images\apples and Image-Classification\training_images\mangos
Remember to save some test images in the test_images directory to test the classifications.



## Acknowledgments
* The Tensorflow github located at https://github.com/tensorflow/tensorflow/
* Several tutorials on youtube that are dedicated to giving useful guides and walkthroughs on the subject of image classification and detection.
* Fruit-360 by Mihai Oltean on Kaggle for the dataset of fruits. 
