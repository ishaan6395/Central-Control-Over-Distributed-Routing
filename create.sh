#!/bin/bash

files=`find . -name "*.java"`
mkdir out
javac -source 1.8 -target 1.8 -d "out" -cp "lib/gson-2.6.2.jar:lib/sqlite-jdbc-3.21.0.jar" $files

if [ $? -eq 0 ] # If the previous command exited with 0 ie no error
then
	echo Compiled..
	docker stop $(docker ps -aq)
	docker system prune -f
	docker image rm fibbing #_sub

	docker image build -t fibbing .

else
	echo Did Not Compile..
	exit 1
fi
