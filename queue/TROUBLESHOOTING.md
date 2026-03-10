# Troubleshooting Java GUI

## ⚠️ KHUSUS FEDORA WAYLAND (KDE Plasma)

Fedora KDE Plasma menggunakan Wayland secara default. Java Swing memerlukan XWayland untuk berjalan dengan baik.

---

## Problem 1: `javac: command not found`


**Penyebab:** JDK belum terinstall, hanya ada JRE (Java Runtime).

**Solusi:**
```bash
# Install JDK lengkap
sudo dnf install java-25-openjdk-devel

# Verifikasi
javac -version
```

---

## Problem 2: `HeadlessException`

```
java.awt.HeadlessException: No X11 DISPLAY variable was set
```

**Penyebab:** Java mendeteksi environment sebagai headless (tanpa GUI).

**Solusi Terbaik - Gunakan Script:**
```bash
"/home/Apachersa/Dokumen/Semester 4/Struktur data/queue/run.sh"
```

Script ini sudah mengatur semua environment variables yang diperlukan untuk Wayland.

**Atau Manual:**
```bash
# Set environment variables
export GDK_BACKEND=x11
export _JAVA_AWT_WM_NONREPARENTING=1
export DISPLAY=:0

# Run dengan flag yang benar
java -Djava.awt.headless=false -Dawt.toolkit.name=XToolkit \
  -cp "/home/Apachersa/Dokumen/Semester 4/Struktur data" \
  queue.QueueSimulation
```

---

## Problem 3: Package Error

```
The declared package "" does not match the expected package "queue"
```

**Penyebab:** Package declaration di-comment atau salah.

**Solusi:** Pastikan baris pertama file adalah:
```java
package queue;
```

Bukan `// package queue;`

---

## Problem 4: Voice TTS Berbahasa Inggris / Aneh

**Penyebab:** Voice Indonesia belum terinstall atau menggunakan voice standar yang kurang natural.

**Solusi TERBAIK - Install MBROLA (Voice Berkualitas Tinggi):**
```bash
# Install MBROLA untuk voice Indonesia yang sangat natural
sudo apt update
sudo apt install -y espeak-ng espeak-ng-data mbrola mbrola-id1

# Test MBROLA voice (jauh lebih natural!)
espeak-ng -v mb-id1 "Nomor antrian satu, Budi silakan menuju ke loket"

# Atau gunakan script installer
./install_indonesian_voice.sh

# Test semua voice
./test_voice.sh
```

**Solusi Manual (Linux) - Minimum:**
```bash
# Install espeak-ng dengan voice Indonesia standar
sudo apt update
sudo apt install -y espeak-ng espeak-ng-data

# Test voice Indonesia standar (lebih robotik)
espeak-ng -v id "Nomor antrian satu, Budi"
```

**PENTING untuk Linux:**

Program Python akan otomatis menggunakan voice terbaik yang tersedia:
1. **Priority 1**: `mb-id1` (MBROLA) - **Sangat natural, RECOMMENDED!**
2. **Priority 2**: `id` (Standard) - Voice standar espeak-ng
3. **Priority 3**: `espeak` atau `spd-say` - Fallback engines

**Tidak perlu konfigurasi tambahan.** Program akan otomatis detect dan gunakan voice terbaik.

**Perbedaan Voice:**
- **MBROLA (`mb-id1`)**: Suara manusia yang sangat natural, seperti native speaker
- **Standard (`id`)**: Suara robot/synthesized, kurang natural tapi tetap Indonesia

**Untuk macOS:**
1. Buka System Preferences > Accessibility > Speech
2. Pilih voice "Damayanti" (Indonesian)

**Untuk Windows:**
1. Settings > Time & Language > Language
2. Add language: "Bahasa Indonesia"
3. Pastikan opsi "Text-to-speech" di-check saat install

---

## Verifikasi System

### Cek Display Server:
```bash
echo $XDG_SESSION_TYPE  # Harus output: wayland
echo $DISPLAY           # Harus output: :0
```

### Cek XWayland:
```bash
which Xwayland          # Harus output: /usr/bin/Xwayland
```

### Cek Java:
```bash
java -version           # Cek Runtime
javac -version          # Cek Compiler (harus ada!)
```

---

## Alternative

Jika Java tetap bermasalah, gunakan versi Python yang lebih stabil:
```bash
pip install pyttsx3
python3 "/home/Apachersa/Dokumen/Semester 4/Struktur data/queue/queue_simulation.py"
```

---

## Tips

1. **Selalu run dari terminal GUI** (Konsole), bukan SSH
2. **Restart terminal** setelah install JDK
3. **Gunakan script `run.sh`** untuk kemudahan
4. **File .class akan auto-cleanup** oleh .gitignore
