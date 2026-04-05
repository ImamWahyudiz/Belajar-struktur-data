#!/usr/bin/env python3
"""
Program Manajemen Data Barang
Implementasi struktur data Binary Search Tree (BST)

Program ini mendemonstrasikan penggunaan BST untuk menyimpan dan mengelola data barang
dengan berbagai operasi: insert, search, delete, dan traversal (inorder, preorder, postorder).
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


class Node:
    """
    Class untuk merepresentasikan node dalam Binary Search Tree
    
    Attributes:
        data (Barang): Data barang yang disimpan di node
        left (Node): Pointer ke node kiri (subtree dengan nilai lebih kecil)
        right (Node): Pointer ke node kanan (subtree dengan nilai lebih besar)
    """
    
    def __init__(self, data):
        """
        Constructor untuk membuat node baru
        
        Args:
            data (Barang): Data barang yang disimpan di node
        """
        self.data = data
        self.left = None
        self.right = None


class BSTApp:
    """
    Class untuk manajemen Binary Search Tree (BST)
    
    Implementasi struktur data Binary Search Tree yang mendukung:
    - Insert: menambahkan data barang
    - Search: mencari data barang berdasarkan ID
    - Delete: menghapus data barang berdasarkan ID
    - Traversal: Inorder, Preorder, Postorder
    
    Karakteristik BST:
    - Setiap node memiliki paling banyak 2 children
    - Nilai di left subtree < nilai node
    - Nilai di right subtree > nilai node
    
    Attributes:
        root (Node): Root node dari BST
        count (int): Jumlah node dalam tree
    """
    
    def __init__(self):
        """
        Constructor - Inisialisasi BST kosong
        """
        self.root = None
        self.count = 0
    
    def insert(self, id, nama):
        """
        Insert data barang ke dalam BST
        Time Complexity: O(log n) - average case, O(n) - worst case (skewed tree)
        
        Args:
            id (int): ID barang
            nama (str): Nama barang
        """
        # Cek apakah ID sudah ada
        if self._search_node(self.root, id) is not None:
            print("Error: ID sudah ada dalam tree!")
            return
        
        self.root = self._insert_recursive(self.root, Barang(id, nama))
        self.count += 1
        print("Data berhasil ditambahkan!")
    
    def _insert_recursive(self, node, data):
        """
        Helper method untuk insert secara recursive
        
        Args:
            node (Node): Node saat ini dalam proses traversal
            data (Barang): Data barang yang akan diinsert
        
        Returns:
            Node: Node yang sudah diupdate
        """
        # Base case: jika node kosong, buat node baru
        if node is None:
            return Node(data)
        
        # Jika ID lebih kecil, insert ke left subtree
        if data.id < node.data.id:
            node.left = self._insert_recursive(node.left, data)
        # Jika ID lebih besar, insert ke right subtree
        elif data.id > node.data.id:
            node.right = self._insert_recursive(node.right, data)
        
        return node
    
    def search(self, id):
        """
        Search/Cari data barang berdasarkan ID
        Time Complexity: O(log n) - average case, O(n) - worst case
        
        Args:
            id (int): ID barang yang dicari
        
        Returns:
            Barang: Objek Barang jika ditemukan, None jika tidak ada
        """
        result = self._search_node(self.root, id)
        if result is not None:
            print(f"Data ditemukan: {result.data}")
            return result.data
        else:
            print(f"Data dengan ID {id} tidak ditemukan!")
            return None
    
    def _search_node(self, node, id):
        """
        Helper method untuk search secara recursive
        
        Args:
            node (Node): Node saat ini
            id (int): ID yang dicari
        
        Returns:
            Node: Node jika ditemukan, None jika tidak
        """
        # Base case: node kosong atau ID ditemukan
        if node is None:
            return None
        
        if id == node.data.id:
            return node
        # Jika ID lebih kecil, cari di left subtree
        elif id < node.data.id:
            return self._search_node(node.left, id)
        # Jika ID lebih besar, cari di right subtree
        else:
            return self._search_node(node.right, id)
    
    def delete(self, id):
        """
        Delete data barang berdasarkan ID
        Time Complexity: O(log n) - average case, O(n) - worst case
        Menangani 3 kasus: leaf node, node dengan 1 child, node dengan 2 children
        
        Args:
            id (int): ID barang yang akan dihapus
        """
        if self._search_node(self.root, id) is None:
            print(f"Data dengan ID {id} tidak ditemukan!")
            return
        
        self.root = self._delete_recursive(self.root, id)
        self.count -= 1
        print("Data berhasil dihapus!")
    
    def _delete_recursive(self, node, id):
        """
        Helper method untuk delete secara recursive
        
        Args:
            node (Node): Node saat ini
            id (int): ID yang akan dihapus
        
        Returns:
            Node: Node yang sudah diupdate
        """
        if node is None:
            return None
        
        if id < node.data.id:
            node.left = self._delete_recursive(node.left, id)
        elif id > node.data.id:
            node.right = self._delete_recursive(node.right, id)
        else:
            # Node ditemukan, sekarang delete-nya
            
            # Case 1: Node adalah leaf node (tidak punya child)
            if node.left is None and node.right is None:
                return None
            
            # Case 2: Node punya hanya right child
            if node.left is None:
                return node.right
            
            # Case 2: Node punya hanya left child
            if node.right is None:
                return node.left
            
            # Case 3: Node punya 2 children
            # Cari successor (node terkecil di right subtree)
            successor = self._find_min(node.right)
            node.data = successor.data
            node.right = self._delete_recursive(node.right, successor.data.id)
        
        return node
    
    def _find_min(self, node):
        """
        Helper method untuk mencari node dengan nilai minimum (paling kiri)
        
        Args:
            node (Node): Node awal pencarian
        
        Returns:
            Node: Node dengan nilai minimum
        """
        while node.left is not None:
            node = node.left
        return node
    
    def inorder_traversal(self):
        """
        Traversal Inorder (Left-Root-Right)
        Menghasilkan output dalam urutan ascending/sorted
        Time Complexity: O(n)
        """
        if self.root is None:
            print("\nTree kosong!")
            return
        
        print("\n" + "="*70)
        print("INORDER TRAVERSAL: Left → Root → Right (Urutan Ascending)")
        print("="*70)
        
        result = []
        self._inorder_recursive(self.root, result)
        
        print("\nHasil Traversal (Urutan Ascending):")
        for i, item in enumerate(result, 1):
            print(f"  {i:2d}. {item}")
        
        balance_status = "✓ BALANCED" if self._is_balanced(self.root) else "✗ NOT BALANCED"
        print(f"\n{'─'*70}")
        print(f"Total: {self.count} node | Tinggi: {self._get_height(self.root)} | Status: {balance_status}")
        print("="*70 + "\n")
    
    def _inorder_with_steps(self, node, result, steps, prefix=""):
        """Helper untuk inorder dengan tracking steps"""
        if node is None:
            return
        self._inorder_with_steps(node.left, result, steps, prefix + "L")
        data_str = f"ID: {node.data.id}, Nama: {node.data.nama}"
        steps.append(data_str)
        result.append(data_str)
        self._inorder_with_steps(node.right, result, steps, prefix + "R")
    
    def _inorder_recursive(self, node, result):
        """
        Helper method untuk inorder traversal
        
        Args:
            node (Node): Node saat ini
            result (list): List hasil traversal
        """
        if node is None:
            return
        self._inorder_recursive(node.left, result)
        result.append(str(node.data))
        self._inorder_recursive(node.right, result)
    
    def preorder_traversal(self):
        """
        Traversal Preorder (Root-Left-Right)
        Time Complexity: O(n)
        """
        if self.root is None:
            print("\nTree kosong!")
            return
        
        print("\n" + "="*70)
        print("PREORDER TRAVERSAL: Root → Left → Right (Untuk Copy Tree)")
        print("="*70)
        
        result = []
        self._preorder_recursive(self.root, result)
        
        print("\nHasil Traversal (Urutan Kunjungan):")
        for i, item in enumerate(result, 1):
            print(f"  {i:2d}. {item}")
        
        balance_status = "✓ BALANCED" if self._is_balanced(self.root) else "✗ NOT BALANCED"
        print(f"\n{'─'*70}")
        print(f"Total: {self.count} node | Tinggi: {self._get_height(self.root)} | Status: {balance_status}")
        print("="*70 + "\n")
        
        print(f"\nInfo Tree:")
        print(f"  • Total node: {self.count}")
        print(f"  • Tinggi tree: {self._get_height(self.root)}")
        print(f"  • Status: {'✓ BALANCED' if self._is_balanced(self.root) else '✗ NOT BALANCED'}")
        print("="*60 + "\n")
    
    def _preorder_with_steps(self, node, result, steps, prefix=""):
        """Helper untuk preorder dengan tracking steps"""
        if node is None:
            return
        data_str = f"ID: {node.data.id}, Nama: {node.data.nama}"
        steps.append(data_str)
        result.append(data_str)
        self._preorder_with_steps(node.left, result, steps, prefix + "L")
        self._preorder_with_steps(node.right, result, steps, prefix + "R")
    
    def _preorder_recursive(self, node, result):
        """
        Helper method untuk preorder traversal
        
        Args:
            node (Node): Node saat ini
            result (list): List hasil traversal
        """
        if node is None:
            return
        result.append(str(node.data))
        self._preorder_recursive(node.left, result)
        self._preorder_recursive(node.right, result)
    
    def postorder_traversal(self):
        """
        Traversal Postorder (Left-Right-Root)
        Time Complexity: O(n)
        """
        if self.root is None:
            print("\nTree kosong!")
            return
        
        print("\n" + "="*70)
        print("POSTORDER TRAVERSAL: Left → Right → Root (Untuk Delete Tree)")
        print("="*70)
        
        result = []
        self._postorder_recursive(self.root, result)
        
        print("\nHasil Traversal (Urutan Kunjungan):")
        for i, item in enumerate(result, 1):
            print(f"  {i:2d}. {item}")
        
        balance_status = "✓ BALANCED" if self._is_balanced(self.root) else "✗ NOT BALANCED"
        print(f"\n{'─'*70}")
        print(f"Total: {self.count} node | Tinggi: {self._get_height(self.root)} | Status: {balance_status}")
        print("="*70 + "\n")
        
        print(f"\nInfo Tree:")
        print(f"  • Total node: {self.count}")
        print(f"  • Tinggi tree: {self._get_height(self.root)}")
        print(f"  • Status: {'✓ BALANCED' if self._is_balanced(self.root) else '✗ NOT BALANCED'}")
        print("="*60 + "\n")
    
    def _postorder_with_steps(self, node, result, steps, prefix=""):
        """Helper untuk postorder dengan tracking steps"""
        if node is None:
            return
        self._postorder_with_steps(node.left, result, steps, prefix + "L")
        self._postorder_with_steps(node.right, result, steps, prefix + "R")
        data_str = f"ID: {node.data.id}, Nama: {node.data.nama}"
        steps.append(data_str)
        result.append(data_str)
    
    def _postorder_recursive(self, node, result):
        """
        Helper method untuk postorder traversal
        
        Args:
            node (Node): Node saat ini
            result (list): List hasil traversal
        """
        if node is None:
            return
        self._postorder_recursive(node.left, result)
        self._postorder_recursive(node.right, result)
        result.append(str(node.data))
    
    def display_all(self):
        """
        Tampilkan semua data dalam tree (menggunakan inorder traversal)
        """
        print("\n========== SEMUA DATA BARANG ==========")
        if self.root is None:
            print("Tree kosong!")
        else:
            result = []
            self._inorder_recursive(self.root, result)
            for item in result:
                print(item)
        print("======================================\n")
    
    def show_statistics(self):
        """
        Tampilkan informasi statistik tree
        """
        print("\n========== STATISTIK TREE ==========")
        print(f"Jumlah node: {self.count}")
        print(f"Tinggi tree: {self._get_height(self.root)}")
        print(f"Status balance: {'✓ BALANCED' if self._is_balanced(self.root) else '✗ NOT BALANCED'}")
        print("====================================\n")
    
    def balance_tree(self):
        """
        Rebuild tree agar balanced (AVL-like)
        Menggunakan in-order traversal untuk mendapatkan data terurut,
        lalu rebuild tree dari tengah (divide & conquer)
        """
        if self.count == 0:
            print("Tree kosong, tidak ada yang di-balance!")
            return
        
        # Dapatkan semua data dalam urutan sorted (inorder)
        sorted_data = []
        self._inorder_recursive(self.root, sorted_data)
        
        # Clear tree
        self.root = None
        
        # Rebuild tree dari data yang sudah sorted (membuat balanced tree)
        self._build_balanced_tree(sorted_data)
        print(f"\n✓ Tree berhasil di-balance! (Sebelumnya: NOT BALANCED, Sekarang: BALANCED)")
    
    def _build_balanced_tree(self, sorted_items):
        """
        Build balanced tree dari sorted data
        Menggunakan middle element sebagai root, lalu recursive ke left dan right
        """
        if not sorted_items:
            return
        
        # Ekstrak ID dan nama dari string "ID: x, Nama: y"
        data_list = []
        for item in sorted_items:
            parts = item.split(", ")
            id_str = parts[0].replace("ID: ", "")
            nama_str = parts[1].replace("Nama: ", "")
            data_list.append(Barang(int(id_str), nama_str))
        
        self.root = self._build_balanced_recursive(data_list, 0, len(data_list) - 1)
    
    def _build_balanced_recursive(self, data_list, start, end):
        """
        Recursive helper untuk build balanced tree
        Mengambil middle element sebagai root
        """
        if start > end:
            return None
        
        mid = (start + end) // 2
        node = Node(data_list[mid])
        
        node.left = self._build_balanced_recursive(data_list, start, mid - 1)
        node.right = self._build_balanced_recursive(data_list, mid + 1, end)
        
        return node
    
    def _print_tree_visual(self, node=None, level=0, prefix="", is_left=True):
        """
        Visualisasi tree yang lebih detail dengan height info
        """
        if node is None:
            node = self.root
            if node is None:
                print("  (Tree Kosong)")
                return
        
        # Print current node dengan height info
        balance_marker = self._get_balance_marker(node)
        height = self._get_height(node)
        print(f"{prefix}└─ ID: {node.data.id:4d} (h:{height}) {balance_marker}")
        
        # Print children
        children = []
        if node.left is not None:
            children.append(("L", node.left))
        if node.right is not None:
            children.append(("R", node.right))
        
        for i, (direction, child) in enumerate(children):
            is_last = i == len(children) - 1
            new_prefix = prefix + ("    " if is_left else "    ")
            extension = "    " if is_last else "│   "
            
            print(f"{prefix}{'└── ' if is_last else '├── '}{direction}:")
            self._print_tree_visual(child, level + 1, new_prefix + extension, not is_last)
    
    def _get_balance_marker(self, node):
        """
        Dapatkan marker untuk menunjukkan balance status dari setiap node
        """
        if node is None:
            return ""
        
        left_h = self._get_height(node.left)
        right_h = self._get_height(node.right)
        diff = abs(left_h - right_h)
        
        if diff > 1:
            return "✗ UNBALANCED"
        elif diff == 0:
            return "✓ PERFECT"
        else:
            return "✓ OK"
    
    def _print_tree(self, node=None, level=0, prefix="Root: "):
        """
        Visualisasi struktur tree dalam format ASCII art
        """
        if node is None:
            node = self.root
        
        if node is None:
            print("  (empty)")
            return
        
        print("  " + prefix + str(node.data.id))
        
        if node.left is not None or node.right is not None:
            if node.left is not None:
                self._print_tree(node.left, level + 1, "├─ L: ")
            else:
                print("  " + "│  " * level + "├─ L: (empty)")
            
            if node.right is not None:
                self._print_tree(node.right, level + 1, "└─ R: ")
            else:
                print("  " + "│  " * level + "└─ R: (empty)")
    
    def _is_balanced(self, node):
        """
        Check apakah tree balanced atau tidak
        Tree balanced jika untuk setiap node, perbedaan tinggi left dan right subtree <= 1
        
        Returns:
            bool: True jika balanced, False jika tidak
        """
        if node is None:
            return True
        
        left_height = self._get_height(node.left)
        right_height = self._get_height(node.right)
        
        if abs(left_height - right_height) > 1:
            return False
        
        return self._is_balanced(node.left) and self._is_balanced(node.right)
    
    def load_from_csv(self, filename):
        """
        Load data barang dari file CSV
        Format CSV: ID,Nama (dengan header)
        
        Args:
            filename (str): Path ke file CSV
        
        Returns:
            int: Jumlah data yang berhasil ditambahkan
        """
        # Cek beberapa kemungkinan lokasi file
        possible_paths = [
            filename,  # Path yang diberikan user
            os.path.join(os.path.dirname(__file__), filename),  # Di folder script
            os.path.join(os.path.dirname(__file__), 'bst', filename),  # Di subfolder bst
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
                        
                        if self._search_node(self.root, id_barang) is not None:
                            print(f"  Warning: ID {id_barang} sudah ada, dilewati.")
                            error_count += 1
                            continue
                        
                        self.root = self._insert_recursive(self.root, Barang(id_barang, nama_barang))
                        self.count += 1
                        success_count += 1
                    
                    except ValueError:
                        print(f"  Warning: ID '{row.get('ID', 'N/A')}' bukan angka, dilewati.")
                        error_count += 1
                        continue
            
            if success_count > 0:
                print(f"\nBerhasil menambahkan {success_count} data dari file CSV")
                if error_count > 0:
                    print(f"({error_count} data gagal/dilewati)")
            else:
                print("\nGagal menambahkan data dari CSV")
            
            return success_count
        
        except Exception as e:
            print(f"Error membaca file CSV: {e}")
            return 0
    
    def _get_height(self, node):
        """
        Helper method untuk mendapatkan tinggi tree
        
        Args:
            node (Node): Node awal
        
        Returns:
            int: Tinggi tree
        """
        if node is None:
            return 0
        return 1 + max(self._get_height(node.left), self._get_height(node.right))


def main():
    """
    Main function - Program utama dengan menu interaktif
    """
    bst = BSTApp()
    
    print("========== PROGRAM BINARY SEARCH TREE ==========")
    print("Program Manajemen Data Barang")
    print("================================================\n")
    
    running = True
    while running:
        print("========== MENU BINARY SEARCH TREE ==========")
        print("1. Tambah Data")
        print("2. Cari Data")
        print("3. Hapus Data")
        print("4. Traversal (Inorder)")
        print("5. Traversal (Preorder)")
        print("6. Traversal (Postorder)")
        print("7. Tampilkan Semua Data")
        print("8. Informasi Tree")
        print("9. Tambah Data dari CSV")
        print("10. Balance/Rebalance Tree")
        print("0. Keluar")
        print("=============================================")
        
        try:
            choice = input("Pilih menu (0-10): ").strip()
            
            if choice == '1':
                try:
                    id_barang = int(input("Masukkan ID Barang: "))
                    nama_barang = input("Masukkan Nama Barang: ").strip()
                    if not nama_barang:
                        print("Error: Nama barang tidak boleh kosong!")
                    else:
                        bst.insert(id_barang, nama_barang)
                except ValueError:
                    print("Error: ID harus berupa angka!")
            
            elif choice == '2':
                try:
                    search_id = int(input("Masukkan ID Barang yang dicari: "))
                    bst.search(search_id)
                except ValueError:
                    print("Error: ID harus berupa angka!")
            
            elif choice == '3':
                try:
                    delete_id = int(input("Masukkan ID Barang yang akan dihapus: "))
                    bst.delete(delete_id)
                except ValueError:
                    print("Error: ID harus berupa angka!")
            
            elif choice == '4':
                bst.inorder_traversal()
            
            elif choice == '5':
                bst.preorder_traversal()
            
            elif choice == '6':
                bst.postorder_traversal()
            
            elif choice == '7':
                bst.display_all()
            
            elif choice == '8':
                bst.show_statistics()
            
            elif choice == '9':
                filename = input("Masukkan nama file CSV (default: data.csv): ").strip()
                if not filename:
                    filename = "data.csv"
                bst.load_from_csv(filename)
            
            elif choice == '10':
                print("\nMengubah tree menjadi balanced...")
                bst.balance_tree()
                bst.show_statistics()
            
            elif choice == '0':
                print("Terima kasih! Program selesai.")
                running = False
            
            else:
                print("Pilihan tidak valid! Silakan coba lagi.")
        
        except KeyboardInterrupt:
            print("\n\nProgram dihentikan oleh user.")
            running = False
        except Exception as e:
            print(f"Error: {e}")
        
        print()


if __name__ == "__main__":
    main()
