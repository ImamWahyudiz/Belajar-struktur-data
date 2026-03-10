# Simulasi Antrian Bank (Queue Simulation)

Program simulasi antrian di tempat pelayanan publik (Bank) menggunakan struktur data Queue dengan GUI.

## 📋 Fitur

1. ✅ **Ambil Antrian** - Input nama, nomor di-generate otomatis (1, 2, 3, ...)
2. ✅ **Tampilkan Data Antrian** - Menampilkan semua nomor dan nama dalam antrian
3. ✅ **Panggil Antrian** - Memanggil antrian dengan tampilan visual dan suara
4. ✅ **Struktur Data Queue** - Menggunakan LinkedList (Java) dan deque (Python)
5. ✅ **GUI** - Interface grafis yang user-friendly
6. ✅ **Text-to-Speech** - Panggilan antrian dengan suara

## 🎯 Struktur Data

Program ini menggunakan **Linked List** sebagai implementasi Queue:

### Java Implementation
- **`LinkedList<Customer>`** dari Java Collections Framework
- Implementasi doubly-linked list
- Operasi queue:
  - `add()` / `offer()` - Enqueue: O(1)
  - `poll()` / `remove()` - Dequeue: O(1)
  - `peek()` - Lihat elemen depan: O(1)

### Python Implementation
- **`collections.deque`** (Double-ended Queue)
- Implementasi doubly-linked list yang dioptimalkan
- Operasi queue:
  - `append()` - Enqueue: O(1)
  - `popleft()` - Dequeue: O(1)
  - `[0]` - Peek: O(1)

### Mengapa Linked List?
✅ **Efisien untuk Queue**: Operasi tambah/hapus di ujung = O(1)  
✅ **Tidak perlu shifting**: Berbeda dengan array yang harus menggeser elemen saat dequeue  
✅ **Dynamic size**: Ukuran dapat tumbuh/menyusut sesuai kebutuhan  
✅ **FIFO**: Cocok untuk prinsip First In First Out

> **Note**: Kedua implementasi mengikuti prinsip FIFO (First In First Out) sesuai dengan konsep Queue.

## 🔧 Requirements & Dependencies

### Java Version

#### Requirements:
- **JDK 8 atau lebih tinggi** (sudah include Swing untuk GUI)
- **Tidak ada library eksternal yang diperlukan!** Semua menggunakan Java Standard Library

#### Library yang Digunakan (Built-in):
- `javax.swing.*` - Untuk GUI
- `java.awt.*` - Untuk layout dan event handling
- `java.util.LinkedList` - Untuk struktur data Queue
- `javax.sound.sampled.*` - Untuk audio (opsional)

#### Text-to-Speech:
Menggunakan system TTS (tidak perlu install library):
- **Linux**: `spd-say` (speech-dispatcher)
  ```bash
  sudo dnf install speech-dispatcher  # Fedora
  sudo apt install speech-dispatcher  # Ubuntu/Debian
  ```
- **Windows**: PowerShell built-in TTS (tidak perlu install)
- **macOS**: `say` command (built-in)

#### 🇮🇩 Voice Bahasa Indonesia:

Untuk mendapatkan TTS berbahasa Indonesia yang lebih natural:

**Linux (Otomatis):**
```bash
cd queue
./install_indonesian_voice.sh
```

**Linux (Manual):**
```bash
sudo apt install espeak-ng espeak-ng-data speech-dispatcher-espeak-ng
espeak-ng -v id "Test suara Indonesia"  # Test voice
```

**macOS:**
Gunakan voice "Damayanti" yang sudah tersedia:
- System Preferences > Accessibility > Speech > System Voice
- Pilih "Damayanti (Indonesian)"

**Windows:**
- Settings > Time & Language > Language
- Add "Bahasa Indonesia"
- Pastikan "Text-to-speech" di-check saat install

Program akan otomatis mendeteksi dan menggunakan voice Indonesia jika tersedia.

#### Compile & Run:
```bash
# Dari root directory (Struktur data)
cd "/home/Apachersa/Dokumen/Semester 4/Struktur data"

# Compile dengan package
javac queue/QueueSimulation.java

# Run dengan flag non-headless (penting!)
java -Djava.awt.headless=false queue.QueueSimulation

# Atau gunakan script yang sudah disediakan
cd queue
./run_java.sh
```

**PENTING**: Jika mendapat error `HeadlessException`, lihat [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

### Python Version

#### Requirements:
- **Python 3.6 atau lebih tinggi**
- **tkinter** - Untuk GUI (biasanya sudah included di Python)
- **espeak-ng** - Untuk Text-to-Speech dengan bahasa Indonesia (Linux)
- **pyttsx3** - Untuk TTS (optional, hanya untuk Windows/macOS)

#### Install Dependencies:
```bash
# Linux: Install espeak-ng untuk voice Indonesia
sudo apt update
sudo apt install -y espeak-ng espeak-ng-data

# Atau gunakan script installer otomatis
./install_indonesian_voice.sh

# Test voice
./test_voice.sh

# Untuk Windows/macOS: install pyttsx3
pip install pyttsx3

# Atau menggunakan requirements file
pip install -r requirements.txt
```

**Catatan Linux:** Program akan otomatis menggunakan `espeak-ng -v id` (voice Indonesia). 
Tidak perlu konfigurasi tambahan, cukup pastikan espeak-ng terinstall.

#### 🇮🇩 Voice Bahasa Indonesia (Python):

**SANGAT DIREKOMENDASIKAN: Install MBROLA untuk voice berkualitas tinggi**

```bash
# Install semua package sekaligus (RECOMMENDED)
sudo apt update
sudo apt install -y espeak-ng espeak-ng-data mbrola mbrola-id1

# Atau gunakan script installer otomatis
./install_indonesian_voice.sh

# Test voice Indonesia
espeak-ng -v mb-id1 "Nomor antrian satu, Budi"  # MBROLA (best quality)
espeak-ng -v id "Nomor antrian dua, Ani"        # Standard voice

# Test semua TTS engine
./test_voice.sh
```

**Perbedaan Voice:**
- **`mb-id1`** (MBROLA) - Voice berkualitas tinggi, sangat natural, **RECOMMENDED!**
- **`id`** (Standard) - Voice standar, lebih robotik

Program akan otomatis menggunakan MBROLA (`mb-id1`) jika tersedia, fallback ke voice standar (`id`).

**Install Manual Step-by-Step:**
```bash
# 1. Install espeak-ng engine
sudo apt install espeak-ng espeak-ng-data

# 2. Install MBROLA untuk voice berkualitas tinggi (IMPORTANT!)
sudo apt install mbrola mbrola-id1

# 3. Test
espeak-ng -v mb-id1 "Halo, ini adalah suara Indonesia yang natural"
```

#### Run:
```bash
python queue_simulation.py

# Atau
python3 queue_simulation.py

# Atau make executable
chmod +x queue_simulation.py
./queue_simulation.py
```

## 📦 File Structure

```
queue/
├── QueueSimulation.java      # Implementasi Java dengan GUI
├── queue_simulation.py        # Implementasi Python dengan GUI
├── run.sh                     # Script untuk run Java (Wayland compatible)
├── run_python.sh              # Script untuk run Python
├── requirements.txt           # Python dependencies
├── README.md                  # Dokumentasi lengkap
├── TROUBLESHOOTING.md         # Panduan troubleshooting
└── .gitignore                 # Git ignore untuk compiled files
```

## 🚀 Cara Menjalankan Program

### Java Version

**Pastikan JDK sudah terinstall:**
```bash
# Install JDK (jika belum)
sudo dnf install java-25-openjdk-devel

# Verifikasi
javac -version
```

**Jalankan dengan script (RECOMMENDED):**
```bash
# Dari folder queue
./run.sh

# Atau dari mana saja
"/home/Apachersa/Dokumen/Semester 4/Struktur data/queue/run.sh"
```

**Atau jalankan manual:**
```bash
# Set environment untuk Wayland (Fedora KDE)
export GDK_BACKEND=x11
export _JAVA_AWT_WM_NONREPARENTING=1

# Compile
javac "/home/Apachersa/Dokumen/Semester 4/Struktur data/queue/QueueSimulation.java"

# Run
java -Djava.awt.headless=false -Dawt.toolkit.name=XToolkit \
  -cp "/home/Apachersa/Dokumen/Semester 4/Struktur data" \
  queue.QueueSimulation
```

### Python Version

**Install dependencies:**
```bash
pip install pyttsx3
```

**Jalankan dengan script:**
```bash
./run_python.sh
```

**Atau manual:**
```bash
python3 queue_simulation.py
```

## 📖 Cara Penggunaan Aplikasi

### 1. Ambil Nomor Antrian
- Masukkan nama di field "Nama"
- Klik tombol "Ambil Antrian" atau tekan Enter
- Sistem akan generate nomor antrian otomatis
- Akan muncul konfirmasi dengan nomor antrian

### 2. Tampilkan Antrian
- Klik tombol "Tampilkan Antrian"
- Semua data antrian akan ditampilkan dalam tabel
- Menampilkan nomor antrian dan nama

### 3. Panggil Antrian
- Klik tombol "Panggil Antrian"
- Sistem akan mengeluarkan antrian pertama (FIFO)
- Akan muncul dialog dengan info customer yang dipanggil
- Sistem akan memanggil dengan suara (TTS)
- Data akan otomatis ter-refresh

## 🎨 Screenshots

### Java Version
- Menggunakan Swing dengan tampilan native
- Tabel untuk menampilkan antrian
- Button dengan warna-warni yang menarik
- Dialog popup untuk panggilan antrian

### Python Version
- Menggunakan Tkinter dengan ttk widgets
- Treeview untuk tampilan tabel profesional
- Layout yang responsive dan modern
- Custom colors untuk better UX

## 🔊 Text-to-Speech (TTS)

### Java
Menggunakan system command untuk TTS:
- **Linux**: `spd-say` (speech-dispatcher)
- **Windows**: PowerShell System.Speech
- **macOS**: `say` command

### Python
Menggunakan library `pyttsx3`:
- Multi-platform support (Linux, Windows, macOS)
- Offline TTS engine
- Configurable (rate, volume, voice)

Jika TTS tidak available, program tetap berjalan normal tanpa suara.

## 💡 Penjelasan Konsep Queue

Queue adalah struktur data linear yang mengikuti prinsip **FIFO (First In First Out)**:
- Element yang pertama masuk akan pertama keluar
- Operasi utama: `enqueue` (tambah) dan `dequeue` (hapus)
- Digunakan untuk: scheduling, resource sharing, breadth-first search, dll

### Operasi dalam Program:
- **Enqueue**: `queue.offer()` (Java) atau `queue.append()` (Python)
- **Dequeue**: `queue.poll()` (Java) atau `queue.popleft()` (Python)
- **Display**: Iterasi melalui queue tanpa menghapus element

## 🛠️ Troubleshooting

### Java
**Problem**: Program tidak bisa di-compile
- **Solution**: Pastikan JDK terinstall dengan benar (`java -version` dan `javac -version`)

**Problem**: TTS tidak berfungsi
- **Solution**: Install speech-dispatcher (Linux) atau pastikan system TTS aktif

### Python
**Problem**: ModuleNotFoundError: No module named 'tkinter'
- **Solution Ubuntu/Debian**: `sudo apt install python3-tk`
- **Solution Fedora**: `sudo dnf install python3-tkinter`

**Problem**: pyttsx3 error
- **Solution Linux**: Install espeak: `sudo apt install espeak`
- **Solution Windows**: Install pywin32: `pip install pywin32`

## 🎓 Penjelasan Kode

### Java
```java
// Queue menggunakan LinkedList
private LinkedList<Customer> queue;

// Enqueue (tambah antrian)
queue.offer(customer);

// Dequeue (panggil antrian)
Customer customer = queue.poll();

// Check if empty
if (queue.isEmpty()) { ... }
```

### Python
```python
# Queue menggunakan deque
from collections import deque
queue = deque()

# Enqueue (tambah antrian)
queue.append(customer)

# Dequeue (panggil antrian)
customer = queue.popleft()

# Check if empty
if not queue: ...
```

## 📝 Catatan

1. Nomor antrian akan terus bertambah selama program berjalan
2. Setelah dipanggil, data customer akan dihapus dari queue
3. Program menggunakan multi-threading untuk TTS agar tidak blocking UI
4. GUI menggunakan standard library, tidak perlu install dependency eksternal (kecuali TTS)

## 👨‍💻 Author

Dibuat untuk tugas Struktur Data - Implementasi Queue dengan GUI

## 📄 License

Free to use for educational purposes.
