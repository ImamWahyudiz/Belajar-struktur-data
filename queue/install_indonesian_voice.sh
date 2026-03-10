#!/bin/bash

# Script untuk menginstall voice Indonesia untuk TTS

echo "=========================================="
echo "  Install Voice Indonesia untuk TTS"
echo "=========================================="
echo ""

# Detect OS
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    echo "🐧 Detected: Linux"
    echo ""
    echo "📦 Installing espeak-ng for Indonesian voice support..."
    echo ""
    
    # Check if user has sudo access
    if command -v sudo &> /dev/null; then
        echo "Installing packages:"
        echo "  - espeak-ng (TTS engine)"
        echo "  - espeak-ng-data (voice data including Indonesian)"
        echo "  - speech-dispatcher (speech dispatcher integration)"
        echo "  - mbrola (high-quality voice synthesizer)"
        echo "  - mbrola-id1 (Indonesian MBROLA voice - SANGAT RECOMMENDED!)"
        echo ""
        
        # Detect package manager
        if command -v dnf &> /dev/null; then
            # Fedora/RHEL/CentOS
            echo "📥 Installing espeak-ng (Fedora/RHEL)..."
            sudo dnf install -y espeak-ng speech-dispatcher
            
            echo ""
            echo "📥 Installing MBROLA for high-quality Indonesian voice..."
            sudo dnf install -y mbrola mbrola-id1
        elif command -v apt &> /dev/null; then
            # Debian/Ubuntu
            echo "📥 Installing espeak-ng (Debian/Ubuntu)..."
            sudo apt update
            sudo apt install -y espeak-ng espeak-ng-data speech-dispatcher-espeak-ng
            
            echo ""
            echo "📥 Installing MBROLA for high-quality Indonesian voice..."
            sudo apt install -y mbrola mbrola-id1
        elif command -v pacman &> /dev/null; then
            # Arch Linux
            echo "📥 Installing espeak-ng (Arch Linux)..."
            sudo pacman -S --noconfirm espeak-ng speech-dispatcher mbrola mbrola-voices-id1
        else
            echo "❌ Package manager tidak dikenali"
            echo "   Install manual sesuai distro Anda"
            exit 1
        fi
        
        if [ $? -eq 0 ]; then
            echo ""
            echo "✅ Installation successful!"
            echo ""
            echo "Testing Indonesian voices:"
            echo ""
            
            # Test MBROLA voice first (better quality)
            if command -v mbrola &> /dev/null; then
                echo "🔊 Testing MBROLA Indonesian voice (high quality):"
                espeak-ng -v mb-id1 "Nomor antrian satu atas nama Budi" 2>/dev/null
                if [ $? -eq 0 ]; then
                    echo "✅ MBROLA voice (mb-id1) berfungsi dengan baik!"
                    echo "   (Ini adalah voice berkualitas tinggi yang akan digunakan)"
                else
                    echo "⚠️  MBROLA voice tidak berfungsi, menggunakan voice standar"
                fi
            else
                echo "⚠️  MBROLA tidak terinstall, menggunakan voice standar"
            fi
            
            echo ""
            echo "🔊 Testing standard Indonesian voice:"
            espeak-ng -v id "Nomor antrian dua atas nama Ani"
            echo ""
            echo "✅ Voice Indonesia siap digunakan!"
        else
            echo ""
            echo "❌ Installation failed. Please try manually:"
            echo ""
            echo "Untuk Fedora/RHEL:"
            echo "   sudo dnf install espeak-ng speech-dispatcher mbrola mbrola-id1"
            echo ""
            echo "Untuk Debian/Ubuntu:"
            echo "   sudo apt install espeak-ng espeak-ng-data mbrola mbrola-id1"
            echo ""
            echo "Untuk Arch Linux:"
            echo "   sudo pacman -S espeak-ng speech-dispatcher mbrola mbrola-voices-id1"
        fi
    else
        echo "❌ sudo not available. Please run manually:"
        echo ""
        echo "Untuk Fedora/RHEL:"
        echo "   sudo dnf install espeak-ng speech-dispatcher mbrola mbrola-id1"
        echo ""
        echo "Untuk Debian/Ubuntu:"
        echo "   sudo apt install espeak-ng espeak-ng-data mbrola mbrola-id1"
    fi
    
elif [[ "$OSTYPE" == "darwin"* ]]; then
    echo "🍎 Detected: macOS"
    echo ""
    echo "Voice Indonesia 'Damayanti' should be available by default on macOS."
    echo ""
    echo "To enable it:"
    echo "1. Open System Preferences > Accessibility > Speech"
    echo "2. Select 'Damayanti' as the System Voice"
    echo ""
    echo "Testing available voices:"
    say -v "?" | grep -i indonesia
    echo ""
    echo "Testing with Damayanti:"
    say -v Damayanti "Nomor antrian satu, Budi"
    
elif [[ "$OSTYPE" == "msys" ]] || [[ "$OSTYPE" == "cygwin" ]]; then
    echo "🪟 Detected: Windows"
    echo ""
    echo "To install Indonesian voice on Windows:"
    echo ""
    echo "1. Open Settings > Time & Language > Language"
    echo "2. Click 'Add a language'"
    echo "3. Search for 'Indonesian' (Bahasa Indonesia)"
    echo "4. Click 'Next' and make sure 'Text-to-speech' is checked"
    echo "5. Click 'Install'"
    echo ""
    echo "After installation, restart your application."
    
else
    echo "❓ Unknown OS: $OSTYPE"
    echo "Please install TTS voice manually for your system."
fi

echo ""
echo "=========================================="
echo "Installation script completed"
echo "=========================================="
