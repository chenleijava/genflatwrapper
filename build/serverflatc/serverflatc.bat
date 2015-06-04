flatc.exe -j -o  ../src/main/java/ -b Game.fbs&&java -jar genflatmapper.jar -l ljava -c ../src/main/java/com/dc/server/flatgen
@pause