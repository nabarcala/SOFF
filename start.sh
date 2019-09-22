#!/bin/bash

cd soff/ 

python camera.py

python3 Image-Classification/label_image.py --graph=Image-Classification/output_graph.pb --labels=Image-Classification/output_labels.txt --input_layer=Placeholder --output_layer=final_result --image=fruit_img.jpg
