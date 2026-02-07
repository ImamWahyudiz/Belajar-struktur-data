#!/usr/bin/env python3
"""
Program Manajemen Data Mahasiswa
Implementasi Array dengan operasi insert dan delete
"""

class Mahasiswa:
    """Class untuk menyimpan data mahasiswa"""
    def __init__(self, nim, nama):
        self.nim = nim
        self.nama = nama
    
    def __str__(self):
        return f"NIM: {self.nim}, Nama: {self.nama}"


class MahasiswaApp:
    """Class untuk manajemen array mahasiswa"""
    
    CAPACITY = 10
    
    def __init__(self):
        self.data = [None] * self.CAPACITY
        self.count = 0
    
    def insert_at_beginning(self, nim, nama):
        """Insert data di awal array"""
        if self.count >= self.CAPACITY:
            print("Array penuh! Tidak bisa menambah data.")
            return
        
        # Shift semua elemen ke kanan
        for i in range(self.count, 0, -1):
            self.data[i] = self.data[i - 1]
        
        self.data[0] = Mahasiswa(nim, nama)
        self.count += 1
        print("Data berhasil ditambahkan di awal!")
    
    def insert_at_position(self, position, nim, nama):
        """Insert data di posisi tertentu"""
        if position < 0 or position > self.count:
            print(f"Posisi tidak valid! (0 - {self.count})")
            return
        
        if self.count >= self.CAPACITY:
            print("Array penuh! Tidak bisa menambah data.")
            return
        
        # Shift elemen dari posisi ke kanan
        for i in range(self.count, position, -1):
            self.data[i] = self.data[i - 1]
        
        self.data[position] = Mahasiswa(nim, nama)
        self.count += 1
        print(f"Data berhasil ditambahkan di posisi {position}!")
    
    def insert_at_end(self, nim, nama):
        """Insert data di akhir array"""
        if self.count >= self.CAPACITY:
            print("Array penuh! Tidak bisa menambah data.")
            return
        
        self.data[self.count] = Mahasiswa(nim, nama)
        self.count += 1
        print("Data berhasil ditambahkan di akhir!")
    
    def delete_from_beginning(self):
        """Delete data dari awal array"""
        if self.count == 0:
            print("Array kosong! Tidak ada data yang dihapus.")
            return
        
        # Shift semua elemen ke kiri
        for i in range(self.count - 1):
            self.data[i] = self.data[i + 1]
        
        self.data[self.count - 1] = None
        self.count -= 1
        print("Data berhasil dihapus dari awal!")
    
    def delete_from_position(self, position):
        """Delete data dari posisi tertentu"""
        if position < 0 or position >= self.count:
            print(f"Posisi tidak valid! (0 - {self.count - 1})")
            return
        
        # Shift elemen dari posisi ke kiri
        for i in range(position, self.count - 1):
            self.data[i] = self.data[i + 1]
        
        self.data[self.count - 1] = None
        self.count -= 1
        print(f"Data berhasil dihapus dari posisi {position}!")
    
    def delete_from_end(self):
        """Delete data dari akhir array"""
        if self.count == 0:
            print("Array kosong! Tidak ada data yang dihapus.")
            return
        
        self.data[self.count - 1] = None
        self.count -= 1
        print("Data berhasil dihapus dari akhir!")
    
    def delete_first_occurrence(self, nim):
        """Delete kemunculan pertama berdasarkan NIM"""
        found = False
        for i in range(self.count):
            if self.data[i].nim == nim:
                # Shift elemen dari posisi ke kiri
                for j in range(i, self.count - 1):
                    self.data[j] = self.data[j + 1]
                
                self.data[self.count - 1] = None
                self.count -= 1
                print(f"Data dengan NIM {nim} berhasil dihapus!")
                found = True
                break
        
        if not found:
            print(f"Data dengan NIM {nim} tidak ditemukan!")
    
    def show_data(self):
        """Tampilkan semua data"""
        if self.count == 0:
            print("Array kosong! Tidak ada data untuk ditampilkan.")
            return
        
        print("\n========== DATA MAHASISWA ==========")
        print(f"Jumlah data: {self.count}")
        for i in range(self.count):
            print(f"{i + 1}. {self.data[i]}")
        print("===================================\n")
    
    def display_menu(self):
        """Tampilkan menu"""
        print("\n===== MENU MANAJEMEN DATA MAHASISWA =====")
        print("1. Insert at beginning")
        print("2. Insert at given position")
        print("3. Insert at end")
        print("4. Delete from beginning")
        print("5. Delete given position")
        print("6. Delete from end")
        print("7. Delete first occurence")
        print("8. Show data")
        print("9. Exit")
        print("========================================")


def main():
    """Fungsi utama program"""
    app = MahasiswaApp()
    
    print("=== PROGRAM MANAJEMEN DATA MAHASISWA ===")
    print(f"Kapasitas Array: {MahasiswaApp.CAPACITY}")
    print("=========================================\n")
    
    while True:
        app.display_menu()
        
        try:
            choice = int(input("Pilih menu (1-9): "))
            
            if choice == 1:
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                app.insert_at_beginning(nim, nama)
            
            elif choice == 2:
                position = int(input(f"Masukkan posisi (0 - {app.count}): "))
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                app.insert_at_position(position, nim, nama)
            
            elif choice == 3:
                nim = input("Masukkan NIM: ")
                nama = input("Masukkan Nama: ")
                app.insert_at_end(nim, nama)
            
            elif choice == 4:
                app.delete_from_beginning()
            
            elif choice == 5:
                position = int(input(f"Masukkan posisi (0 - {app.count - 1}): "))
                app.delete_from_position(position)
            
            elif choice == 6:
                app.delete_from_end()
            
            elif choice == 7:
                nim = input("Masukkan NIM yang ingin dihapus: ")
                app.delete_first_occurrence(nim)
            
            elif choice == 8:
                app.show_data()
            
            elif choice == 9:
                print("Terima kasih! Program selesai.")
                break
            
            else:
                print("Menu tidak valid! Silakan pilih menu 1-9.")
        
        except ValueError:
            print("Input tidak valid! Masukkan angka.")
        except KeyboardInterrupt:
            print("\n\nProgram dihentikan.")
            break


if __name__ == "__main__":
    main()
