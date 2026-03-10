#!/bin/bash

# Simple Java Run Script - No CD required

echo "🔧 Java Queue Simulation Runner"
echo "================================"
echo ""

# Define paths
WORKSPACE="/home/Apachersa/Dokumen/Semester 4/Struktur data"
JAVA_FILE="$WORKSPACE/queue/QueueSimulation.java"

# Check if JDK is installed
if ! command -v javac &> /dev/null; then
    echo "❌ javac tidak ditemukan!"
    echo ""
    echo "Install JDK dengan command berikut:"
    echo "  sudo dnf install java-25-openjdk-devel"
    echo ""
    exit 1
fi

echo "✅ JDK Found: $(javac -version 2>&1)"
echo ""

# Set environment for Wayland
export GDK_BACKEND=x11
export _JAVA_AWT_WM_NONREPARENTING=1
export DISPLAY=:0

echo "📝 Compiling from: $JAVA_FILE"sudo dnf install java-25-openjdk-devel
javac "$JAVA_FILE"

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
    echo ""
    echo "🚀 Launching GUI..."
    java -Djava.awt.headless=false -Dawt.toolkit.name=XToolkit -cp "$WORKSPACE" queue.QueueSimulation
else
    echo "❌ Compilation failed!"
    echo ""
    echo "Check errors above"
    exit 1
fi
