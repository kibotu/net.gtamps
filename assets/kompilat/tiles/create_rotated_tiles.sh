#!/bin/bash

# this script automatically creates rotated versions of the bitmaps in this folder.
# TO RUN THIS SCRIPT IMAGE MAGICK MUST BE INSTALLED

rm ./0/GTATILES*
convert ./*.bmp ./0/GTATILES-%03d.jpg
rm ./90/GTATILES*
convert -rotate 90  ./0/GTATILES*  ./90/GTATILES-%03d.jpg
rm ./180/GTATILES*
convert -rotate 180 ./0/GTATILES* ./180/GTATILES-%03d.jpg
rm ./270/GTATILES*
convert -rotate 270 ./0/GTATILES* ./270/GTATILES-%03d.jpg
