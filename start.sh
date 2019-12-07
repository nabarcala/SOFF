#!/bin/bash

cd Fruit-Classification/Image-Classification 

# Take a picture with the Raspberry Pi Camera V2
python camera.py

# Using NVIDIA's Inference: Classifying Images with ImageNet
# --network flag is optional
#python3 imagenet-console.py --network=googlenet fruit_img.jpg output_0.jpg  

# Retrained Model:

#python imagenet-console.py --model=fruit/resnet18.onnx --input_blob=input_0 --output_blob=output_0 --labels=fruit/labels.txt fruit_img.jpg fruit_img_output.png

# Apple, Oranges
python3 imagenet-console.py --model=images/resnet182.onnx --input_blob=input_0 --output_blob=output_0 --labels=images/temp.txt fruit_img.jpg fruit_img_output.png
