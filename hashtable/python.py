#!/usr/bin/env python3
"""
Program Manajemen Data Numerik
Implementasi Hash Table dengan Separate Chaining

Program ini mendemonstrasikan penggunaan Hash Table menggunakan
Separate Chaining sebagai teknik collision handling untuk menyimpan
himpunan data numerik unik.

Saat program dijalankan, 100 angka random unik sudah diinputkan
secara otomatis.

Hash Function : h(key) = |key| % TABLE_SIZE (Modulo Division)
Collision Tech : Separate Chaining (Open Hashing)
TABLE_SIZE     : 151 (bilangan prima)
"""

import random
import sys

# Paksa stdout menggunakan UTF-8 agar karakter box-drawing tampil benar di Windows
if sys.stdout.encoding.lower() != 'utf-8':
    sys.stdout.reconfigure(encoding='utf-8')


class Node:
    """
    Class untuk merepresentasikan satu node dalam chain (Linked List)
    Digunakan sebagai entry dalam setiap bucket Hash Table

    Attributes:
        data (int): Nilai numerik yang disimpan
        next (Node): Pointer ke node berikutnya dalam chain
    """

    def __init__(self, data):
        """
        Constructor untuk membuat node baru

        Args:
            data (int): Nilai numerik yang akan disimpan
        """
        self.data = data
        self.next = None


class HashTable:
    """
    Class HashTable - Implementasi Hash Table dengan Separate Chaining

    Separate Chaining adalah teknik collision handling di mana setiap bucket
    berupa Linked List. Elemen-elemen dengan hash value yang sama dirangkai
    dalam satu chain di bucket yang sama.

    Hash Function : h(key) = |key| % TABLE_SIZE  (Modulo Division)
    Collision Tech : Separate Chaining (Open Hashing)

    Kompleksitas Waktu (rata-rata):
        Insert : O(1)
        Delete : O(1)
        Search : O(1)
    Kompleksitas Waktu (terburuk, semua elemen satu bucket):
        Insert : O(n)
        Delete : O(n)
        Search : O(n)

    Attributes:
        TABLE_SIZE (int): Ukuran tabel hash - bilangan prima (151)
        table (list): Array of Node, masing-masing adalah head chain
        count (int): Jumlah elemen yang tersimpan
    """

    TABLE_SIZE = 151  # Bilangan prima untuk distribusi yang lebih merata

    def __init__(self):
        """
        Constructor - Inisialisasi hash table kosong
        Semua bucket diinisialisasi dengan None (chain kosong)
        """
        self.table = [None] * self.TABLE_SIZE
        self.count = 0

    def hash_function(self, key):
        """
        Hash Function menggunakan metode Modulo Division
        h(key) = |key| % TABLE_SIZE

        Menggunakan abs() agar angka negatif menghasilkan index valid.

        Args:
            key (int): Nilai kunci yang akan di-hash

        Returns:
            int: Index bucket (0 hingga TABLE_SIZE-1)
        """
        return abs(key) % self.TABLE_SIZE

    def insert(self, value):
        """
        Insert data ke dalam hash table
        Jika collision terjadi, elemen baru disisipkan di depan chain (prepend).
        Time Complexity: O(1) average, O(n) worst case

        Args:
            value (int): Nilai numerik yang akan dimasukkan

        Returns:
            bool: True jika berhasil, False jika nilai sudah ada (duplikat)
        """
        index = self.hash_function(value)

        # Traversal chain untuk cek duplikat
        current = self.table[index]
        while current is not None:
            if current.data == value:
                return False  # Duplikat - tolak
            current = current.next

        # Sisipkan di depan chain (prepend) - O(1)
        new_node = Node(value)
        new_node.next = self.table[index]
        self.table[index] = new_node
        self.count += 1
        return True

    def delete(self, value):
        """
        Hapus data dari hash table
        Time Complexity: O(1) average, O(n) worst case

        Args:
            value (int): Nilai numerik yang akan dihapus

        Returns:
            bool: True jika berhasil dihapus, False jika tidak ditemukan
        """
        index = self.hash_function(value)
        current = self.table[index]
        prev = None

        # Traversal chain untuk mencari nilai
        while current is not None:
            if current.data == value:
                # Putus sambungan node dari chain
                if prev is None:
                    self.table[index] = current.next  # Hapus head chain
                else:
                    prev.next = current.next           # Bypass node yang dihapus
                self.count -= 1
                return True
            prev = current
            current = current.next

        return False  # Tidak ditemukan

    def search(self, value):
        """
        Cari data dalam hash table
        Time Complexity: O(1) average, O(n) worst case

        Args:
            value (int): Nilai numerik yang dicari

        Returns:
            tuple: (bool found, int bucket_index, int chain_position)
                   chain_position = 0 jika tidak ditemukan
        """
        index = self.hash_function(value)
        current = self.table[index]
        position = 1  # Posisi dalam chain (1-based)

        while current is not None:
            if current.data == value:
                return True, index, position  # Ditemukan
            current = current.next
            position += 1

        return False, index, 0  # Tidak ditemukan

    def display(self):
        """
        Tampilkan semua data dalam hash table secara terurut ascending
        Time Complexity: O(n log n) karena sorting
        """
        values = []
        for i in range(self.TABLE_SIZE):
            current = self.table[i]
            while current is not None:
                values.append(current.data)
                current = current.next

        values.sort()
        load_factor = self.count / self.TABLE_SIZE

        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║          DAFTAR DATA NUMERIK DALAM HASH TABLE             ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print(f"  Total data  : {self.count}")
        print(f"  Table size  : {self.TABLE_SIZE}")
        print(f"  Load factor : {load_factor:.4f}")
        print("  --------------------------------------------------------")

        if not values:
            print("  Hash table kosong!")
        else:
            for i, v in enumerate(values):
                print(f"  {v:6d}", end="")
                if (i + 1) % 10 == 0:
                    print()
            if len(values) % 10 != 0:
                print()

        print("╚════════════════════════════════════════════════════════════╝\n")

    def display_internal(self):
        """
        Tampilkan visualisasi internal hash table (bucket view)
        Hanya menampilkan bucket yang tidak kosong
        """
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║            VISUALISASI INTERNAL HASH TABLE                ║")
        print("╠════════════════════════════════════════════════════════════╣")

        non_empty = 0
        for i in range(self.TABLE_SIZE):
            if self.table[i] is not None:
                non_empty += 1
                print(f"  Bucket[{i:3d}]: ", end="")
                current = self.table[i]
                parts = []
                while current is not None:
                    parts.append(str(current.data))
                    current = current.next
                print(" -> ".join(parts))

        print("  --------------------------------------------------------")
        print(f"  Bucket terpakai: {non_empty} / {self.TABLE_SIZE}")
        print(f"  Bucket kosong  : {self.TABLE_SIZE - non_empty} / {self.TABLE_SIZE}")
        print("╚════════════════════════════════════════════════════════════╝\n")


def generate_random_data(ht):
    """
    Generate 100 angka random unik (range 1000-9999) dan masukkan ke hash table

    Args:
        ht (HashTable): Hash table yang akan diisi data awal
    """
    print("  Generating 100 data random unik (range 1000-9999)...")
    generated = set()

    while len(generated) < 100:
        val = random.randint(1000, 9999)
        if val not in generated:
            generated.add(val)
            ht.insert(val)

    print("  ✓ 100 data random berhasil diinputkan!\n")


def display_menu():
    """Tampilkan menu utama program"""
    print("╔════════════════════════════════════════════════════════════╗")
    print("║     SISTEM MANAJEMEN DATA NUMERIK - HASH TABLE            ║")
    print("║       Collision Technique: Separate Chaining              ║")
    print("╠════════════════════════════════════════════════════════════╣")
    print("║  1. Input Data  (Insert)                                  ║")
    print("║  2. Hapus Data  (Delete)                                  ║")
    print("║  3. Cari Data   (Search)                                  ║")
    print("║  4. Tampilkan Semua Data                                  ║")
    print("║  5. Visualisasi Internal Hash Table                       ║")
    print("║  0. Keluar                                                ║")
    print("╚════════════════════════════════════════════════════════════╝")


def main():
    """
    Fungsi utama program
    Menjalankan loop interaktif untuk operasi hash table
    """
    ht = HashTable()

    print("════════════════════════════════════════════════════════════")
    print("       PROGRAM HASH TABLE - HIMPUNAN DATA NUMERIK")
    print("════════════════════════════════════════════════════════════")
    print("  Teknik Collision : Separate Chaining (Open Hashing)")
    print("  Hash Function    : h(key) = |key| % 151")
    print("  Ukuran Tabel     : 151 bucket (bilangan prima)")
    print("════════════════════════════════════════════════════════════\n")

    # Pre-load 100 angka random unik saat program dimulai
    generate_random_data(ht)

    while True:
        display_menu()

        try:
            choice = int(input("Pilih menu (0-5): "))

            if choice == 1:  # Input Data
                val = int(input("Masukkan angka yang akan diinput: "))
                if ht.insert(val):
                    print(f"✓ Angka {val} berhasil diinputkan!")
                    print(f"  → Disimpan di bucket: {abs(val) % HashTable.TABLE_SIZE}")
                else:
                    print(f"✗ Angka {val} sudah ada dalam hash table! (duplikat ditolak)")

            elif choice == 2:  # Hapus Data
                val = int(input("Masukkan angka yang akan dihapus: "))
                if ht.delete(val):
                    print(f"✓ Angka {val} berhasil dihapus!")
                else:
                    print(f"✗ Angka {val} tidak ditemukan dalam hash table!")

            elif choice == 3:  # Cari Data
                val = int(input("Masukkan angka yang akan dicari: "))
                found, bucket, pos = ht.search(val)
                if found:
                    print(f"✓ Angka {val} DITEMUKAN dalam hash table!")
                    print(f"  → Posisi bucket  : {bucket}")
                    print(f"  → Posisi di chain: ke-{pos}")
                else:
                    print(f"✗ Angka {val} TIDAK DITEMUKAN dalam hash table!")
                    print(f"  → Dicek di bucket: {bucket} (kosong / tidak cocok)")

            elif choice == 4:  # Tampilkan semua data
                ht.display()

            elif choice == 5:  # Visualisasi internal
                ht.display_internal()

            elif choice == 0:  # Keluar
                print("\n╔════════════════════════════════════════════════════════════╗")
                print("║  Terima kasih telah menggunakan program ini!               ║")
                print("╚════════════════════════════════════════════════════════════╝")
                break

            else:
                print("✗ Pilihan tidak valid! Silakan pilih 0-5.")

        except ValueError:
            print("✗ Input tidak valid! Masukkan angka bulat.")
        except KeyboardInterrupt:
            print("\n\nProgram dihentikan.")
            break


if __name__ == "__main__":
    main()
