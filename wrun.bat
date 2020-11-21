rmdir out /S /Q
mkdir out
kotlinc src\*.kt -o out\out
out\out.exe
rmdir out /S /Q