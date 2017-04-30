@echo off
echo File?
read FILE

cd ../output
java Simple2 ../src/yalcode/$FILE.yal
cd ../src/

