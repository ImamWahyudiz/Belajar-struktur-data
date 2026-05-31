# Matrix - Visualisasi Operasi Matriks

Program ini dibuat untuk memvisualisasikan operasi-operasi pada sebuah matriks dua dimensi. Terdiri dari antarmuka interaktif yang memiliki fitur animasi *"step-by-step"* yang memungkinkan Anda mengamati setiap proses komputasi yang berlangsung.

---

## Deskripsi Program

Program ini mengimplementasikan struktur 2D array (Matriks) yang akan disorot sel per sel berdasarkan jenis operasi yang Anda pilih. Disediakan implementasi pada dua bahasa yaitu Java (menggunakan `javax.swing`) dan Python (menggunakan `tkinter`).

---

## Fitur dan Menu Operasi

| No | Operasi | Keterangan |
|----|---------|------------|
| 1-a | **Sort matrix row-wise** | Mengurutkan nilai dari elemen terkecil hingga terbesar di tiap-tiap baris. |
| 1-b | **Sort matrix column-wise** | Mengurutkan nilai dari elemen terkecil hingga terbesar di tiap-tiap kolom. |
| 2-a | **Rotate Clockwise by 1** | Memutar elemen satu langkah searah jarum jam berdasarkan cincin batas (rings). |
| 2-b | **Rotate Counter-Clockwise by 1** | Memutar elemen satu langkah berlawanan arah jarum jam berdasarkan cincin batas. |
| 2-c | **Rotate by 90** | Memutar keseluruhan matriks searah jarum jam sejauh 90 derajat. |
| 2-d | **Rotate by 180** | Memutar keseluruhan matriks searah jarum jam sejauh 180 derajat. |
| 3-a | **Row-wise traversal** | Menelusuri dan menyorot elemen dari kiri ke kanan baris per baris. |
| 3-b | **Column-wise traversal** | Menelusuri elemen dari atas ke bawah kolom per kolom. |
| 4 | **Spiral traversal** | Menelusuri seluruh elemen matriks dalam lintasan spiral. |
| 5 | **Transpose matrix** | Mengubah setiap baris matriks menjadi kolom (dan sebaliknya). |
| 6 | **Quit** | Keluar dari program. |

---

## Cara Menjalankan

### Versi Java
Pastikan Java JDK (minimal JDK 8) telah ter-install di perangkat Anda.
```bash
# Buka terminal dan arahkan ke direktori root (Belajar-struktur-data/)
# Lakukan kompilasi
javac matrix/javaX.java

# Jalankan program
java matrix.javaX
```

### Versi Python
Pastikan Python 3 sudah ter-install (beserta modul tkinter bawaan sistem).
```bash
# Buka terminal dan jalankan file python.py
python matrix/python.py
```

---

## Fitur Animasi Interaktif
- **Control Bar**: Program menyediakan tombol **Start**, **Pause**, **Step**, dan **Reset** agar Anda leluasa mengendalikan laju animasi dari setiap algoritma.
- **Log Panel**: Terdapat log tertulis (narasi langkah) yang muncul di pojok untuk menjelaskan proses penukaran variabel (swapping) maupun operasi indeks yang sedang terjadi di belakang layar.
- **Matrix Dimension Customization**: Anda bisa membuat bentuk matriks dengan berbagai dimensi baris dan kolom yang bervariasi lalu men-generate ulang isi matriks tersebut menggunakan nilai acak.
