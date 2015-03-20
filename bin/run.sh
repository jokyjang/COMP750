#!/bin/sh

java -classpath ~/Dropbox/Homework/GradProj/oeall22.jar:. mergematrix/SessionManagerServerStarter &
sleep 1
java -classpath ~/Dropbox/Homework/GradProj/oeall22.jar:. mergematrix/AliceIM &
java -classpath ~/Dropbox/Homework/GradProj/oeall22.jar:. mergematrix/BobIM &
#java -classpath ~/Dropbox/Homework/GradProj/oeall22.jar:. mergematrix/CathyIM &
