javac -d output $(find $(pwd) -name "*.java")
java -cp output Main
