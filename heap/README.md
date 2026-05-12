# Program Heap (Min-Heap dan Max-Heap)

## Deskripsi
Program ini adalah implementasi struktur data Heap dengan berbagai operasi untuk manajemen data barang. Program tersedia dalam dua versi: **Java** dan **Python**.

Heap adalah struktur data binary tree yang memenuhi heap property:
- **Min-Heap**: Parent node ≤ Children nodes (root adalah elemen terkecil)
- **Max-Heap**: Parent node ≥ Children nodes (root adalah elemen terbesar)

## Fitur Program

### Operasi Insert (Tambah Data)
1. **Insert/Tambah Data** - Menambahkan data barang (ID dan Nama) ke dalam Min-Heap dan Max-Heap sekaligus
   - Melakukan validasi untuk mencegah duplikasi ID
   - Time Complexity: O(log n)

### Operasi Display (Tampilkan Data)
2. **Display Ascending** - Menampilkan data urut berdasarkan ID secara ascending menggunakan Min-Heap
   - Data ditampilkan dari terkecil ke terbesar berdasarkan ID
   - Time Complexity: O(n log n)

3. **Display Descending** - Menampilkan data urut berdasarkan ID secara descending menggunakan Max-Heap
   - Data ditampilkan dari terbesar ke terkecil berdasarkan ID
   - Time Complexity: O(n log n)

### Operasi Delete (Hapus Data)
4. **Delete from Min-Heap** - Menghapus data barang berdasarkan ID dari Min-Heap
   - Time Complexity: O(n)

5. **Delete from Max-Heap** - Menghapus data barang berdasarkan ID dari Max-Heap
   - Time Complexity: O(n)

### Operasi Load Data
6. **Load from CSV** - Memuat data dari file CSV ke dalam Min-Heap dan Max-Heap
   - Membaca format CSV: ID,Nama (dengan header)
   - Melakukan validasi dan error handling
   - Mencegah duplikasi ID

## Spesifikasi

- **Data Barang**: ID (Integer) dan Nama (String)
- **Struktur**: Min-Heap dan Max-Heap (Array-based binary tree)
- **Validasi**: Program melakukan validasi duplikasi ID dan format data
- **Representasi**: Menggunakan array (list) dengan indexing:
  - Parent(i) = (i-1)/2
  - Left Child(i) = 2*i + 1
  - Right Child(i) = 2*i + 2

## Konsep Heap

### Min-Heap Property
```
        1                   <- Root (Smallest element)
       / \
      3   2
     / \ /
    7  4 5
```
- Parent ≤ Children
- Optimal untuk mencari elemen minimum
- Operasi: insert, deleteMin, heapifyUp, heapifyDown

### Max-Heap Property
```
        7                   <- Root (Largest element)
       / \
      3   5
     / \ / \
    1  2 4  6
```
- Parent ≥ Children
- Optimal untuk mencari elemen maksimum
- Operasi: insert, deleteMax, heapifyUp, heapifyDown

## Time Complexity

| Operasi | Min-Heap | Max-Heap |
|---------|----------|----------|
| Insert | O(log n) | O(log n) |
| Delete (by ID) | O(n) | O(n) |
| Get Sorted | O(n log n) | O(n log n) |
| Search | O(n) | O(n) |

## Space Complexity
- O(n) untuk menyimpan n elemen dalam heap

## Cara Menjalankan

### Versi Java (javaX.java)

#### Prasyarat
- Java Development Kit (JDK) versi 11 atau lebih baru
- Java compiler (`javac`) dan Java runtime (`java`)

#### Compile dan Run
```bash
# Navigasi ke direktori heap
cd heap

# Compile
javac javaX.java

# Run
java javaX
```

#### Contoh Output
```
════════════════════════════════════════════════════════════
       PROGRAM SISTEM MANAJEMEN DATA BARANG - HEAP
════════════════════════════════════════════════════════════
  Menggunakan Min-Heap dan Max-Heap
════════════════════════════════════════════════════════════

╔════════════════════════════════════════════════════════════╗
║       SISTEM MANAJEMEN DATA BARANG - MIN-HEAP & MAX-HEAP   ║
╠════════════════════════════════════════════════════════════╣
║  1. Tambah Data (Insert)                                   ║
║  2. Tampilkan Data (Ascending - Min-Heap)                  ║
║  3. Tampilkan Data (Descending - Max-Heap)                 ║
║  4. Hapus Data dari Min-Heap                               ║
║  5. Hapus Data dari Max-Heap                               ║
║  6. Tambah Data dari File CSV                              ║
║  0. Keluar                                                 ║
╚════════════════════════════════════════════════════════════╝
Pilih menu (0-6): 
```

### Versi Python (python.py)

#### Prasyarat
- Python 3.6 atau lebih baru
- Module: `csv` (built-in)

#### Run
```bash
# Navigasi ke direktori heap
cd heap

# Run
python3 python.py
```

#### Contoh Output
```
═══════════════════════════════════════════════════════════
      PROGRAM SISTEM MANAJEMEN DATA BARANG - HEAP
═══════════════════════════════════════════════════════════
  Menggunakan Min-Heap dan Max-Heap
═══════════════════════════════════════════════════════════

╔════════════════════════════════════════════════════════════╗
║       SISTEM MANAJEMEN DATA BARANG - MIN-HEAP & MAX-HEAP   ║
╠════════════════════════════════════════════════════════════╣
║  1. Tambah Data (Insert)                                   ║
║  2. Tampilkan Data (Ascending - Min-Heap)                  ║
║  3. Tampilkan Data (Descending - Max-Heap)                 ║
║  4. Hapus Data dari Min-Heap                               ║
║  5. Hapus Data dari Max-Heap                               ║
║  6. Tambah Data dari File CSV                              ║
║  0. Keluar                                                 ║
╚════════════════════════════════════════════════════════════╝
Pilih menu (0-6): 
```

## Menu Program

### 1. Tambah Data (Insert)
- Input: ID Barang dan Nama Barang
- Fungsi: Menambahkan data ke Min-Heap dan Max-Heap sekaligus
- Validasi: Mencegah duplikasi ID

### 2. Tampilkan Data (Ascending - Min-Heap)
- Menampilkan semua data urut berdasarkan ID dari kecil ke besar
- Menggunakan Min-Heap untuk sorting

### 3. Tampilkan Data (Descending - Max-Heap)
- Menampilkan semua data urut berdasarkan ID dari besar ke kecil
- Menggunakan Max-Heap untuk sorting

### 4. Hapus Data dari Min-Heap
- Input: ID Barang yang akan dihapus
- Mencari dan menghapus elemen dengan ID yang sesuai dari Min-Heap

### 5. Hapus Data dari Max-Heap
- Input: ID Barang yang akan dihapus
- Mencari dan menghapus elemen dengan ID yang sesuai dari Max-Heap

### 6. Tambah Data dari File CSV
- Input: Nama file CSV (default: data.csv)
- Format file: ID,Nama (dengan header)
- Fungsi: Membaca data dari file dan menambahkan ke kedua heap

## Format File CSV

File CSV harus memiliki format berikut:
```csv
ID,Nama
5288,pensil
5993,pulpen
8689,penghapus
...
```

- Header: `ID,Nama` (wajib ada)
- Setiap baris: `<ID>,<Nama>`
- ID harus berupa angka (integer)
- Nama dapat berisi spasi

## Contoh Penggunaan

### Input Manual
```
Pilih menu (0-6): 1
Masukkan ID Barang: 1001
Masukkan Nama Barang: Buku
✓ Data berhasil ditambahkan ke Min-Heap dan Max-Heap!

Pilih menu (0-6): 1
Masukkan ID Barang: 1005
Masukkan Nama Barang: Pensil
✓ Data berhasil ditambahkan ke Min-Heap dan Max-Heap!

Pilih menu (0-6): 2
(Menampilkan data ascending)
```

### Load dari CSV
```
Pilih menu (0-6): 6
Masukkan nama file CSV (default: data.csv): data.csv

Membaca file CSV...
✓ Berhasil menambahkan 29 data dari file CSV

Pilih menu (0-6): 2
```

## Struktur File

```
heap/
├── javaX.java          # Implementasi Java
├── python.py           # Implementasi Python
├── data.csv            # File data untuk load
├── README.md           # File dokumentasi (ini)
└── PROMPTS.txt         # File prompt/panduan
```

## Implementasi Detail

### Heapify Up (Insert)
- Memasukkan elemen baru di akhir heap
- Membandingkan dengan parent
- Menukar jika melanggar heap property
- Ulangi sampai heap property terpenuhi

### Heapify Down (Delete)
- Menghapus root dan mengganti dengan elemen terakhir
- Membandingkan dengan children
- Menukar dengan child yang sesuai (Min-Heap: terkecil, Max-Heap: terbesar)
- Ulangi sampai heap property terpenuhi

### Get Sorted
- Membuat copy dari heap
- Ekstrak root secara berulang
- Heapify down setiap kali ekstrak
- Menghasilkan array yang terurut

## Catatan Penting

1. **Duplikasi ID**: Program mencegah penambahan data dengan ID yang sudah ada
2. **Error Handling**: Program menangani error pada input dan pembacaan file
3. **Validasi CSV**: Program melakukan validasi format CSV sebelum membaca
4. **Dual Heap**: Data selalu ditambahkan/dihapus dari kedua heap secara konsisten

## Pengembangan Lebih Lanjut

Fitur yang dapat ditambahkan:
1. Update data (replace)
2. Search data berdasarkan ID atau Nama
3. Export data ke CSV
4. Visualisasi heap structure
5. Statistik heap (height, level, dll)
6. Perbandingan performance antara Min-Heap dan Max-Heap

## Referensi

- Heap data structure: https://en.wikipedia.org/wiki/Binary_heap
- Heap operations: https://www.geeksforgeeks.org/binary-heap/
- Time complexity analysis: https://www.geeksforgeeks.org/binary-heap-operations/

## Lisensi

Program ini dibuat untuk tujuan pendidikan.

---

**Dibuat pada**: 2024
**Versi**: 1.0
