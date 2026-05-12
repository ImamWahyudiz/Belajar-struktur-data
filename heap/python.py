#!/usr/bin/env python3
"""
Program Manajemen Data Barang
Implementasi struktur data Heap (Min-Heap dan Max-Heap)

Program ini mendemonstrasikan penggunaan Heap untuk menyimpan dan mengelola data barang
dengan operasi: insert, delete, dan display (sorted ascending/descending berdasarkan ID).
"""

import csv
import os


class Barang:
    """
    Class untuk merepresentasikan data barang
    
    Attributes:
        id (int): ID Barang
        nama (str): Nama barang
    """
    
    def __init__(self, id, nama):
        """
        Constructor untuk membuat objek Barang baru
        
        Args:
            id (int): ID Barang
            nama (str): Nama barang
        """
        self.id = id
        self.nama = nama
    
    def __str__(self):
        """
        String representation dari objek Barang
        
        Returns:
            str: Format string "ID: xxx, Nama: xxx"
        """
        return f"ID: {self.id}, Nama: {self.nama}"
    
    def __lt__(self, other):
        """
        Operator < untuk membandingkan berdasarkan ID
        
        Args:
            other (Barang): Barang lain untuk dibandingkan
        
        Returns:
            bool: True jika ID ini lebih kecil
        """
        return self.id < other.id
    
    def __gt__(self, other):
        """
        Operator > untuk membandingkan berdasarkan ID
        
        Args:
            other (Barang): Barang lain untuk dibandingkan
        
        Returns:
            bool: True jika ID ini lebih besar
        """
        return self.id > other.id
    
    def __eq__(self, other):
        """
        Operator == untuk membandingkan berdasarkan ID
        
        Args:
            other (Barang): Barang lain untuk dibandingkan
        
        Returns:
            bool: True jika ID sama
        """
        return self.id == other.id


class MinHeap:
    """
    Class untuk implementasi Min-Heap
    
    Min-Heap adalah struktur data binary tree yang memenuhi min-heap property:
    - Parent node <= Children nodes
    - Root adalah elemen terkecil
    
    Direpresentasikan menggunakan array (list) dengan:
    - Index 0: root
    - Index i: node parent
    - Index 2*i+1: left child
    - Index 2*i+2: right child
    
    Attributes:
        heap (list): List yang menyimpan elemen-elemen dalam heap
    """
    
    def __init__(self):
        """
        Constructor - Inisialisasi Min-Heap kosong
        """
        self.heap = []
    
    def parent(self, i):
        """
        Mendapatkan index parent dari node di index i
        
        Args:
            i (int): Index node
        
        Returns:
            int: Index parent
        """
        return (i - 1) // 2
    
    def left_child(self, i):
        """
        Mendapatkan index left child dari node di index i
        
        Args:
            i (int): Index node
        
        Returns:
            int: Index left child
        """
        return 2 * i + 1
    
    def right_child(self, i):
        """
        Mendapatkan index right child dari node di index i
        
        Args:
            i (int): Index node
        
        Returns:
            int: Index right child
        """
        return 2 * i + 2
    
    def swap(self, i, j):
        """
        Menukar dua elemen dalam heap
        
        Args:
            i (int): Index elemen pertama
            j (int): Index elemen kedua
        """
        self.heap[i], self.heap[j] = self.heap[j], self.heap[i]
    
    def heapify_up(self, i):
        """
        Memindahkan elemen ke atas untuk mempertahankan min-heap property
        Time Complexity: O(log n)
        
        Args:
            i (int): Index elemen yang akan diheapify
        """
        while i > 0 and self.heap[self.parent(i)] > self.heap[i]:
            self.swap(i, self.parent(i))
            i = self.parent(i)
    
    def heapify_down(self, i):
        """
        Memindahkan elemen ke bawah untuk mempertahankan min-heap property
        Time Complexity: O(log n)
        
        Args:
            i (int): Index elemen yang akan diheapify
        """
        smallest = i
        left = self.left_child(i)
        right = self.right_child(i)
        
        # Cari node terkecil antara node dan children-nya
        if left < len(self.heap) and self.heap[left] < self.heap[smallest]:
            smallest = left
        
        if right < len(self.heap) and self.heap[right] < self.heap[smallest]:
            smallest = right
        
        # Jika node bukan yang terkecil, tukar dan lanjutkan heapify
        if smallest != i:
            self.swap(i, smallest)
            self.heapify_down(smallest)
    
    def insert(self, barang):
        """
        Menambahkan data barang ke dalam min-heap
        Time Complexity: O(log n)
        
        Args:
            barang (Barang): Data barang yang akan ditambahkan
        """
        self.heap.append(barang)
        self.heapify_up(len(self.heap) - 1)
    
    def delete_min(self):
        """
        Menghapus elemen terkecil (root) dari min-heap
        Time Complexity: O(log n)
        
        Returns:
            Barang: Elemen yang dihapus atau None jika heap kosong
        """
        if not self.heap:
            return None
        
        if len(self.heap) == 1:
            return self.heap.pop()
        
        root = self.heap[0]
        self.heap[0] = self.heap.pop()
        self.heapify_down(0)
        
        return root
    
    def delete_by_id(self, id):
        """
        Menghapus data barang berdasarkan ID dari min-heap
        Time Complexity: O(n)
        
        Args:
            id (int): ID barang yang akan dihapus
        
        Returns:
            bool: True jika berhasil dihapus, False jika tidak ditemukan
        """
        # Cari index dari barang dengan ID yang dimaksud
        index = -1
        for i, barang in enumerate(self.heap):
            if barang.id == id:
                index = i
                break
        
        if index == -1:
            return False
        
        # Ganti dengan elemen terakhir
        self.heap[index] = self.heap.pop()
        
        # Jika elemen yang dihapus bukan elemen terakhir
        if index < len(self.heap):
            # Cek apakah perlu heapify_up atau heapify_down
            parent_idx = self.parent(index)
            if index > 0 and self.heap[index] < self.heap[parent_idx]:
                self.heapify_up(index)
            else:
                self.heapify_down(index)
        
        return True
    
    def get_sorted(self):
        """
        Mendapatkan semua elemen dalam urutan ascending (sorted)
        Time Complexity: O(n log n)
        
        Returns:
            list: List berisi semua elemen yang sudah diurutkan ascending
        """
        result = []
        # Membuat copy heap agar original tidak berubah
        temp_heap = self.heap[:]
        
        while temp_heap:
            # Simpan root (elemen terkecil)
            root = temp_heap[0]
            result.append(root)
            
            # Ganti root dengan elemen terakhir
            last = temp_heap.pop()
            
            # Jika masih ada elemen, lakukan heapify down
            if temp_heap:
                temp_heap[0] = last
                
                # Heapify down
                i = 0
                while True:
                    smallest = i
                    left = 2 * i + 1
                    right = 2 * i + 2
                    
                    if left < len(temp_heap) and temp_heap[left] < temp_heap[smallest]:
                        smallest = left
                    
                    if right < len(temp_heap) and temp_heap[right] < temp_heap[smallest]:
                        smallest = right
                    
                    if smallest != i:
                        temp_heap[i], temp_heap[smallest] = temp_heap[smallest], temp_heap[i]
                        i = smallest
                    else:
                        break
        
        return result
    
    def is_empty(self):
        """
        Cek apakah heap kosong
        
        Returns:
            bool: True jika heap kosong
        """
        return len(self.heap) == 0
    
    def size(self):
        """
        Mendapatkan jumlah elemen dalam heap
        
        Returns:
            int: Jumlah elemen
        """
        return len(self.heap)


class MaxHeap:
    """
    Class untuk implementasi Max-Heap
    
    Max-Heap adalah struktur data binary tree yang memenuhi max-heap property:
    - Parent node >= Children nodes
    - Root adalah elemen terbesar
    
    Direpresentasikan menggunakan array (list) dengan:
    - Index 0: root
    - Index i: node parent
    - Index 2*i+1: left child
    - Index 2*i+2: right child
    
    Attributes:
        heap (list): List yang menyimpan elemen-elemen dalam heap
    """
    
    def __init__(self):
        """
        Constructor - Inisialisasi Max-Heap kosong
        """
        self.heap = []
    
    def parent(self, i):
        """
        Mendapatkan index parent dari node di index i
        
        Args:
            i (int): Index node
        
        Returns:
            int: Index parent
        """
        return (i - 1) // 2
    
    def left_child(self, i):
        """
        Mendapatkan index left child dari node di index i
        
        Args:
            i (int): Index node
        
        Returns:
            int: Index left child
        """
        return 2 * i + 1
    
    def right_child(self, i):
        """
        Mendapatkan index right child dari node di index i
        
        Args:
            i (int): Index node
        
        Returns:
            int: Index right child
        """
        return 2 * i + 2
    
    def swap(self, i, j):
        """
        Menukar dua elemen dalam heap
        
        Args:
            i (int): Index elemen pertama
            j (int): Index elemen kedua
        """
        self.heap[i], self.heap[j] = self.heap[j], self.heap[i]
    
    def heapify_up(self, i):
        """
        Memindahkan elemen ke atas untuk mempertahankan max-heap property
        Time Complexity: O(log n)
        
        Args:
            i (int): Index elemen yang akan diheapify
        """
        while i > 0 and self.heap[self.parent(i)] < self.heap[i]:
            self.swap(i, self.parent(i))
            i = self.parent(i)
    
    def heapify_down(self, i):
        """
        Memindahkan elemen ke bawah untuk mempertahankan max-heap property
        Time Complexity: O(log n)
        
        Args:
            i (int): Index elemen yang akan diheapify
        """
        largest = i
        left = self.left_child(i)
        right = self.right_child(i)
        
        # Cari node terbesar antara node dan children-nya
        if left < len(self.heap) and self.heap[left] > self.heap[largest]:
            largest = left
        
        if right < len(self.heap) and self.heap[right] > self.heap[largest]:
            largest = right
        
        # Jika node bukan yang terbesar, tukar dan lanjutkan heapify
        if largest != i:
            self.swap(i, largest)
            self.heapify_down(largest)
    
    def insert(self, barang):
        """
        Menambahkan data barang ke dalam max-heap
        Time Complexity: O(log n)
        
        Args:
            barang (Barang): Data barang yang akan ditambahkan
        """
        self.heap.append(barang)
        self.heapify_up(len(self.heap) - 1)
    
    def delete_max(self):
        """
        Menghapus elemen terbesar (root) dari max-heap
        Time Complexity: O(log n)
        
        Returns:
            Barang: Elemen yang dihapus atau None jika heap kosong
        """
        if not self.heap:
            return None
        
        if len(self.heap) == 1:
            return self.heap.pop()
        
        root = self.heap[0]
        self.heap[0] = self.heap.pop()
        self.heapify_down(0)
        
        return root
    
    def delete_by_id(self, id):
        """
        Menghapus data barang berdasarkan ID dari max-heap
        Time Complexity: O(n)
        
        Args:
            id (int): ID barang yang akan dihapus
        
        Returns:
            bool: True jika berhasil dihapus, False jika tidak ditemukan
        """
        # Cari index dari barang dengan ID yang dimaksud
        index = -1
        for i, barang in enumerate(self.heap):
            if barang.id == id:
                index = i
                break
        
        if index == -1:
            return False
        
        # Ganti dengan elemen terakhir
        self.heap[index] = self.heap.pop()
        
        # Jika elemen yang dihapus bukan elemen terakhir
        if index < len(self.heap):
            # Cek apakah perlu heapify_up atau heapify_down
            parent_idx = self.parent(index)
            if index > 0 and self.heap[index] > self.heap[parent_idx]:
                self.heapify_up(index)
            else:
                self.heapify_down(index)
        
        return True
    
    def get_sorted_descending(self):
        """
        Mendapatkan semua elemen dalam urutan descending (sorted)
        Time Complexity: O(n log n)
        
        Returns:
            list: List berisi semua elemen yang sudah diurutkan descending
        """
        result = []
        # Membuat copy heap agar original tidak berubah
        temp_heap = self.heap[:]
        
        while temp_heap:
            # Simpan root (elemen terbesar)
            root = temp_heap[0]
            result.append(root)
            
            # Ganti root dengan elemen terakhir
            last = temp_heap.pop()
            
            # Jika masih ada elemen, lakukan heapify down
            if temp_heap:
                temp_heap[0] = last
                
                # Heapify down
                i = 0
                while True:
                    largest = i
                    left = 2 * i + 1
                    right = 2 * i + 2
                    
                    if left < len(temp_heap) and temp_heap[left] > temp_heap[largest]:
                        largest = left
                    
                    if right < len(temp_heap) and temp_heap[right] > temp_heap[largest]:
                        largest = right
                    
                    if largest != i:
                        temp_heap[i], temp_heap[largest] = temp_heap[largest], temp_heap[i]
                        i = largest
                    else:
                        break
        
        return result
    
    def is_empty(self):
        """
        Cek apakah heap kosong
        
        Returns:
            bool: True jika heap kosong
        """
        return len(self.heap) == 0
    
    def size(self):
        """
        Mendapatkan jumlah elemen dalam heap
        
        Returns:
            int: Jumlah elemen
        """
        return len(self.heap)


class HeapApp:
    """
    Class untuk manajemen Heap (Min-Heap dan Max-Heap)
    
    Implementasi struktur data Heap yang mendukung:
    - Insert: menambahkan data barang ke kedua heap sekaligus
    - Delete: menghapus data dari min-heap atau max-heap
    - Display: menampilkan data terurut ascending (min-heap) atau descending (max-heap)
    - Load CSV: membaca data dari file CSV
    
    Attributes:
        min_heap (MinHeap): Instance Min-Heap
        max_heap (MaxHeap): Instance Max-Heap
    """
    
    def __init__(self):
        """
        Constructor - Inisialisasi kedua heap
        """
        self.min_heap = MinHeap()
        self.max_heap = MaxHeap()
    
    def insert(self, id, nama):
        """
        Menambahkan data barang ke kedua min-heap dan max-heap sekaligus
        Time Complexity: O(log n)
        
        Args:
            id (int): ID barang
            nama (str): Nama barang
        """
        barang = Barang(id, nama)
        
        # Cek duplikasi
        for item in self.min_heap.heap:
            if item.id == id:
                print("Error: ID sudah ada dalam heap!")
                return False
        
        self.min_heap.insert(barang)
        self.max_heap.insert(Barang(id, nama))
        print("✓ Data berhasil ditambahkan ke Min-Heap dan Max-Heap!")
        return True
    
    def display_ascending(self):
        """
        Menampilkan data urut berdasarkan ID secara ascending menggunakan Min-Heap
        Time Complexity: O(n log n)
        """
        if self.min_heap.is_empty():
            print("\n✗ Heap kosong! Tidak ada data untuk ditampilkan.")
            return
        
        sorted_data = self.min_heap.get_sorted()
        
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║     DAFTAR DATA BARANG (ASCENDING - MIN-HEAP)             ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print(f"  Total data: {len(sorted_data)}")
        print("────────────────────────────────────────────────────────────")
        
        for i, barang in enumerate(sorted_data, 1):
            print(f"  [{i}] {barang}")
        
        print("╚════════════════════════════════════════════════════════════╝\n")
    
    def display_descending(self):
        """
        Menampilkan data urut berdasarkan ID secara descending menggunakan Max-Heap
        Time Complexity: O(n log n)
        """
        if self.max_heap.is_empty():
            print("\n✗ Heap kosong! Tidak ada data untuk ditampilkan.")
            return
        
        sorted_data = self.max_heap.get_sorted_descending()
        
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║    DAFTAR DATA BARANG (DESCENDING - MAX-HEAP)             ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print(f"  Total data: {len(sorted_data)}")
        print("────────────────────────────────────────────────────────────")
        
        for i, barang in enumerate(sorted_data, 1):
            print(f"  [{i}] {barang}")
        
        print("╚════════════════════════════════════════════════════════════╝\n")
    
    def delete_from_min_heap(self, id):
        """
        Menghapus data dari min-heap berdasarkan ID
        Time Complexity: O(n)
        
        Args:
            id (int): ID barang yang akan dihapus
        """
        if self.min_heap.delete_by_id(id):
            print("✓ Data berhasil dihapus dari Min-Heap!")
        else:
            print(f"✗ Data dengan ID {id} tidak ditemukan di Min-Heap!")
    
    def delete_from_max_heap(self, id):
        """
        Menghapus data dari max-heap berdasarkan ID
        Time Complexity: O(n)
        
        Args:
            id (int): ID barang yang akan dihapus
        """
        if self.max_heap.delete_by_id(id):
            print("✓ Data berhasil dihapus dari Max-Heap!")
        else:
            print(f"✗ Data dengan ID {id} tidak ditemukan di Max-Heap!")
    
    def load_from_csv(self, filename):
        """
        Load data barang dari file CSV ke kedua min-heap dan max-heap
        Format CSV: ID,Nama (dengan header)
        
        Args:
            filename (str): Path ke file CSV
        
        Returns:
            int: Jumlah data yang berhasil ditambahkan
        """
        # Cek beberapa kemungkinan lokasi file
        possible_paths = [
            filename,
            os.path.join(os.path.dirname(__file__), filename),
            os.path.join(os.path.dirname(__file__), 'heap', filename),
        ]
        
        actual_path = None
        for path in possible_paths:
            if os.path.exists(path):
                actual_path = path
                break
        
        if actual_path is None:
            print(f"Error: File '{filename}' tidak ditemukan!")
            print(f"Lokasi yang dicari:")
            for path in possible_paths:
                print(f"  - {os.path.abspath(path)}")
            return 0
        
        success_count = 0
        error_count = 0
        
        try:
            with open(actual_path, 'r', encoding='utf-8') as file:
                csv_reader = csv.DictReader(file)
                
                if csv_reader.fieldnames is None or 'ID' not in csv_reader.fieldnames or 'Nama' not in csv_reader.fieldnames:
                    print("Error: Format CSV tidak sesuai. Header harus berisi 'ID' dan 'Nama'")
                    return 0
                
                print("\nMembaca file CSV...")
                for row in csv_reader:
                    try:
                        id_barang = int(row['ID'].strip())
                        nama_barang = row['Nama'].strip()
                        
                        if not nama_barang:
                            print(f"  Warning: Row dengan ID {id_barang} memiliki nama kosong, dilewati.")
                            error_count += 1
                            continue
                        
                        # Cek duplikasi
                        duplicate = False
                        for item in self.min_heap.heap:
                            if item.id == id_barang:
                                print(f"  Warning: ID {id_barang} sudah ada, dilewati.")
                                duplicate = True
                                error_count += 1
                                break
                        
                        if duplicate:
                            continue
                        
                        barang = Barang(id_barang, nama_barang)
                        self.min_heap.insert(barang)
                        self.max_heap.insert(Barang(id_barang, nama_barang))
                        success_count += 1
                    
                    except ValueError:
                        print(f"  Warning: ID '{row.get('ID', 'N/A')}' bukan angka, dilewati.")
                        error_count += 1
                        continue
            
            if success_count > 0:
                print(f"\n✓ Berhasil menambahkan {success_count} data dari file CSV")
                if error_count > 0:
                    print(f"  ({error_count} data gagal/dilewati)")
            else:
                print("\n✗ Gagal menambahkan data dari CSV")
            
            return success_count
        
        except Exception as e:
            print(f"Error membaca file CSV: {e}")
            return 0
    
    def display_menu(self):
        """
        Menampilkan menu utama program
        """
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║       SISTEM MANAJEMEN DATA BARANG - MIN-HEAP & MAX-HEAP   ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print("║  1. Tambah Data (Insert)                                   ║")
        print("║  2. Tampilkan Data (Ascending - Min-Heap)                  ║")
        print("║  3. Tampilkan Data (Descending - Max-Heap)                 ║")
        print("║  4. Hapus Data dari Min-Heap                               ║")
        print("║  5. Hapus Data dari Max-Heap                               ║")
        print("║  6. Tambah Data dari File CSV                              ║")
        print("║  0. Keluar                                                 ║")
        print("╚════════════════════════════════════════════════════════════╝")
    
    def run(self):
        """
        Method utama untuk menjalankan program
        """
        print("═══════════════════════════════════════════════════════════")
        print("      PROGRAM SISTEM MANAJEMEN DATA BARANG - HEAP")
        print("═══════════════════════════════════════════════════════════")
        print("  Menggunakan Min-Heap dan Max-Heap")
        print("═══════════════════════════════════════════════════════════\n")
        
        while True:
            self.display_menu()
            
            try:
                choice = input("Pilih menu (0-6): ").strip()
                
                if choice == '1':
                    try:
                        id_barang = int(input("Masukkan ID Barang: "))
                        nama_barang = input("Masukkan Nama Barang: ").strip()
                        if not nama_barang:
                            print("✗ Error: Nama barang tidak boleh kosong!")
                        else:
                            self.insert(id_barang, nama_barang)
                    except ValueError:
                        print("✗ Error: ID harus berupa angka!")
                
                elif choice == '2':
                    self.display_ascending()
                
                elif choice == '3':
                    self.display_descending()
                
                elif choice == '4':
                    try:
                        delete_id = int(input("Masukkan ID Barang yang akan dihapus dari Min-Heap: "))
                        self.delete_from_min_heap(delete_id)
                    except ValueError:
                        print("✗ Error: ID harus berupa angka!")
                
                elif choice == '5':
                    try:
                        delete_id = int(input("Masukkan ID Barang yang akan dihapus dari Max-Heap: "))
                        self.delete_from_max_heap(delete_id)
                    except ValueError:
                        print("✗ Error: ID harus berupa angka!")
                
                elif choice == '6':
                    filename = input("Masukkan nama file CSV (default: data.csv): ").strip()
                    if not filename:
                        filename = "data.csv"
                    self.load_from_csv(filename)
                
                elif choice == '0':
                    print("\n╔════════════════════════════════════════════════════════════╗")
                    print("║  Terima kasih telah menggunakan program ini!               ║")
                    print("╚════════════════════════════════════════════════════════════╝\n")
                    break
                
                else:
                    print("✗ Pilihan tidak valid! Silakan pilih 0-6.")
            
            except KeyboardInterrupt:
                print("\n\nProgram dihentikan oleh user.")
                break
            except Exception as e:
                print(f"✗ Error: {e}")
            
            print()


def main():
    """
    Function main - entry point program
    """
    app = HeapApp()
    app.run()


if __name__ == "__main__":
    main()
