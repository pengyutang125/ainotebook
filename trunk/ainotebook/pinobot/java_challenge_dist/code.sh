#!/bin/sh

cp -vf ../src/*.java ./java/ 

find . -name '*.class' -exec rm -Rvf {} \;

