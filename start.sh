#!/bin/bash

cd Fruit-Classification/Image-Classification 

python camera.py

python3 label_image.py --graph=output_graph.pb --labels=output_labels.txt --input_layer=Placeholder --output_layer=final_result --image=fruit_img.jpg
