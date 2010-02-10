#!/bin/sh

cp -vf ../src/*.java ./java/ 

find . -name '*.class' -exec rm -Rvf {} \;

tar -cvf ../java_challenge_dist.tar .
gzip ../java_challenge_dist.tar
