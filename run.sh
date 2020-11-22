rm -rf out/
mkdir out
kotlinc -d out/ src/*.kt
kotlin -cp out/ Asgn7Kt
rm -rf out/
