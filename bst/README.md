# Program Binary Search Tree (BST)

## Deskripsi
Program ini adalah implementasi struktur data Binary Search Tree (BST) dengan berbagai operasi untuk manajemen data barang. Program tersedia dalam dua versi: **Java** dan **Python**.

## Fitur Program

### Operasi Insert (Tambah Data)
1. **Insert/Tambah Data** - Menambahkan data barang (ID dan Nama) ke dalam BST

### Operasi Search (Cari Data)
2. **Search/Cari Data** - Mencari data barang berdasarkan ID tertentu

### Operasi Delete (Hapus Data)
3. **Delete/Hapus Data** - Menghapus data barang berdasarkan ID tertentu
   - Menangani kasus: node leaf, node dengan satu child, node dengan dua children

### Operasi Traversal
4. **Inorder Traversal** - Menampilkan data dalam urutan ascending/sorted (Left-Root-Right)
5. **Preorder Traversal** - Menampilkan data dengan root terlebih dahulu (Root-Left-Right)
6. **Postorder Traversal** - Menampilkan data dengan root terakhir (Left-Right-Root)

### Operasi Lainnya
7. **Display All Data** - Menampilkan semua data dalam tree (inorder)
8. **Show Tree Statistics** - Menampilkan informasi tree (jumlah node, height, dsb)
9. **Exit** - Keluar dari program

## Spesifikasi

- **Data Barang**: ID (Integer) dan Nama (String)
- **Struktur**: Binary Search Tree dengan self-balancing capability
- **Validasi**: Program melakukan validasi duplikasi ID dan struktur tree

## Cara Menjalankan

### Versi Java (javaX.java)

#### Prasyarat
- Java Development Kit (JDK) versi 11 atau lebih baru
- Java compiler (`javac`) dan Java runtime (`java`)

#### Compile dan Run
```bash
# Compile
javac bst/javaX.java

# Run
java bst.javaX
```

#### Atau jika berada di dalam folder bst:
```bash
cd bst
javac javaX.java
java javaX
```

### Versi Python (python.py)

#### Prasyarat
- Python versi 3.6 atau lebih baru
- Bash shell (untuk run script)

#### Cara Menjalankan
```bash
# Langsung jalankan
python3 bst/python.py

# Atau jika berada di dalam folder bst:
cd bst
python3 python.py
```

## Input Data

### Format Data
Data input menggunakan format ID (integer) dan Nama (string):
```
ID: 5288, Nama: pensil
ID: 5993, Nama: pulpen
ID: 2156, Nama: penggaris
...
```

### Contoh Penggunaan
```
========== MENU BINARY SEARCH TREE ==========
1. Tambah Data
2. Cari Data
3. Hapus Data
4. Traversal (Inorder)
5. Traversal (Preorder)
6. Traversal (Postorder)
7. Tampilkan Semua Data
8. Informasi Tree
0. Keluar

Pilih menu (0-8): 1
Masukkan ID Barang: 5288
Masukkan Nama Barang: pensil
Data berhasil ditambahkan!
```

## Kompleksitas Algoritma

### Best Case (Balanced Tree)
- Insert: O(log n)
- Search: O(log n)
- Delete: O(log n)
- Traversal: O(n)

### Worst Case (Skewed Tree)
- Insert: O(n)
- Search: O(n)
- Delete: O(n)
- Traversal: O(n)

## Catatan Penting
- Program menghindari duplikasi ID
- Operasi delete menghandle 3 kasus: leaf node, node dengan 1 child, node dengan 2 children
- Tree dapat menampilkan informasi statistik seperti tinggi dan jumlah node
