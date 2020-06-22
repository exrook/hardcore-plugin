#!/bin/bash
set -e # exit when any command returns nonzero

javac -classpath ./spigot-api.jar HardcorePlugin.java

mkdir -p org/distanthills/hardcore
cp *.class org/distanthills/hardcore/
jar cf HardcorePlugin.jar org/distanthills/hardcore/*.class plugin.yml
rm -r org
echo BUILD COMPLETE
