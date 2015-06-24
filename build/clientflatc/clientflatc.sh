#!/bin/bash
#gen game header for client
flatc -c -o  ../../src/flatgen -b Game.fbs
#regsiter controller and msgID and make game header msgID change to static ,easy get
java -jar genflatmapper.jar -l cpp -c ../../src/controller -f Game.fbs -g ../../src/flatgen
