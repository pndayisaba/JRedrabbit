#!/bin/bash

echo "COMPILING SERVLETS IN PROGRESS ...";
cd servlet;
wicPath="../WEB-INF/classes/";
for f in *.java;
  do
    echo "Compiling $f";
    javac $f -Xlint:unchecked;
    
    echo "Creating $wicPath$fileName.class";
    fileName=${f%.*};
    mv "$fileName.class" $wicPath; 
  done


echo "";
echo "";
echo "COPYING PACKAGE FILES (.jar) IN PROGRESS ...";
cd ../lib;
libPath="../../../lib/";
for f in *.jar;
  do
    echo "Creating $libPath$f";
    cp $f $libPath;
  done

echo "";
 echo "DONE";

 
