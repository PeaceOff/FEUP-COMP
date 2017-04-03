@echo off
set /p file=Yal File Name?

cd ../output
java Simple2 ../src/yalcode/%file%.yal
cd ../src/
pause
