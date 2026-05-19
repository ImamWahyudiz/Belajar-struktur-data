# Hash Table - Manajemen Data Numerik

Implementasi struktur data **Hash Table** dengan **Separate Chaining** untuk menyimpan himpunan data numerik unik. Program mendukung operasi Insert, Delete, dan Search.

---

## Deskripsi Program

Program menyimpan sekumpulan angka integer dalam Hash Table. Saat program dijalankan, **100 angka random unik** (range 1000–9999) sudah langsung dimasukkan secara otomatis.

### Hash Function

```
h(key) = |key| % TABLE_SIZE
```

- **Metode**: Modulo Division  
- **TABLE_SIZE**: 151 (bilangan prima — meminimalisasi collision)  
- `abs()` digunakan agar angka negatif tetap menghasilkan index valid

### Teknik Collision: Separate Chaining

**Separate Chaining** (Open Hashing) adalah teknik di mana setiap bucket menyimpan **Linked List**. Ketika dua nilai memiliki hash yang sama (collision), keduanya dirangkai dalam satu chain di bucket yang sama.

```
Bucket[  5]:  1409 -> 5
Bucket[ 12]:  3467
Bucket[ 18]:  2873 -> 169
```

Elemen baru selalu disisipkan di **depan chain** (prepend) — O(1).

---

## Kompleksitas Waktu

| Operasi | Rata-rata | Terburuk |
|---------|-----------|----------|
| Insert  | O(1)      | O(n)     |
| Delete  | O(1)      | O(n)     |
| Search  | O(1)      | O(n)     |

Kasus terburuk terjadi jika semua elemen masuk ke bucket yang sama.

**Load Factor** = `count / TABLE_SIZE`  
Load factor ideal < 1.0 untuk menjaga performa O(1).

---

## Struktur Kode

### Java (`javaX.java`)

```
package hashtable;

class Node          → Satu node dalam chain (data + next pointer)
class HashTable     → Implementasi hash table dengan Separate Chaining
    + hashFunction()    → h(key) = |key| % 151
    + insert()          → Tambah data, cek duplikat, prepend ke chain
    + delete()          → Hapus data, traverse chain, putus pointer
    + search()          → Cari data, kembalikan (found, bucket, posisi)
    + display()         → Tampilkan semua data terurut ascending
    + displayInternal() → Tampilkan struktur bucket per bucket
class javaX         → Main class (entry point)
    + generateRandomData() → Pre-load 100 angka random unik
    + displayMenu()        → Tampilkan menu interaktif
    + main()               → Loop program utama
```

### Python (`python.py`)

```
class Node          → Satu node dalam chain (data + next pointer)
class HashTable     → Implementasi hash table dengan Separate Chaining
    hash_function() → h(key) = |key| % 151
    insert()        → Tambah data, cek duplikat, prepend ke chain
    delete()        → Hapus data, traverse chain, putus pointer
    search()        → Cari data, kembalikan (found, bucket, posisi)
    display()       → Tampilkan semua data terurut ascending
    display_internal() → Tampilkan struktur bucket per bucket
generate_random_data() → Pre-load 100 angka random unik
display_menu()         → Tampilkan menu interaktif
main()                 → Loop program utama
```

---

## Fitur Program

| No | Fitur | Deskripsi |
|----|-------|-----------|
| 1  | **Input Data** | Masukkan angka baru ke hash table (duplikat ditolak) |
| 2  | **Hapus Data** | Hapus angka dari hash table |
| 3  | **Cari Data** | Cari angka, tampilkan bucket & posisi chain jika ditemukan |
| 4  | **Tampilkan Semua Data** | Lihat semua angka terurut ascending beserta statistik |
| 5  | **Visualisasi Internal** | Lihat isi setiap bucket dan chain-nya |
| 0  | **Keluar** | Keluar dari program |

---

## Cara Menjalankan

### Java

```bash
# Dari folder root project (Belajar-struktur-data/)
javac -encoding UTF-8 hashtable/javaX.java -d bin
java -cp bin hashtable.javaX
```

### Python

```bash
# Dari folder root project atau dari dalam folder hashtable/
python hashtable/python.py
# atau
cd hashtable
python python.py
```

---

## Contoh Output

```
════════════════════════════════════════════════════════════
       PROGRAM HASH TABLE - HIMPUNAN DATA NUMERIK
════════════════════════════════════════════════════════════
  Teknik Collision : Separate Chaining (Open Hashing)
  Hash Function    : h(key) = |key| % 151
  Ukuran Tabel     : 151 bucket (bilangan prima)
════════════════════════════════════════════════════════════

  Generating 100 data random unik (range 1000-9999)...
  ✓ 100 data random berhasil diinputkan!

Pilih menu (0-5): 3
Masukkan angka yang akan dicari: 4523
✓ Angka 4523 DITEMUKAN dalam hash table!
  → Posisi bucket  : 47
  → Posisi di chain: ke-1
```

---

## Bahasa yang Digunakan

- **Java** — OpenJDK 17+
- **Python** — Python 3.9+
