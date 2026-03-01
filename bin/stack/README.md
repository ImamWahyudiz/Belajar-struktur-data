# Program Evaluasi Ekspresi Aritmatika dengan Stack

## Deskripsi
Program ini adalah implementasi **Stack** untuk evaluasi ekspresi aritmatika. Program mengkonversi ekspresi **infix** ke **postfix** (Reverse Polish Notation), kemudian mengevaluasi ekspresi postfix tersebut. Semua proses ditampilkan **step by step** untuk tujuan pembelajaran. Program tersedia dalam dua versi: **Java** dan **Python**.

**Implementasi ini tidak menggunakan library Stack bawaan** - Stack diimplementasikan secara manual menggunakan array/list.

## Fitur Program

### Input
- **Ekspresi Infix**: Format matematika standar (contoh: `3 + 5 * 2` atau `(3 + 5) * 2`)
- **Operator yang Didukung**: `+`, `-`, `*`, `/`, `^` (pangkat), `(`, `)`
- **Operand**: Angka integer atau desimal, bisa multi-digit

### Output
1. **Postfix Expression**: Hasil konversi dari infix ke postfix
2. **Step by Step Konversi**: Setiap langkah proses konversi infix ke postfix
3. **Step by Step Evaluasi**: Setiap langkah proses evaluasi postfix
4. **Hasil Akhir**: Nilai numerik hasil perhitungan

### Contoh Output
```
╔════════════════════════════════════════════════════════════╗
║          KONVERSI INFIX KE POSTFIX (Step by Step)         ║
╠════════════════════════════════════════════════════════════╣
  Infix Expression: 3 + 5 * 2
────────────────────────────────────────────────────────────
  Step | Simbol | Stack           | Postfix
────────────────────────────────────────────────────────────
  1    | 3      | []              | 3
  2    | +      | [+]             | 3
  3    | 5      | [+]             | 3 5
  4    | *      | [+, *]          | 3 5
  5    | 2      | [+, *]          | 3 5 2
  6    | pop    | [+]             | 3 5 2 *
  7    | pop    | []              | 3 5 2 * +
╚════════════════════════════════════════════════════════════╝

╔════════════════════════════════════════════════════════════╗
║          EVALUASI POSTFIX (Step by Step)                  ║
╠════════════════════════════════════════════════════════════╣
  Postfix Expression: 3 5 2 * +
────────────────────────────────────────────────────────────
  Step | Token  | Operasi           | Stack
────────────────────────────────────────────────────────────
  1    | 3      | Push 3            | ['3']
  2    | 5      | Push 5            | ['3', '5']
  3    | 2      | Push 2            | ['3', '5', '2']
  4    | *      | 5.00 * 2.00 = 10.00 | [10.0]
  5    | +      | 3.00 + 10.00 = 13.00 | [13.0]
╚════════════════════════════════════════════════════════════╝

╔════════════════════════════════════════════════════════════╗
║                    HASIL AKHIR                             ║
╠════════════════════════════════════════════════════════════╣
  3 + 5 * 2 = 13.00
╚════════════════════════════════════════════════════════════╝
```

## Konsep Stack

### Definisi
**Stack** adalah struktur data linear yang mengikuti prinsip **LIFO (Last In First Out)** - elemen yang terakhir masuk akan menjadi yang pertama keluar.

### Operasi Dasar Stack
```
┌─────────┐
│    5    │ ← Top (Push/Pop di sini)
├─────────┤
│    3    │
├─────────┤
│    7    │
├─────────┤
│    2    │
└─────────┘
```

| Operasi | Deskripsi | Kompleksitas |
|---------|-----------|--------------|
| **Push** | Menambahkan elemen ke top | O(1) |
| **Pop** | Menghapus elemen dari top | O(1) |
| **Peek/Top** | Melihat elemen top tanpa menghapus | O(1) |
| **isEmpty** | Cek apakah stack kosong | O(1) |
| **isFull** | Cek apakah stack penuh (array-based) | O(1) |

## Algoritma Konversi Infix ke Postfix

### Aturan Konversi
1. **Operand** (angka): Langsung tambahkan ke output
2. **Operator**: 
   - Jika stack kosong atau top adalah `(`, push operator
   - Jika precedence operator baru > top stack, push operator
   - Jika precedence operator baru ≤ top stack, pop top ke output, lalu push operator baru
3. **Tanda Kurung**:
   - `(`: Push ke stack
   - `)`: Pop semua operator sampai ketemu `(`, buang `(`

### Precedence (Prioritas) Operator
| Operator | Precedence | Asosiatif |
|----------|-----------|-----------|
| `^` | 3 (tertinggi) | Kanan ke kiri |
| `*`, `/` | 2 | Kiri ke kanan |
| `+`, `-` | 1 (terendah) | Kiri ke kanan |

### Contoh Konversi
```
Infix:    3 + 5 * 2
Postfix:  3 5 2 * +

Infix:    (3 + 5) * 2
Postfix:  3 5 + 2 *

Infix:    2 ^ 3 ^ 2
Postfix:  2 3 2 ^ ^
```

## Algoritma Evaluasi Postfix

### Aturan Evaluasi
1. Scan ekspresi postfix dari kiri ke kanan
2. **Jika operand**: Push ke stack
3. **Jika operator**: 
   - Pop dua operand dari stack (operand2 lalu operand1)
   - Hitung: operand1 operator operand2
   - Push hasil ke stack
4. Hasil akhir adalah elemen terakhir di stack

### Contoh Evaluasi
```
Postfix: 3 5 2 * +

Step 1: Push 3       → Stack: [3]
Step 2: Push 5       → Stack: [3, 5]
Step 3: Push 2       → Stack: [3, 5, 2]
Step 4: Pop 2, 5     → Stack: [3]
        5 * 2 = 10
        Push 10      → Stack: [3, 10]
Step 5: Pop 10, 3    → Stack: []
        3 + 10 = 13
        Push 13      → Stack: [13]

Hasil: 13
```

## Cara Menjalankan

### Versi Java (javaX.java)

#### Prasyarat
- Java Development Kit (JDK) versi 11 atau lebih baru

#### Compile dan Run
```bash
# Compile dari parent directory
javac stack/javaX.java

# Run dari parent directory
java stack.javaX
```

### Versi Python (python.py)

#### Prasyarat
- Python 3.6 atau lebih baru

#### Run Langsung
```bash
# Run dari root directory
python3 stack/python.py

# Atau jika berada di dalam folder stack
cd stack
python3 python.py

# Atau dengan shebang (Linux/Mac)
chmod +x python.py
./python.py
```

## Struktur Kode

### Java
```
stack/javaX.java
├── class Stack                      # Stack implementation (array-based)
│   ├── push()                       # Push item ke stack
│   ├── pop()                        # Pop item dari stack
│   ├── peek()                       # Lihat top tanpa pop
│   ├── isEmpty()                    # Cek stack kosong
│   ├── isFull()                     # Cek stack penuh
│   └── getContent()                 # Get stack content as string
├── class ExpressionEvaluator        # Main evaluator class
│   ├── precedence()                 # Tentukan prioritas operator
│   ├── isOperator()                 # Cek apakah operator
│   ├── infixToPostfix()            # Konversi infix ke postfix
│   ├── evaluatePostfix()           # Evaluasi postfix
│   ├── isNumeric()                  # Cek apakah angka
│   └── applyOperator()              # Aplikasikan operator
└── class javaX                      # Main program
    └── main()                       # Entry point
```

### Python
```
stack/python.py
├── class Stack                      # Stack implementation (list-based)
│   ├── push()                       # Push item ke stack
│   ├── pop()                        # Pop item dari stack
│   ├── peek()                       # Lihat top tanpa pop
│   ├── is_empty()                   # Cek stack kosong
│   ├── is_full()                    # Cek stack penuh
│   └── get_content()                # Get stack content as string
├── class ExpressionEvaluator        # Main evaluator class
│   ├── precedence()                 # Tentukan prioritas operator
│   ├── is_operator()                # Cek apakah operator
│   ├── infix_to_postfix()          # Konversi infix ke postfix
│   ├── evaluate_postfix()          # Evaluasi postfix
│   ├── is_numeric()                 # Cek apakah angka
│   └── apply_operator()             # Aplikasikan operator
└── main()                           # Entry point
```

## Contoh Penggunaan

### Contoh 1: Ekspresi Sederhana
```
Input:  3 + 5 * 2
Output: 
  Postfix: 3 5 2 * +
  Hasil: 13.00
```

### Contoh 2: Dengan Tanda Kurung
```
Input:  (3 + 5) * 2
Output:
  Postfix: 3 5 + 2 *
  Hasil: 16.00
```

### Contoh 3: Dengan Pangkat
```
Input:  2 ^ 3 + 5
Output:
  Postfix: 2 3 ^ 5 +
  Hasil: 13.00
```

### Contoh 4: Ekspresi Kompleks
```
Input:  (3 + 5) * (2 - 1) / 4
Output:
  Postfix: 3 5 + 2 1 - * 4 /
  Hasil: 2.00
```

## Notasi Ekspresi

### Infix Notation (Standar)
Operator berada di antara operand
```
Contoh: A + B * C
        (A + B) * (C - D)
```

### Postfix Notation (RPN - Reverse Polish Notation)
Operator berada setelah operand
```
Contoh: A B + C *
        A B C * +
```

### Prefix Notation (Polish Notation)
Operator berada sebelum operand
```
Contoh: + A * B C
        * + A B - C D
```

### Perbandingan
| Infix | Postfix | Prefix |
|-------|---------|--------|
| `A + B` | `A B +` | `+ A B` |
| `A + B * C` | `A B C * +` | `+ A * B C` |
| `(A + B) * C` | `A B + C *` | `* + A B C` |

## Keunggulan Postfix

1. **Tidak Perlu Tanda Kurung**: Urutan operasi sudah jelas
2. **Mudah Dievaluasi**: Bisa dievaluasi dalam satu scan (left-to-right)
3. **Efisien**: Cocok untuk komputer dan calculator
4. **No Ambiguity**: Tidak ada ambiguitas dalam urutan operasi

## Aplikasi Stack dalam Dunia Nyata

1. **Compiler**: Parsing dan evaluasi ekspresi
2. **Calculator**: Calculator RPN (HP, scientific calculators)
3. **Browser**: Back button (history stack)
4. **Undo/Redo**: Text editors, photo editors
5. **Function Call**: Call stack dalam programming
6. **Syntax Checking**: Bracket matching, HTML/XML validation
7. **Recursion**: Conversion to iterative using stack

## Kompleksitas Algoritma

### Konversi Infix ke Postfix
- **Time Complexity**: O(n) - scan setiap karakter sekali
- **Space Complexity**: O(n) - worst case semua operator di stack

### Evaluasi Postfix
- **Time Complexity**: O(n) - scan setiap token sekali
- **Space Complexity**: O(n) - worst case semua operand di stack

## Validasi dan Error Handling

Program melakukan handling untuk:
- ✓ Ekspresi kosong
- ✓ Operator tidak valid
- ✓ Pembagian dengan nol
- ✓ Ekspresi tidak valid (missing operand/operator)
- ✓ Stack overflow/underflow
- ✓ Format angka tidak valid

## Catatan Penting

1. **Tidak Menggunakan Library**
   - Java: Tidak menggunakan `java.util.Stack`
   - Python: Tidak menggunakan `collections.deque`
   - Stack diimplementasikan manual dengan array/list

2. **Operator Precedence**
   - Operator dengan precedence lebih tinggi dievaluasi terlebih dahulu
   - Tanda kurung memiliki prioritas tertinggi

3. **Asosiatif**
   - `+`, `-`, `*`, `/`: Left-to-right
   - `^`: Right-to-left (2^3^2 = 2^(3^2) = 2^9 = 512)

4. **Multi-digit Numbers**
   - Program mendukung angka multi-digit (contoh: 123, 45.67)
   - Spasi digunakan sebagai separator

## Pembelajaran

Program ini cocok untuk:
- Memahami konsep Stack dan operasinya
- Mempelajari konversi infix ke postfix
- Memahami evaluasi ekspresi dengan stack
- Praktik implementasi algoritma klasik
- Memahami operator precedence dan associativity
- Aplikasi stack dalam compiler design

## Tips Penggunaan

1. **Gunakan Spasi**: Pisahkan angka dan operator dengan spasi untuk clarity
2. **Tanda Kurung**: Gunakan tanda kurung untuk mengubah urutan evaluasi
3. **Multi-digit**: Program mendukung angka seperti 123 atau 45.67
4. **Step by Step**: Perhatikan setiap step untuk memahami proses

## Referensi

- Data Structures and Algorithms in Java (Robert Lafore)
- Introduction to Algorithms (CLRS)
- The Art of Computer Programming (Donald Knuth)

## Author

Program ini dibuat untuk keperluan pembelajaran Struktur Data.

## Lisensi

Program ini dibuat untuk tujuan edukasi dan pembelajaran.
