@echo off
echo Setting path...

set path=\Users\witherherobrine\Desktop\Program_Languages\Java\JDK\bin

echo Path set!

echo Compliling to jar...

jar cmfv GroundWar.mf  GroundWar.jar jggames/*.class

echo Jar compiled!

echo Running...

java -jar GroundWar.jar

echo Running terminated!