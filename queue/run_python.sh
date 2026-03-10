#!/bin/bash

# Script untuk run Python Queue Simulation

echo "============================================"
echo "  Queue Simulation - Python Runner"
echo "============================================"
echo ""

# Tentukan Python command
PYTHON_CMD="python3"

# Check if virtual environment exists
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PARENT_DIR="$(dirname "$SCRIPT_DIR")"
VENV_PATH="$PARENT_DIR/.venv/bin/python"

if [ -f "$VENV_PATH" ]; then
    echo "✅ Virtual environment ditemukan"
    PYTHON_CMD="$VENV_PATH"
else
    # Check if python3 exists
    if ! command -v python3 &> /dev/null; then
        echo "❌ Error: python3 tidak ditemukan!"
        echo "   Silakan install Python 3 terlebih dahulu."
        exit 1
    fi
fi

echo "✅ Python ditemukan:"
"$PYTHON_CMD" --version
echo ""

# Check if pyttsx3 is installed
echo "🔍 Checking dependencies..."
"$PYTHON_CMD" -c "import pyttsx3" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "⚠️  Warning: pyttsx3 tidak ditemukan!"
    echo "   Install dengan: pip install pyttsx3"
    echo "   Program tetap bisa dijalankan tanpa TTS."
    echo ""
fi

"$PYTHON_CMD" -c "import tkinter" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "❌ Error: tkinter tidak ditemukan!"
    echo "   Install dengan: sudo apt install python3-tk"
    exit 1
fi

echo "✅ Dependencies OK!"
echo ""
echo "🚀 Running queue_simulation.py..."
echo ""
cd "$SCRIPT_DIR"
"$PYTHON_CMD" queue_simulation.py
