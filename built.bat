@echo off

cls

echo Setting path...

set path=\Users\witherherobrine\Desktop\Program_Languages\Java\JDK\bin

echo Path set!

echo Building...

del /S *.class

cd jggames

javac *.java

cd..

javac -d . *.java

echo Building done!


echo Running...

java jggames.Launch

echo Running terminated!