flatc.exe -c -o  ../../src/flatgen -b Game.fbs &&java -jar genflatmapper.jar -l cpp -c ../../src/controller -f ./ -g ../../src/flatgen