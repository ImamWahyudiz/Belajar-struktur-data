#!/usr/bin/env python3
"""
Program Manajemen Data Mahasiswa
Implementasi struktur data Array dengan operasi insert dan delete

Program ini mendemonstrasikan penggunaan array (list) dengan kapasitas tetap
untuk menyimpan dan mengelola data mahasiswa dengan berbagai operasi.
"""


class Mahasiswa:
    """
    Class untuk merepresentasikan data mahasiswa
    
    Attributes:
        nim (str): Nomor Induk Mahasiswa
        nama (str): Nama lengkap mahasiswa
    """
    
    def __init__(self, nim, nama):
        """
        Constructor untuk membuat objek Mahasiswa baru
        
        Args:
            nim (str): Nomor Induk Mahasiswa
            nama (str): Nama lengkap mahasiswa
        """
        self.nim = nim
        self.nama = nama
    
    def __str__(self):
        """
        String representation dari objek Mahasiswa
        
        Returns:
            str: Format string "NIM: xxx, Nama: xxx"
        """
        return f"NIM: {self.nim}, Nama: {self.nama}"


class MahasiswaApp:
    """
    Class untuk manajemen array mahasiswa dengan kapasitas tetap
    
    Implementasi struktur data array yang mendukung berbagai operasi:
    - Insert: beginning, position, end
    - Delete: beginning, position, end, by NIM
    - Display: menampilkan semua data
    
    Array menggunakan kapasitas tetap (fixed-size) dan tidak bisa di-resize.
    
    Attributes:
        CAPACITY (int): Kapasitas maksimal array (10 elemen)
        data (list): List untuk menyimpan objek Mahasiswa
        count (int): Jumlah elemen yang terisi dalam array
    """
    
    CAPACITY = 10  # Kapasitas maksimal array (fixed)
    
    def __init__(self):
        """
        Constructor - Inisialisasi array dengan kapasitas tetap
        Array diinisialisasi dengan None dan count dimulai dari 0
        """
        self.data = [None] * self.CAPACITY  # Array dengan kapasitas tetap
        self.count = 0  # Jumlah elemen yang terisi
    
    def insert_at_beginning(self, nim, nama):
        """
        Insert data mahasiswa di awal array (index 0)
        Time Complexity: O(n) - karena perlu shift semua elemen ke kanan
        
        Args:
            nim (str): NIM mahasiswa yang akan ditambahkan
            nama (str): Nama mahasiswa yang akan ditambahkan
        """
        # Validasi: cek apakah array sudah penuh
        if self.count >= self.CAPACITY:
            print("Array penuh! Tidak bisa menambah data.")
            return
        
        # Shift semua elemen ke kanan untuk memberi ruang di index 0
        # Mulai dari elemen terakhir agar tidak tertimpa
        for i in range(self.count, 0, -1):
            self.data[i] = self.data[i - 1]
        
        # Masukkan data baru di posisi pertama
        self.data[0] = Mahasiswa(nim, nama)
        self.count += 1  # Increment jumlah elemen
        print("Data berhasil ditambahkan di awal!")
    
    def insert_at_position(self, position, nim, nama):
        """
        Insert data mahasiswa di posisi tertentu
        Time Complexity: O(n) - karena perlu shift elemen dari posisi tersebut
        
        Args:
            position (int): Posisi index dimana data akan disisipkan (0 sampai count)
            nim (str): NIM mahasiswa yang akan ditambahkan
            nama (str): Nama mahasiswa yang akan ditambahkan
        """
        # Validasi: cek apakah posisi valid
        if position < 0 or position > self.count:
            print(f"Posisi tidak valid! (0 - {self.count})")
            return
        
        # Validasi: cek apakah array sudah penuh
        if self.count >= self.CAPACITY:
            print("Array penuh! Tidak bisa menambah data.")
            return
        
        # Shift elemen dari posisi yang diinginkan ke kanan
        for i in range(self.count, position, -1):
            self.data[i] = self.data[i - 1]
        
        # Masukkan data baru di posisi yang diminta
        self.data[position] = Mahasiswa(nim, nama)
        self.count += 1
        print(f"Data berhasil ditambahkan di posisi {position}!")
    
    def insert_at_end(self, nim, nama):
        """
        Insert data mahasiswa di akhir array
        Time Complexity: O(1) - tidak perlu shift, langsung tambah di belakang
        
        Args:
            nim (str): NIM mahasiswa yang akan ditambahkan
            nama (str): Nama mahasiswa yang akan ditambahkan
        """
        # Validasi: cek apakah array sudah penuh
        if self.count >= self.CAPACITY:
            print("Array penuh! Tidak bisa menambah data.")
            return
        
        # Langsung masukkan data di posisi count (akhir)
        self.data[self.count] = Mahasiswa(nim, nama)
        self.count += 1
        print("Data berhasil ditambahkan di akhir!")
    
    def delete_from_beginning(self):
        """
        Delete data mahasiswa dari awal array (index 0)
        Time Complexity: O(n) - karena perlu shift semua elemen ke kiri
        """
        # Validasi: cek apakah array kosong
        if self.count == 0:
            print("Array kosong! Tidak ada data yang dihapus.")
            return
        
        # Shift semua elemen ke kiri untuk menimpa elemen pertama
        for i in range(self.count - 1):
            self.data[i] = self.data[i + 1]
        
        # Set posisi terakhir jadi None untuk cleanup
        self.data[self.count - 1] = None
        self.count -= 1  # Decrement jumlah elemen
        print("Data berhasil dihapus dari awal!")
    
    def delete_from_position(self, position):
        """
        Delete data mahasiswa dari posisi tertentu
        Time Complexity: O(n) - karena perlu shift elemen dari posisi tersebut
        
        Args:
            position (int): Posisi index yang akan dihapus (0 sampai count-1)
        """
        # Validasi: cek apakah posisi valid
        if position < 0 or position >= self.count:
            print(f"Posisi tidak valid! (0 - {self.count - 1})")
            return
        
        # Shift elemen dari posisi selanjutnya ke kiri untuk menimpa elemen yang dihapus
        for i in range(position, self.count - 1):
            self.data[i] = self.data[i + 1]
        
        # Set posisi terakhir jadi None untuk cleanup
        self.data[self.count - 1] = None
        self.count -= 1
        print(f"Data berhasil dihapus dari posisi {position}!")
    
    def delete_from_end(self):
        """
        Delete data mahasiswa dari akhir array
        Time Complexity: O(1) - tidak perlu shift, langsung hapus dari belakang
        """
        # Validasi: cek apakah array kosong
        if self.count == 0:
            print("Array kosong! Tidak ada data yang dihapus.")
            return
        
        # Set posisi terakhir jadi None dan kurangi count
        self.data[self.count - 1] = None
        self.count -= 1
        print("Data berhasil dihapus dari akhir!")
    
    def delete_first_occurrence(self, nim):
        """
        Delete kemunculan pertama dari mahasiswa dengan NIM tertentu
        Time Complexity: O(n) - perlu search linear + shift
        
        Args:
            nim (str): NIM mahasiswa yang akan dihapus
        """
        found = False
        
        # Linear search untuk mencari NIM yang cocok
        for i in range(self.count):
            if self.data[i].nim == nim:
                # Jika ditemukan, shift elemen ke kiri dari posisi tersebut
                for j in range(i, self.count - 1):
                    self.data[j] = self.data[j + 1]
                
                # Cleanup posisi terakhir
                self.data[self.count - 1] = None
                self.count -= 1
                print(f"Data dengan NIM {nim} berhasil dihapus!")
                found = True
                break  # Hanya hapus kemunculan pertama
        
        # Jika tidak ditemukan, beri notifikasi
        if not found:
            print(f"Data dengan NIM {nim} tidak ditemukan!")
    
    def show_data(self):
        """
        Menampilkan semua data mahasiswa yang ada di array
        Menampilkan jumlah data dan list semua mahasiswa dengan nomor urut
        """
        if self.count == 0:
            print("Array kosong! Tidak ada data untuk ditampilkan.")
            return
        
        print("\n========== DATA MAHASISWA ==========")
        print(f"Jumlah data: {self.count}")
        
        # Loop untuk menampilkan setiap data mahasiswa
        for i in range(self.count):
            print(f"{i + 1}. {self.data[i]}")
        print("===================================\n")
    
    def display_menu(self):
        """
        Menampilkan menu pilihan operasi yang tersedia
        """
        print("\n===== MENU MANAJEMEN DATA MAHASISWA =====")
        print("1. Tambah di awal")
        print("2. Tambah di posisi tertentu")
        print("3. Tambah di akhir")
        print("4. Hapus dari awal")
        print("5. Hapus dari posisi tertentu")
        print("6. Hapus dari akhir")
        print("7. Hapus berdasarkan NIM")
        print("8. Tampilkan data")
        print("9. Keluar")
        print("========================================")


def main():
    """
    Fungsi utama program
    Menjalankan loop utama untuk menerima input user dan memproses pilihan menu
    """
    app = MahasiswaApp()  # Inisialisasi sistem
    
    # Header program
    print("=== PROGRAM MANAJEMEN DATA MAHASISWA ===")
    print(f"Kapasitas Array: {MahasiswaApp.CAPACITY}")
    print("=========================================\n")
    
    # Loop utama program
    while True:
        app.display_menu()
        
        try:
            choice = int(input("Pilih menu (1-9): "))
            
            # Proses pilihan user
            if choice == 1:  # Insert at beginning
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                app.insert_at_beginning(nim, nama)
            
            elif choice == 2:  # Insert at position
                position = int(input(f"Masukkan posisi (0 - {app.count}): "))
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                app.insert_at_position(position, nim, nama)
            
            elif choice == 3:  # Insert at end
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                app.insert_at_end(nim, nama)
            
            elif choice == 4:  # Delete from beginning
                app.delete_from_beginning()
            
            elif choice == 5:  # Delete from position
                position = int(input(f"Masukkan posisi (0 - {app.count - 1}): "))
                app.delete_from_position(position)
            
            elif choice == 6:  # Delete from end
                app.delete_from_end()
            
            elif choice == 7:  # Delete by NIM
                nim = input("Masukkan NIM yang ingin dihapus: ")
                app.delete_first_occurrence(nim)
            
            elif choice == 8:  # Show all data
                app.show_data()
            
            elif choice == 9:  # Exit program
                print("Terima kasih! Program selesai.")
                break
            
            else:
                print("Menu tidak valid! Silakan pilih menu 1-9.")
        
        except ValueError:
            # Error handling untuk input bukan angka
            print("Input tidak valid! Masukkan angka.")
        except KeyboardInterrupt:
            # Error handling untuk Ctrl+C
            print("\n\nProgram dihentikan.")
            break


if __name__ == "__main__":
    # Entry point program
    main()
