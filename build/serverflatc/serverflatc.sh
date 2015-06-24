#!/bin/bash
#gen game server java file
./flatc -j -o  ../../src/main/java/ -b Game.fbs
#regsiter controller and msgID and make java files msgID change to static ,easy get.
java -jar genflatmapper.jar -l java -flatgen ../../src/main/java/com/dc/server/flatgen -logiccontroller ../../controller -fbspath ./
