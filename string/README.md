# Program Manajemen Data Mahasiswa

## Deskripsi
Program ini adalah implementasi struktur data array dengan berbagai operasi insert dan delete untuk manajemen data mahasiswa. Program tersedia dalam dua versi: **Java** dan **Python**.

## Fitur Program

### Operasi Insert (Tambah Data)
1. **Insert at Beginning** - Menambahkan data di awal array
2. **Insert at Given Position** - Menambahkan data di posisi tertentu
3. **Insert at End** - Menambahkan data di akhir array

### Operasi Delete (Hapus Data)
4. **Delete from Beginning** - Menghapus data dari awal array
5. **Delete from Given Position** - Menghapus data dari posisi tertentu
6. **Delete from End** - Menghapus data dari akhir array
7. **Delete First Occurrence** - Menghapus data berdasarkan NIM tertentu

### Operasi Lainnya
8. **Show Data** - Menampilkan semua data mahasiswa
9. **Exit** - Keluar dari program

## Spesifikasi

- **Kapasitas Array**: 10 elemen (tetap/fixed size)
- **Data Mahasiswa**: NIM (String) dan Nama (String)
- **Validasi**: Program melakukan validasi posisi dan kapasitas array

## Cara Menjalankan

### Versi Java (MahasiswaApp.java)

#### Prasyarat
- Java Development Kit (JDK) versi 11 atau lebih baru
- Java compiler (`javac`) dan Java runtime (`java`)

#### Compile dan Run
```bash
# Compile
javac string/MahasiswaApp.java

# Run
java string.MahasiswaApp
```

#### Atau jika berada di dalam folder string:
```bash
cd string
javac MahasiswaApp.java
java MahasiswaApp
```

### Versi Python (mahasiswa.py)

#### Prasyarat
- Python 3.6 atau lebih baru

#### Run
```bash
# Langsung jalankan
python3 string/mahasiswa.py

# Atau
python string/mahasiswa.py
```

#### Atau jika berada di dalam folder string:
```bash
cd string
python3 mahasiswa.py

# Atau buat executable (Linux/Mac)
chmod +x mahasiswa.py
./mahasiswa.py
```

## Contoh Penggunaan

### 1. Menambahkan Data di Awal
```
Pilih menu: 1
Masukkan NIM: 123456
Masukkan Nama: Budi Santoso
Data berhasil ditambahkan di awal!
```

### 2. Menambahkan Data di Posisi Tertentu
```
Pilih menu: 2
Masukkan posisi (0 - 1): 1
Masukkan NIM: 234567
Masukkan Nama: Ani Wijaya
Data berhasil ditambahkan di posisi 1!
```

### 3. Menampilkan Data
```
Pilih menu: 8

========== DATA MAHASISWA ==========
Jumlah data: 2
1. NIM: 123456, Nama: Budi Santoso
2. NIM: 234567, Nama: Ani Wijaya
===================================
```

### 4. Menghapus Data Berdasarkan NIM
```
Pilih menu: 7
Masukkan NIM yang ingin dihapus: 123456
Data dengan NIM 123456 berhasil dihapus!
```

## Struktur Program

### Java Version (MahasiswaApp.java)
```
package string;

├── class Mahasiswa
│   ├── String nim
│   ├── String nama
│   └── toString()
│
└── public class MahasiswaApp
    ├── CAPACITY = 10
    ├── Mahasiswa[] data
    ├── int count
    ├── insertAtBeginning()
    ├── insertAtPosition()
    ├── insertAtEnd()
    ├── deleteFromBeginning()
    ├── deleteFromPosition()
    ├── deleteFromEnd()
    ├── deleteFirstOccurrence()
    ├── showData()
    ├── displayMenu()
    └── main()
```

### Python Version (mahasiswa.py)
```
#!/usr/bin/env python3

├── class Mahasiswa
│   ├── __init__(nim, nama)
│   └── __str__()
│
├── class MahasiswaApp
│   ├── CAPACITY = 10
│   ├── data = [None] * CAPACITY
│   ├── count = 0
│   ├── insert_at_beginning()
│   ├── insert_at_position()
│   ├── insert_at_end()
│   ├── delete_from_beginning()
│   ├── delete_from_position()
│   ├── delete_from_end()
│   ├── delete_first_occurrence()
│   ├── show_data()
│   └── display_menu()
│
└── main()
```

## Konsep Struktur Data

### Array Operations

#### Insert Operations
- **Time Complexity**:
  - Insert at beginning: O(n) - perlu shift semua elemen
  - Insert at position: O(n) - perlu shift sebagian elemen
  - Insert at end: O(1) - langsung tambah di akhir

#### Delete Operations
- **Time Complexity**:
  - Delete from beginning: O(n) - perlu shift semua elemen
  - Delete from position: O(n) - perlu shift sebagian elemen
  - Delete from end: O(1) - langsung hapus dari akhir

### Array Characteristics
- **Fixed Size**: Kapasitas array tetap 10 elemen
- **Contiguous Memory**: Elemen tersimpan berurutan di memory
- **Zero-based Indexing**: Indeks dimulai dari 0

## Validasi dan Error Handling

Program melakukan validasi untuk:
-  Posisi insert/delete (harus dalam range yang valid)
-  Kapasitas array (tidak boleh melebihi 10 elemen)
-  Array kosong (tidak bisa delete jika kosong)
-  Input tidak valid (khusus Python: ValueError handling)
-  NIM tidak ditemukan (untuk delete first occurrence)

## Perbedaan Java vs Python

| Aspek | Java | Python |
|-------|------|--------|
| Tipe Data | Static typing (`String`) | Dynamic typing |
| Array | Fixed size array `Mahasiswa[]` | List dengan None `[None] * 10` |
| Loop | for (int i = ...) | for i in range(...) |
| String Format | "text " + variable | f"text {variable}" |
| Input | Scanner.nextLine() | input() |
| Package | package string; | N/A (module-based) |
| Execution | Compile → Run | Interpreted |
| Method Naming | camelCase | snake_case |
| Exception | try-catch | try-except |

## Catatan Penting

1. **Kapasitas Terbatas**: Array memiliki kapasitas maksimal 10 elemen. Jika penuh, tidak bisa menambah data lagi.
2. **Shift Operations**: Operasi insert dan delete (kecuali di end) memerlukan pergeseran elemen, yang memakan waktu O(n).
3. **Memory Management**: 
   - Java: Array tetap menggunakan 10 slot memory
   - Python: List juga menggunakan 10 slot dengan None sebagai placeholder
4. **Input Validation**: Program memvalidasi input untuk mencegah error runtime.

## Pengembangan Lebih Lanjut

Beberapa fitur yang bisa ditambahkan:
- [ ] Dynamic array (auto-resize)
- [ ] Search by name
- [ ] Sort data (by NIM or name)
- [ ] Save/load data to/from file
- [ ] Update data mahasiswa
- [ ] Undo/redo operations
- [ ] Bulk insert/delete
- [ ] Export to CSV/JSON

## Lisensi
Program ini dibuat untuk tujuan pembelajaran struktur data.

## Kontributor
- Program dibuat sebagai bagian dari mata kuliah Struktur Data Semester 4

---


