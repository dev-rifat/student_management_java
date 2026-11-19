#!/bin/bash
# Compile & run: ./run.sh
cd "$(dirname "$0")"
CP="lib/*"
rm -rf out && mkdir -p out
javac -d out -cp "$CP" $(find src/main/java -name "*.java") || exit 1
java -cp "out:$CP" com.sms.Main
