# Program Simulasi Teks Berjalan Berita Televisi

## Deskripsi
Program ini adalah implementasi **Circular Doubly Linked List** untuk simulasi teks berjalan berita seperti yang sering tampil di televisi. Program tersedia dalam dua versi: **Java** dan **Python**.

**Implementasi ini tidak menggunakan library LinkedList bawaan** - semua operasi linked list diimplementasikan secara manual untuk tujuan pembelajaran.

## Fitur Program

### Menu Utama
1. **Insert Berita Baru** - Menambahkan berita baru di akhir list
2. **Hapus Berita** - Menghapus berita berdasarkan nomor urut
3. **Tampilkan Berita Forward** - Menampilkan berita dari depan ke belakang dengan animasi
4. **Tampilkan Berita Backward** - Menampilkan berita dari belakang ke depan dengan animasi
5. **Tampilkan Berita Tertentu** - Menampilkan detail berita berdasarkan nomor urut
6. **Lihat Daftar Semua Berita** - Menampilkan daftar berita tanpa animasi
7. **Exit** - Keluar dari program

### Fitur Khusus
- ✨ **Animasi Scrolling Horizontal** - Teks bergerak dari kanan ke kiri seperti di TV
- 🔄 **Circular Navigation** - Berita berputar dari awal ke akhir secara loop
- ↔️ **Bidirectional** - Bisa tampil maju dan mundur
- 📺 **Teks Berjalan Real-time** - Scrolling dalam 1 baris yang di-overwrite terus

## Cara Menjalankan

### ⚠️ PENTING - Untuk Animasi Scrolling
**Animasi scrolling horizontal hanya bekerja di terminal asli/native.** VS Code output window tidak mendukung carriage return (`\r`), sehingga teks akan bertumpuk vertikal.

**Jalankan dari terminal:**
```bash
# Java
cd "/home/Apachersa/Dokumen/Semester 4/Struktur data"
java -cp bin circulardoublylinkedlist.javaX

# Python  
cd "/home/Apachersa/Dokumen/Semester 4/Struktur data"
python3 circulardoublylinkedlist/python.py
```

Jangan gunakan tombol Run di VS Code untuk melihat animasi scrolling!

## Spesifikasi

- **Struktur Data**: Circular Doubly Linked List
- **Kapasitas**: Dynamic (tidak terbatas, sesuai memori tersedia)
- **Data**: Teks berita (String)
- **Animasi**: Typing effect dengan delay per karakter
- **Delay Antar Berita**: 3 detik
- **Validasi**: Program melakukan validasi nomor urut untuk operasi

## Karakteristik Circular Doubly Linked List

### Struktur
```
        ┌──────────────────────────────────┐
        │                                  │
        ▼                                  │
    ┌──────┐    ┌──────┐    ┌──────┐    ┌──────┐
◄───┤ Node ├───►│ Node ├───►│ Node ├───►│ Node ├───►
    └──────┘    └──────┘    └──────┘    └──────┘
        ▲                                  │
        │                                  │
        └──────────────────────────────────┘
```

### Kelebihan
- **Circular**: Node terakhir terhubung ke node pertama (loop)
- **Bidirectional**: Bisa traversal maju (next) dan mundur (prev)
- **No Dead End**: Tidak ada pointer NULL di next/prev
- **Efficient**: Insert/delete di awal dan akhir lebih efisien
- **Dynamic Size**: Ukuran fleksibel sesuai kebutuhan

### Kekurangan
- **Complex Implementation**: Lebih kompleks dari singly linked list
- **Extra Memory**: Memerlukan dua pointer per node (next & prev)
- **Careful Handling**: Harus hati-hati mengelola circular links

### Time Complexity
| Operasi | Complexity |
|---------|-----------|
| Insert at End | O(1) dengan tail pointer / O(n) tanpa tail |
| Delete at Position | O(n) |
| Display Forward | O(n) |
| Display Backward | O(n) |
| Access by Position | O(n) |

## Cara Menjalankan

### Versi Java (javaX.java)

#### Prasyarat
- Java Development Kit (JDK) versi 11 atau lebih baru
- Java compiler (`javac`) dan Java runtime (`java`)

#### Compile dan Run
```bash
# Compile dari parent directory (Struktur data)
javac circulardoublylinkedlist/javaX.java

# Run dari parent directory
java circulardoublylinkedlist.javaX
```

### Versi Python (python.py)

#### Prasyarat
- Python 3.6 atau lebih baru

#### Run Langsung
```bash
# Run dari root directory
python3 circulardoublylinkedlist/python.py

# Atau jika berada di dalam folder
cd circulardoublylinkedlist
python3 python.py

# Atau dengan shebang (Linux/Mac)
chmod +x python.py
./python.py
```

## Struktur Kode

### Java
```
circulardoublylinkedlist/javaX.java
├── class Node                    # Node class untuk CDLL
│   ├── String berita
│   ├── Node prev
│   └── Node next
└── class javaX                   # Main class
    ├── insertBerita()            # Insert di akhir
    ├── hapusBerita()             # Hapus berdasarkan nomor
    ├── tampilkanForward()        # Display maju dengan animasi
    ├── tampilkanBackward()       # Display mundur dengan animasi
    ├── tampilkanBeritaTertentu() # Display berita spesifik
    ├── lihatDaftarBerita()       # List semua berita
    └── run()                     # Main loop
```

### Python
```
circulardoublylinkedlist/python.py
├── class Node                         # Node class untuk CDLL
│   ├── berita: str
│   ├── prev: Node
│   └── next: Node
└── class BeritaRunningText           # Main class
    ├── insert_berita()               # Insert di akhir
    ├── hapus_berita()                # Hapus berdasarkan nomor
    ├── tampilkan_forward()           # Display maju dengan animasi
    ├── tampilkan_backward()          # Display mundur dengan animasi
    ├── tampilkan_berita_tertentu()   # Display berita spesifik
    ├── lihat_daftar_berita()         # List semua berita
    └── run()                         # Main loop
```

## Contoh Penggunaan

### Contoh Sesi Program

```
╔════════════════════════════════════════════════════════════╗
║       SIMULASI TEKS BERJALAN BERITA TELEVISI               ║
║       Circular Doubly Linked List Implementation           ║
╠════════════════════════════════════════════════════════════╣
║  1. Insert berita baru                                     ║
║  2. Hapus berita                                           ║
║  3. Tampilkan berita (Forward - Depan ke Belakang)         ║
║  4. Tampilkan berita (Backward - Belakang ke Depan)        ║
║  5. Tampilkan berita tertentu                              ║
║  6. Lihat daftar semua berita                              ║
║  7. Exit                                                   ║
╚════════════════════════════════════════════════════════════╝
Pilih menu (1-7): 1

Masukkan teks berita: Breaking News: Presiden mengumumkan kebijakan baru
✓ Berita berhasil ditambahkan!
  Total berita: 1

Pilih menu (1-7): 1

Masukkan teks berita: Update Cuaca: Hujan diprediksi turun sore hari
✓ Berita berhasil ditambahkan!
  Total berita: 2

Pilih menu (1-7): 6

╔════════════════════════════════════════════════════════════╗
║                  DAFTAR SEMUA BERITA                       ║
╠════════════════════════════════════════════════════════════╣
  Total berita: 2
────────────────────────────────────────────────────────────
  [1] Breaking News: Presiden mengumumkan kebijakan baru
  [2] Update Cuaca: Hujan diprediksi turun sore hari
╚════════════════════════════════════════════════════════════╝

Pilih menu (1-7): 3

╔════════════════════════════════════════════════════════════╗
║          TEKS BERJALAN BERITA - MODE FORWARD               ║
╠════════════════════════════════════════════════════════════╣
  Total berita: 2
  Delay: 3 detik antar berita
╚════════════════════════════════════════════════════════════╝

============================================================
>>> BREAKING NEWS [1/2]
============================================================

  Breaking News: Presiden mengumumkan kebijakan baru

============================================================
Berita berikutnya dalam 3 detik...

[Animasi dengan efek mengetik karakter per karakter...]
```

## Konsep Circular Doubly Linked List

### Visualisasi Node
```
┌──────────────────────────┐
│   Node                   │
├──────────────────────────┤
│   berita: String         │
│   prev: Node ◄───┐       │
│   next: Node ────┼───►   │
└──────────────────┼───────┘
                   └───Circular link
```

### Circular Property
```
head
  │
  ▼
┌─────────────────────────────────────────────┐
│                                             │
│  ┌──────┐ ◄──► ┌──────┐ ◄──► ┌──────┐     │
└─►│ N1   │      │ N2   │      │ N3   │ ────┘
   └──────┘      └──────┘      └──────┘
      ▲                            │
      └────────────────────────────┘
```

### Insert at End
```
Sebelum:
head -> [A] ◄─► [B] ◄─► [C] ─┐
  ▲                           │
  └───────────────────────────┘

Sesudah:
head -> [A] ◄─► [B] ◄─► [C] ◄─► [X] ─┐
  ▲                                   │
  └───────────────────────────────────┘
```

### Delete at Position
```
Hapus posisi 2:
Sebelum: [A] ◄─► [B] ◄─► [C] ◄─► [D]
Sesudah: [A] ◄─► [C] ◄─► [D]
                  ▲        │
                  └────────┘
```

## Perbedaan dengan Jenis Linked List Lain

| Aspek | Singly LL | Doubly LL | Circular DLL |
|-------|-----------|-----------|--------------|
| Pointer per Node | 1 (next) | 2 (prev, next) | 2 (prev, next) |
| Traversal | Satu arah | Dua arah | Dua arah |
| Circular | Tidak | Tidak | Ya |
| End Detection | next == null | next == null | next == head |
| Memory | Paling efisien | Sedang | Sedang |
| Complexity | Sederhana | Sedang | Kompleks |
| Use Case | Sederhana | Undo/Redo | Playlist, Buffer |

## Aplikasi Praktis

Program ini mensimulasikan penggunaan Circular Doubly Linked List dalam kehidupan nyata:

1. **Teks Berjalan TV** 📺
   - Berita berputar terus menerus
   - Bisa navigasi maju mundur
   - Loop otomatis tanpa batas

2. **Music Playlist** 🎵
   - Lagu berputar loop
   - Previous dan next track
   - Repeat all mode

3. **Image Carousel** 🖼️
   - Gallery foto melingkar
   - Navigate left/right
   - Infinite scroll

4. **Browser History** 🌐
   - Back dan forward navigation
   - Circular buffer
   - Tab rotation

5. **Round Robin Scheduling** ⚙️
   - Process scheduling
   - Fair time allocation
   - Circular queue

## Validasi dan Error Handling

Program melakukan validasi untuk:
- ✓ Nomor urut harus antara 1 sampai count
- ✓ Berita tidak boleh kosong saat insert
- ✓ Operasi pada list kosong
- ✓ Input data yang valid
- ✓ Exception handling untuk input error

## Catatan Penting

1. **Tidak Menggunakan Library**
   - Java: Tidak menggunakan `java.util.LinkedList`
   - Python: Tidak menggunakan `collections.deque`
   - Semua operasi diimplementasikan manual

2. **Circular Links**
   - head.prev selalu menunjuk ke node terakhir (tail)
   - tail.next selalu menunjuk ke head
   - Tidak ada pointer NULL/None dalam circular structure

3. **Animasi dan Delay**
   - Typing effect: 30ms per karakter
   - Delay antar berita: 3000ms (3 detik)
   - Bisa disesuaikan dengan kebutuhan

4. **Memory Management**
   - Java: Garbage collector otomatis
   - Python: Reference counting otomatis

## Pembelajaran

Program ini cocok untuk:
- Memahami konsep Circular Doubly Linked List
- Mempelajari bidirectional traversal
- Memahami circular data structure
- Praktik implementasi struktur data kompleks
- Aplikasi real-world (teks berjalan berita)

## Tips Penggunaan

1. **Insert Berita**: Tambahkan beberapa berita terlebih dahulu
2. **View List**: Gunakan menu 6 untuk melihat daftar tanpa animasi
3. **Forward Display**: Menu 3 untuk pengalaman teks berjalan lengkap
4. **Backward Display**: Menu 4 untuk melihat berita dari belakang
5. **Specific View**: Menu 5 untuk cek berita tertentu tanpa menunggu

## Author

Program ini dibuat untuk keperluan pembelajaran Struktur Data.

## Lisensi

Program ini dibuat untuk tujuan edukasi dan pembelajaran.
