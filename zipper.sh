#!/bin/sh

cd ./build/libs/
rm db-generator.jar.zip
zip db-generator.jar.zip db-generator.jar
echo "Done!"