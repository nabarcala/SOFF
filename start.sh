#!/bin/bash

cd Fruit-Classification/Image-Classification 

# Take a picture with the Raspberry Pi Camera V2
python camera.py

# Retrained Model
#python3 label_image.py --graph=output_graph.pb --labels=output_labels.txt --input_layer=Placeholder --output_layer=final_result --image=fruit_img.jpg

# Using NVIDIA's Inference: Classifying Images with ImageNet
# --network flag is optional
#python3 imagenet-console.py --network=googlenet fruit_img.jpg output_0.jpg  

# Using retrained 
python imagenet-console.py --model=fruit/resnet18.onnx --input_blob=input_0 --output_blob=output_0 --labels=fruit/labels.txt fruit_img.jpg fruit_img_output.png


