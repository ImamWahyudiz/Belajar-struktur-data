#!/usr/bin/env python3
"""
Program Manajemen Data Mahasiswa
Implementasi struktur data Singly LinkedList tanpa library

Program ini mendemonstrasikan penggunaan Singly LinkedList (single linked list)
untuk menyimpan dan mengelola data mahasiswa dengan berbagai operasi insert dan delete.
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


class Node:
    """
    Class untuk merepresentasikan satu node dalam LinkedList
    
    Attributes:
        data (Mahasiswa): Data mahasiswa yang disimpan dalam node
        next (Node): Pointer ke node berikutnya
    """
    
    def __init__(self, data):
        """
        Constructor untuk membuat node baru
        
        Args:
            data (Mahasiswa): Data mahasiswa yang akan disimpan
        """
        self.data = data
        self.next = None


class MahasiswaLinkedList:
    """
    Class untuk manajemen Singly LinkedList mahasiswa
    
    Implementasi struktur data Singly LinkedList yang mendukung berbagai operasi:
    - Insert: beginning, position, end
    - Delete: beginning, position, end, by NIM
    - Display: menampilkan semua data
    
    LinkedList ini tidak memiliki batasan kapasitas (dynamic size).
    
    Attributes:
        head (Node): Pointer ke node pertama dalam LinkedList
        count (int): Jumlah node dalam LinkedList
    """
    
    def __init__(self):
        """
        Constructor untuk inisialisasi LinkedList kosong
        """
        self.head = None
        self.count = 0
    
    def insert_at_beginning(self, nim, nama):
        """
        Menambahkan data mahasiswa di awal LinkedList
        Time Complexity: O(1)
        
        Args:
            nim (str): Nomor Induk Mahasiswa
            nama (str): Nama lengkap mahasiswa
        """
        mahasiswa = Mahasiswa(nim, nama)
        new_node = Node(mahasiswa)
        
        new_node.next = self.head
        self.head = new_node
        self.count += 1
        
        print("✓ Data berhasil ditambahkan di awal!")
        print(f"  Total data: {self.count}")
    
    def insert_at_position(self, position, nim, nama):
        """
        Menambahkan data mahasiswa di posisi tertentu (1-based index)
        Time Complexity: O(n)
        
        Args:
            position (int): Posisi untuk insert (1 sampai count+1)
            nim (str): Nomor Induk Mahasiswa
            nama (str): Nama lengkap mahasiswa
        """
        # Validasi posisi
        if position < 1 or position > self.count + 1:
            print("✗ Error: Posisi tidak valid!")
            print(f"  Posisi harus antara 1 sampai {self.count + 1}")
            return
        
        # Jika posisi 1, gunakan insert_at_beginning
        if position == 1:
            self.insert_at_beginning(nim, nama)
            return
        
        mahasiswa = Mahasiswa(nim, nama)
        new_node = Node(mahasiswa)
        
        current = self.head
        # Traverse ke posisi sebelum target
        for i in range(1, position - 1):
            current = current.next
        
        new_node.next = current.next
        current.next = new_node
        self.count += 1
        
        print(f"✓ Data berhasil ditambahkan di posisi {position}!")
        print(f"  Total data: {self.count}")
    
    def insert_at_end(self, nim, nama):
        """
        Menambahkan data mahasiswa di akhir LinkedList
        Time Complexity: O(n)
        
        Args:
            nim (str): Nomor Induk Mahasiswa
            nama (str): Nama lengkap mahasiswa
        """
        mahasiswa = Mahasiswa(nim, nama)
        new_node = Node(mahasiswa)
        
        # Jika LinkedList kosong
        if self.head is None:
            self.head = new_node
            self.count += 1
            print("✓ Data berhasil ditambahkan di akhir!")
            print(f"  Total data: {self.count}")
            return
        
        # Traverse ke node terakhir
        current = self.head
        while current.next is not None:
            current = current.next
        
        current.next = new_node
        self.count += 1
        
        print("✓ Data berhasil ditambahkan di akhir!")
        print(f"  Total data: {self.count}")
    
    def delete_from_beginning(self):
        """
        Menghapus data mahasiswa dari awal LinkedList
        Time Complexity: O(1)
        """
        if self.head is None:
            print("✗ Error: LinkedList kosong!")
            return
        
        deleted_data = self.head.data
        self.head = self.head.next
        self.count -= 1
        
        print("✓ Data berhasil dihapus dari awal!")
        print(f"  Data yang dihapus: {deleted_data}")
        print(f"  Total data: {self.count}")
    
    def delete_at_position(self, position):
        """
        Menghapus data mahasiswa dari posisi tertentu (1-based index)
        Time Complexity: O(n)
        
        Args:
            position (int): Posisi untuk delete (1 sampai count)
        """
        if self.head is None:
            print("✗ Error: LinkedList kosong!")
            return
        
        # Validasi posisi
        if position < 1 or position > self.count:
            print("✗ Error: Posisi tidak valid!")
            print(f"  Posisi harus antara 1 sampai {self.count}")
            return
        
        # Jika posisi 1, hapus dari awal
        if position == 1:
            self.delete_from_beginning()
            return
        
        current = self.head
        # Traverse ke posisi sebelum target
        for i in range(1, position - 1):
            current = current.next
        
        deleted_data = current.next.data
        current.next = current.next.next
        self.count -= 1
        
        print(f"✓ Data berhasil dihapus dari posisi {position}!")
        print(f"  Data yang dihapus: {deleted_data}")
        print(f"  Total data: {self.count}")
    
    def delete_from_end(self):
        """
        Menghapus data mahasiswa dari akhir LinkedList
        Time Complexity: O(n)
        """
        if self.head is None:
            print("✗ Error: LinkedList kosong!")
            return
        
        # Jika hanya ada satu node
        if self.head.next is None:
            deleted_data = self.head.data
            self.head = None
            self.count -= 1
            print("✓ Data berhasil dihapus dari akhir!")
            print(f"  Data yang dihapus: {deleted_data}")
            print(f"  Total data: {self.count}")
            return
        
        # Traverse ke node sebelum terakhir
        current = self.head
        while current.next.next is not None:
            current = current.next
        
        deleted_data = current.next.data
        current.next = None
        self.count -= 1
        
        print("✓ Data berhasil dihapus dari akhir!")
        print(f"  Data yang dihapus: {deleted_data}")
        print(f"  Total data: {self.count}")
    
    def delete_by_nim(self, nim):
        """
        Menghapus data mahasiswa berdasarkan NIM (first occurrence)
        Time Complexity: O(n)
        
        Args:
            nim (str): NIM mahasiswa yang akan dihapus
        """
        if self.head is None:
            print("✗ Error: LinkedList kosong!")
            return
        
        # Jika data yang dicari ada di head
        if self.head.data.nim == nim:
            deleted_data = self.head.data
            self.head = self.head.next
            self.count -= 1
            print(f"✓ Data mahasiswa dengan NIM {nim} berhasil dihapus!")
            print(f"  Data yang dihapus: {deleted_data}")
            print(f"  Total data: {self.count}")
            return
        
        # Cari node dengan NIM yang sesuai
        current = self.head
        while current.next is not None:
            if current.next.data.nim == nim:
                deleted_data = current.next.data
                current.next = current.next.next
                self.count -= 1
                print(f"✓ Data mahasiswa dengan NIM {nim} berhasil dihapus!")
                print(f"  Data yang dihapus: {deleted_data}")
                print(f"  Total data: {self.count}")
                return
            current = current.next
        
        print(f"✗ Error: Mahasiswa dengan NIM {nim} tidak ditemukan!")
    
    def show_data(self):
        """
        Menampilkan semua data mahasiswa dalam LinkedList
        Time Complexity: O(n)
        """
        if self.head is None:
            print("LinkedList kosong!")
            return
        
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║              DAFTAR DATA MAHASISWA                         ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print(f"  Total data: {self.count}")
        print("────────────────────────────────────────────────────────────")
        
        current = self.head
        index = 1
        while current is not None:
            print(f"  [{index}] {current.data}")
            current = current.next
            index += 1
        
        print("╚════════════════════════════════════════════════════════════╝\n")
    
    def display_menu(self):
        """
        Menampilkan menu utama program
        """
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║     SISTEM MANAJEMEN DATA MAHASISWA - LINKED LIST          ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print("║  1. Insert at beginning                                    ║")
        print("║  2. Insert at given position                               ║")
        print("║  3. Insert at end                                          ║")
        print("║  4. Delete from beginning                                  ║")
        print("║  5. Delete given position                                  ║")
        print("║  6. Delete from end                                        ║")
        print("║  7. Delete first occurrence (by NIM)                       ║")
        print("║  8. Show data                                              ║")
        print("║  9. Exit                                                   ║")
        print("╚════════════════════════════════════════════════════════════╝")
    
    def run(self):
        """
        Method utama untuk menjalankan program
        """
        while True:
            self.display_menu()
            
            try:
                choice = int(input("Pilih menu (1-9): "))
                
                if choice == 1:  # Insert at beginning
                    nim = input("Masukkan NIM: ")
                    nama = input("Masukkan Nama: ")
                    self.insert_at_beginning(nim, nama)
                    
                elif choice == 2:  # Insert at given position
                    position = int(input(f"Masukkan Posisi (1-{self.count + 1}): "))
                    nim = input("Masukkan NIM: ")
                    nama = input("Masukkan Nama: ")
                    self.insert_at_position(position, nim, nama)
                    
                elif choice == 3:  # Insert at end
                    nim = input("Masukkan NIM: ")
                    nama = input("Masukkan Nama: ")
                    self.insert_at_end(nim, nama)
                    
                elif choice == 4:  # Delete from beginning
                    self.delete_from_beginning()
                    
                elif choice == 5:  # Delete given position
                    position = int(input(f"Masukkan Posisi (1-{self.count}): "))
                    self.delete_at_position(position)
                    
                elif choice == 6:  # Delete from end
                    self.delete_from_end()
                    
                elif choice == 7:  # Delete first occurrence by NIM
                    nim = input("Masukkan NIM yang akan dihapus: ")
                    self.delete_by_nim(nim)
                    
                elif choice == 8:  # Show data
                    self.show_data()
                    
                elif choice == 9:  # Exit
                    print("\n╔════════════════════════════════════════════════════════════╗")
                    print("║  Terima kasih telah menggunakan program ini!               ║")
                    print("╚════════════════════════════════════════════════════════════╝")
                    break
                    
                else:
                    print("✗ Pilihan tidak valid! Silakan pilih 1-9.")
                    
            except ValueError:
                print("✗ Input tidak valid! Silakan masukkan angka.")
            except Exception as e:
                print(f"✗ Terjadi kesalahan: {e}")


def main():
    """
    Function main - entry point program
    """
    app = MahasiswaLinkedList()
    app.run()


if __name__ == "__main__":
    main()
