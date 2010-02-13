#!/bin/sh

cp -vf ../src/*.java ./java/ 
cp -vf ../src/org/berlin/tron/gl/game/*.java ./java/org/berlin/tron/gl/game/

find . -name '*.class' -exec rm -Rvf {} \;

