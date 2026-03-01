#!/bin/bash
# Test script to compile and run javaX

cd "$(dirname "$0")"
echo "Compiling javaX.java..."
javac javaX.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Generated class files:"
    ls -la *.class 2>/dev/null || echo "No class files found"
    echo ""
    echo "Running program..."
    echo "exit" | java javaX
else
    echo "Compilation failed!"
    exit 1
fi
