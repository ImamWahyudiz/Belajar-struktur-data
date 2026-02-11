# Program Manajemen Data Mahasiswa - LinkedList

## Deskripsi
Program ini adalah implementasi struktur data **Singly LinkedList** dengan berbagai operasi insert dan delete untuk manajemen data mahasiswa. Program tersedia dalam dua versi: **Java** dan **Python**.

**Implementasi ini tidak menggunakan library LinkedList bawaan** - semua operasi linked list diimplementasikan secara manual untuk tujuan pembelajaran.

## Fitur Program

### Operasi Insert (Tambah Data)
1. **Insert at Beginning** - Menambahkan data di awal linked list
2. **Insert at Given Position** - Menambahkan data di posisi tertentu (1 sampai count+1)
3. **Insert at End** - Menambahkan data di akhir linked list

### Operasi Delete (Hapus Data)
4. **Delete from Beginning** - Menghapus data dari awal linked list
5. **Delete from Given Position** - Menghapus data dari posisi tertentu (1 sampai count)
6. **Delete from End** - Menghapus data dari akhir linked list
7. **Delete First Occurrence** - Menghapus data berdasarkan NIM tertentu

### Operasi Lainnya
8. **Show Data** - Menampilkan semua data mahasiswa
9. **Exit** - Keluar dari program

## Spesifikasi

- **Struktur Data**: Singly LinkedList (single linked list)
- **Kapasitas**: Dynamic (tidak terbatas, sesuai memori tersedia)
- **Data Mahasiswa**: NIM (String) dan Nama (String)
- **Validasi**: Program melakukan validasi posisi untuk insert/delete
- **Count Tracking**: Variabel count selalu up-to-date dengan jumlah node

## Karakteristik Singly LinkedList

### Kelebihan
- **Dynamic Size**: Tidak memiliki batasan ukuran tetap
- **Efficient Insertion/Deletion**: Insert/delete di awal sangat cepat O(1)
- **Memory Efficient**: Hanya mengalokasikan memori sesuai kebutuhan

### Kekurangan
- **Sequential Access**: Harus traverse dari head untuk akses data tertentu
- **Extra Memory**: Memerlukan extra memori untuk pointer
- **No Backward Traversal**: Hanya bisa traverse ke depan (single direction)

### Time Complexity
| Operasi | Complexity |
|---------|-----------|
| Insert at Beginning | O(1) |
| Insert at Position | O(n) |
| Insert at End | O(n) |
| Delete from Beginning | O(1) |
| Delete at Position | O(n) |
| Delete from End | O(n) |
| Delete by NIM | O(n) |
| Display All | O(n) |

## Cara Menjalankan

### Versi Java (javaX.java)

#### Prasyarat
- Java Development Kit (JDK) versi 11 atau lebih baru
- Java compiler (`javac`) dan Java runtime (`java`)

#### Compile dan Run
```bash
# Dari dalam folder linkedlist
cd linkedlist
javac javaX.java
java javaX
```

### Versi Python (python.py)

#### Prasyarat
- Python 3.6 atau lebih baru

#### Run Langsung
```bash
# Run dari root directory
python3 linkedlist/python.py

# Atau jika berada di dalam folder linkedlist
cd linkedlist
python3 python.py

# Atau dengan shebang (Linux/Mac)
chmod +x python.py
./python.py
```

## Struktur Kode

### Java
```
linkedlist/javaX.java
├── class Mahasiswa        # Data class untuk mahasiswa
├── class Node            # Node class untuk linked list
└── class javaX           # Main class dengan operasi linked list
    ├── insertAtBeginning()
    ├── insertAtPosition()
    ├── insertAtEnd()
    ├── deleteFromBeginning()
    ├── deleteAtPosition()
    ├── deleteFromEnd()
    ├── deleteByNim()
    ├── showData()
    └── run()
```

### Python
```
linkedlist/python.py
├── class Mahasiswa                  # Data class untuk mahasiswa
├── class Node                       # Node class untuk linked list
└── class MahasiswaLinkedList       # Main class dengan operasi linked list
    ├── insert_at_beginning()
    ├── insert_at_position()
    ├── insert_at_end()
    ├── delete_from_beginning()
    ├── delete_at_position()
    ├── delete_from_end()
    ├── delete_by_nim()
    ├── show_data()
    └── run()
```

## Contoh Penggunaan

### Contoh Sesi Program

```
╔════════════════════════════════════════════════════════════╗
║     SISTEM MANAJEMEN DATA MAHASISWA - LINKED LIST          ║
╠════════════════════════════════════════════════════════════╣
║  1. Insert at beginning                                    ║
║  2. Insert at given position                               ║
║  3. Insert at end                                          ║
║  4. Delete from beginning                                  ║
║  5. Delete given position                                  ║
║  6. Delete from end                                        ║
║  7. Delete first occurrence (by NIM)                       ║
║  8. Show data                                              ║
║  9. Exit                                                   ║
╚════════════════════════════════════════════════════════════╝
Pilih menu (1-9): 1
Masukkan NIM: 12345
Masukkan Nama: John Doe
✓ Data berhasil ditambahkan di awal!
  Total data: 1

Pilih menu (1-9): 3
Masukkan NIM: 67890
Masukkan Nama: Jane Smith
✓ Data berhasil ditambahkan di akhir!
  Total data: 2

Pilih menu (1-9): 8

╔════════════════════════════════════════════════════════════╗
║              DAFTAR DATA MAHASISWA                         ║
╠════════════════════════════════════════════════════════════╣
  Total data: 2
────────────────────────────────────────────────────────────
  [1] NIM: 12345, Nama: John Doe
  [2] NIM: 67890, Nama: Jane Smith
╚════════════════════════════════════════════════════════════╝
```

## Konsep Linked List

### Struktur Node
```
┌─────────────────────┐
│  Node               │
├─────────────────────┤
│  data: Mahasiswa    │
│  next: Node         │
└─────────────────────┘
```

### Visualisasi Singly LinkedList
```
head
  │
  ▼
┌──────┐    ┌──────┐    ┌──────┐    ┌──────┐
│ Node │───▶│ Node │───▶│ Node │───▶│ Node │───▶ null
└──────┘    └──────┘    └──────┘    └──────┘
  Data1       Data2       Data3       Data4
```

### Insert at Beginning
```
Sebelum:  head -> [A] -> [B] -> [C] -> null
Sesudah:  head -> [X] -> [A] -> [B] -> [C] -> null
```

### Insert at Position (misal posisi 3)
```
Sebelum:  head -> [A] -> [B] -> [C] -> [D] -> null
Sesudah:  head -> [A] -> [B] -> [X] -> [C] -> [D] -> null
```

### Delete from End
```
Sebelum:  head -> [A] -> [B] -> [C] -> [D] -> null
Sesudah:  head -> [A] -> [B] -> [C] -> null
```

## Perbedaan dengan Array

| Aspek | Array | Linked List |
|-------|-------|-------------|
| Ukuran | Fixed (tetap) | Dynamic (fleksibel) |
| Akses Data | O(1) - random access | O(n) - sequential |
| Insert Awal | O(n) - perlu shift | O(1) - langsung |
| Insert Akhir | O(1) jika ada space | O(n) - perlu traverse |
| Memory | Contiguous block | Scattered nodes |
| Memory Overhead | Minimal | Extra (pointer) |

## Validasi dan Error Handling

Program melakukan validasi untuk:
- ✓ Posisi insert harus antara 1 sampai count+1
- ✓ Posisi delete harus antara 1 sampai count
- ✓ Operasi delete pada linked list kosong
- ✓ Input data yang valid
- ✓ NIM yang tidak ditemukan saat delete by NIM

## Catatan Penting

1. **Tidak Menggunakan Library**
   - Java: Tidak menggunakan `java.util.LinkedList`
   - Python: Tidak menggunakan `collections.deque` atau library lainnya
   - Semua operasi diimplementasikan manual

2. **Count Management**
   - Setiap operasi insert: `count++` atau `count += 1`
   - Setiap operasi delete: `count--` atau `count -= 1`
   - Count selalu akurat mencerminkan jumlah node

3. **Index Convention**
   - Menggunakan 1-based indexing untuk user interface
   - Posisi 1 = node pertama (head)
   - Posisi count = node terakhir (tail)

4. **Memory Management**
   - Java: Garbage collector otomatis membersihkan node yang tidak terpakai
   - Python: Reference counting otomatis membersihkan object

## Pembelajaran

Program ini cocok untuk:
- Memahami konsep dasar Linked List
- Mempelajari pointer/reference manipulation
- Memahami perbedaan array vs linked list
- Praktik implementasi struktur data dari nol
- Memahami time complexity berbagai operasi

## Author

Program ini dibuat untuk keperluan pembelajaran Struktur Data.

## Lisensi

Program ini dibuat untuk tujuan edukasi dan pembelajaran.
