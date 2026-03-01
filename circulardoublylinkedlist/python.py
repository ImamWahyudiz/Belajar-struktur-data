#!/usr/bin/env python3
"""
Program Simulasi Teks Berjalan Berita Televisi
Implementasi struktur data Circular Doubly Linked List tanpa library

Program ini mendemonstrasikan penggunaan Circular Doubly Linked List untuk
simulasi teks berjalan berita seperti yang sering tampil di televisi.
"""

import time
import threading
import sys
import threading
import sys
import sys


class Node:
    """
    Class untuk merepresentasikan satu node dalam Circular Doubly Linked List
    
    Attributes:
        berita (str): Isi teks berita
        prev (Node): Pointer ke node sebelumnya
        next (Node): Pointer ke node berikutnya
    """
    
    def __init__(self, berita):
        """
        Constructor untuk membuat node baru
        
        Args:
            berita (str): Teks berita yang akan disimpan
        """
        self.berita = berita
        self.prev = None
        self.next = None


class BeritaRunningText:
    """
    Class untuk manajemen Circular Doubly Linked List berita
    
    Implementasi struktur data Circular Doubly Linked List yang mendukung:
    - Insert berita di akhir
    - Delete berita berdasarkan nomor urut
    - Display forward (depan ke belakang dengan delay)
    - Display backward (belakang ke depan dengan delay)
    - Display berita tertentu
    
    Karakteristik Circular Doubly Linked List:
    - Node terakhir menunjuk kembali ke node pertama (circular)
    - Node pertama menunjuk kembali ke node terakhir (circular)
    - Bisa traversal dua arah (forward dan backward)
    
    Attributes:
        head (Node): Pointer ke node pertama dalam list
        count (int): Jumlah berita dalam list
    """
    
    def __init__(self):
        """
        Constructor untuk inisialisasi Circular Doubly Linked List kosong
        """
        self.head = None
        self.count = 0
    
    def insert_berita(self, berita):
        """
        Menambahkan berita baru di akhir list
        Time Complexity: O(n) karena perlu traverse ke akhir
        
        Args:
            berita (str): Teks berita yang akan ditambahkan
        """
        new_node = Node(berita)
        
        # Jika list kosong
        if self.head is None:
            self.head = new_node
            self.head.next = self.head  # Menunjuk ke dirinya sendiri (circular)
            self.head.prev = self.head  # Menunjuk ke dirinya sendiri (circular)
            self.count += 1
            print("✓ Berita berhasil ditambahkan!")
            print(f"  Total berita: {self.count}")
            return
        
        # Dapatkan node terakhir (prev dari head)
        tail = self.head.prev
        
        # Insert node baru di akhir
        new_node.next = self.head      # New node next -> head
        new_node.prev = tail            # New node prev -> tail lama
        tail.next = new_node            # Tail lama next -> new node
        self.head.prev = new_node       # Head prev -> new node
        
        self.count += 1
        print("✓ Berita berhasil ditambahkan!")
        print(f"  Total berita: {self.count}")
    
    def hapus_berita(self, nomor):
        """
        Menghapus berita berdasarkan nomor urut (1-based index)
        Time Complexity: O(n)
        
        Args:
            nomor (int): Nomor urut berita yang akan dihapus (1 sampai count)
        """
        if self.head is None:
            print("✗ Error: List kosong!")
            return
        
        # Validasi nomor
        if nomor < 1 or nomor > self.count:
            print("✗ Error: Nomor tidak valid!")
            print(f"  Nomor harus antara 1 sampai {self.count}")
            return
        
        # Jika hanya ada satu node
        if self.count == 1:
            deleted_berita = self.head.berita
            self.head = None
            self.count -= 1
            print("✓ Berita berhasil dihapus!")
            print(f"  Berita yang dihapus: {deleted_berita}")
            print(f"  Total berita: {self.count}")
            return
        
        # Traverse ke node yang akan dihapus
        current = self.head
        for i in range(1, nomor):
            current = current.next
        
        deleted_berita = current.berita
        
        # Update pointer
        current.prev.next = current.next
        current.next.prev = current.prev
        
        # Jika yang dihapus adalah head, update head
        if current == self.head:
            self.head = current.next
        
        self.count -= 1
        print("✓ Berita berhasil dihapus!")
        print(f"  Berita yang dihapus: {deleted_berita}")
        print(f"  Total berita: {self.count}")
    
    def tampilkan_forward(self):
        """
        Menampilkan semua berita secara forward (depan ke belakang)
        dengan animasi scrolling horizontal seperti teks berjalan di TV
        Time Complexity: O(n)
        """
        if self.head is None:
            print("✗ List kosong! Tidak ada berita untuk ditampilkan.")
            return
        
        # Clear terminal
        print("\033[H\033[2J", end="")
        sys.stdout.flush()
        
        # Kumpulkan semua berita ke dalam list
        news_list = []
        current = self.head
        while True:
            news_list.append(f"     >>>  {current.berita}  <<<     ")
            current = current.next
            if current == self.head:
                break
        
        screen_width = 80  # Lebar layar
        
        print("\n╔═══════════════════════════════════════════════════════════════════════════════╗")
        print("║                     TEKS BERJALAN BERITA - MODE FORWARD                       ║")
        print("╠═══════════════════════════════════════════════════════════════════════════════╣")
        print(f"  Total berita: {self.count} | Tekan ENTER untuk berhenti")
        print("╚═══════════════════════════════════════════════════════════════════════════════╝")
        print()  # Baris kosong
        
        # Flag untuk menghentikan scrolling
        stop_scrolling = [False]
        
        # Thread untuk mendengarkan input keyboard
        def listen_input():
            try:
                input()
                stop_scrolling[0] = True
            except:
                pass
        
        input_thread = threading.Thread(target=listen_input, daemon=True)
        input_thread.start()
        
        # Animasi scrolling dari kanan ke kiri (seperti TV) - LOOP TERUS MENERUS
        try:
            while not stop_scrolling[0]:  # Loop sampai user tekan Enter
                # Loop setiap berita
                for news_text in news_list:
                    if stop_scrolling[0]:
                        break
                    
                    total_scrolls = len(news_text) + screen_width
                    
                    # Scroll berita dari kanan ke kiri
                    for i in range(total_scrolls):
                        if stop_scrolling[0]:
                            break
                        
                        # Clear line dengan carriage return
                        display_line = []
                        
                        for j in range(screen_width):
                            text_index = i - screen_width + j
                            if 0 <= text_index < len(news_text):
                                display_line.append(news_text[text_index])
                            else:
                                display_line.append(' ')
                        
                        # Clear line dan print: \033[2K = clear line, \r = carriage return
                        print('\033[2K\r' + ''.join(display_line), end='', flush=True)
                        time.sleep(0.08)  # Delay untuk kecepatan scrolling
                    
                    # Jeda 3 detik setelah berita selesai di-scroll
                    if not stop_scrolling[0]:
                        time.sleep(3)
                    
                    if stop_scrolling[0]:
                        break
        except KeyboardInterrupt:
            print("\n\n[Dihentikan oleh user]")
        
        print("\n\n✓ Semua berita telah ditampilkan (forward).\n")
    
    def tampilkan_backward(self):
        """
        Menampilkan semua berita secara backward (belakang ke depan)
        dengan animasi scrolling horizontal seperti teks berjalan di TV
        Time Complexity: O(n)
        """
        if self.head is None:
            print("✗ List kosong! Tidak ada berita untuk ditampilkan.")
            return
        
        # Clear terminal
        print("\033[H\033[2J", end="")
        sys.stdout.flush()
        
        # Kumpulkan semua berita ke dalam list (dari belakang ke depan)
        news_list = []
        current = self.head.prev  # Mulai dari tail
        for _ in range(self.count):
            news_list.append(f"     >>>  {current.berita}  <<<     ")
            current = current.prev
        
        screen_width = 80  # Lebar layar
        
        print("\n╔═══════════════════════════════════════════════════════════════════════════════╗")
        print("║                     TEKS BERJALAN BERITA - MODE BACKWARD                      ║")
        print("╠═══════════════════════════════════════════════════════════════════════════════╣")
        print(f"  Total berita: {self.count} | Tekan ENTER untuk berhenti")
        print("╚═══════════════════════════════════════════════════════════════════════════════╝")
        print()  # Baris kosong
        
        # Flag untuk menghentikan scrolling
        stop_scrolling = [False]
        
        # Thread untuk mendengarkan input keyboard
        def listen_input():
            try:
                input()
                stop_scrolling[0] = True
            except:
                pass
        
        input_thread = threading.Thread(target=listen_input, daemon=True)
        input_thread.start()
        
        # Animasi scrolling dari kanan ke kiri (seperti TV) - LOOP TERUS MENERUS
        try:
            while not stop_scrolling[0]:  # Loop sampai user tekan Enter
                # Loop setiap berita
                for news_text in news_list:
                    if stop_scrolling[0]:
                        break
                    
                    total_scrolls = len(news_text) + screen_width
                    
                    # Scroll berita dari kanan ke kiri
                    for i in range(total_scrolls):
                        if stop_scrolling[0]:
                            break
                        
                        # Clear line dengan carriage return
                        display_line = []
                        
                        for j in range(screen_width):
                            text_index = i - screen_width + j
                            if 0 <= text_index < len(news_text):
                                display_line.append(news_text[text_index])
                            else:
                                display_line.append(' ')
                        
                        # Clear line dan print: \033[2K = clear line, \r = carriage return
                        print('\033[2K\r' + ''.join(display_line), end='', flush=True)
                        time.sleep(0.08)  # Delay untuk kecepatan scrolling
                    
                    # Jeda 3 detik setelah berita selesai di-scroll
                    if not stop_scrolling[0]:
                        time.sleep(3)
                    
                    if stop_scrolling[0]:
                        break
        except KeyboardInterrupt:
            print("\n\n[Dihentikan oleh user]")
        
        print("\n\n✓ Semua berita telah ditampilkan (backward).\n")
    
    def tampilkan_berita_tertentu(self, nomor):
        """
        Menampilkan berita tertentu berdasarkan nomor urut dengan animasi scrolling
        Time Complexity: O(n)
        
        Args:
            nomor (int): Nomor urut berita yang akan ditampilkan (1 sampai count)
        """
        if self.head is None:
            print("✗ List kosong!")
            return
        
        # Validasi nomor
        if nomor < 1 or nomor > self.count:
            print("✗ Error: Nomor tidak valid!")
            print(f"  Nomor harus antara 1 sampai {self.count}")
            return
        
        # Traverse ke node yang diminta
        current = self.head
        for i in range(1, nomor):
            current = current.next
        
        # Clear terminal
        print("\033[H\033[2J", end="")
        sys.stdout.flush()
        
        # Gabungkan berita terpilih dengan separator untuk scrolling
        news_text = f"     >>>  {current.berita}  <<<     "
        screen_width = 80  # Lebar layar
        
        print("\n╔═══════════════════════════════════════════════════════════════════════════════╗")
        print(f"║                     TEKS BERJALAN BERITA - BERITA #{nomor}                       ║")
        print("╠═══════════════════════════════════════════════════════════════════════════════╣")
        print(f"  Berita nomor: {nomor} dari {self.count} | Tekan ENTER untuk berhenti")
        print("╚═══════════════════════════════════════════════════════════════════════════════╝")
        print()  # Baris kosong
        
        # Flag untuk menghentikan scrolling
        stop_scrolling = [False]
        
        # Thread untuk mendengarkan input keyboard
        def listen_input():
            try:
                input()
                stop_scrolling[0] = True
            except:
                pass
        
        input_thread = threading.Thread(target=listen_input, daemon=True)
        input_thread.start()
        
        # Animasi scrolling dari kanan ke kiri (seperti TV) - LOOP TERUS MENERUS
        try:
            while not stop_scrolling[0]:  # Loop sampai user tekan Enter
                total_scrolls = len(news_text) + screen_width
                
                # Scroll berita dari kanan ke kiri
                for i in range(total_scrolls):
                    if stop_scrolling[0]:
                        break
                    
                    # Clear line dengan carriage return
                    display_line = []
                    
                    for j in range(screen_width):
                        text_index = i - screen_width + j
                        if 0 <= text_index < len(news_text):
                            display_line.append(news_text[text_index])
                        else:
                            display_line.append(' ')
                    
                    # Clear line dan print: \033[2K = clear line, \r = carriage return
                    print('\033[2K\r' + ''.join(display_line), end='', flush=True)
                    time.sleep(0.08)  # Delay untuk kecepatan scrolling
                
                # Jeda 3 detik setelah berita selesai di-scroll, sebelum loop lagi
                if not stop_scrolling[0]:
                    time.sleep(3)
        except KeyboardInterrupt:
            print("\n\n[Dihentikan oleh user]")
        
        print("\n\n✓ Berita telah ditampilkan.\n")
    
    def lihat_daftar_berita(self):
        """
        Menampilkan daftar semua berita tanpa animasi
        Time Complexity: O(n)
        """
        if self.head is None:
            print("✗ List kosong!")
            return
        
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║                  DAFTAR SEMUA BERITA                       ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print(f"  Total berita: {self.count}")
        print("────────────────────────────────────────────────────────────")
        
        current = self.head
        index = 1
        
        while True:
            print(f"  [{index}] {current.berita}")
            current = current.next
            index += 1
            
            if current == self.head:
                break
        
        print("╚════════════════════════════════════════════════════════════╝\n")
    
    def display_menu(self):
        """
        Menampilkan menu utama program
        """
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║       Circular Doubly Linked List Implementation           ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print("║  1. Insert berita baru                                     ║")
        print("║  2. Hapus berita                                           ║")
        print("║  3. Tampilkan berita (Forward - Depan ke Belakang)         ║")
        print("║  4. Tampilkan berita (Backward - Belakang ke Depan)        ║")
        print("║  5. Tampilkan berita tertentu                              ║")
        print("║  6. Lihat daftar semua berita                              ║")
        print("║  7. Exit                                                   ║")
        print("╚════════════════════════════════════════════════════════════╝")
    
    def run(self):
        """
        Method utama untuk menjalankan program
        """
        while True:
            self.display_menu()
            
            try:
                choice = int(input("Pilih menu (1-7): "))
                
                if choice == 1:  # Insert berita
                    berita = input("\nMasukkan teks berita: ")
                    if berita.strip():
                        self.insert_berita(berita)
                    else:
                        print("✗ Error: Berita tidak boleh kosong!")
                
                elif choice == 2:  # Hapus berita
                    if self.count == 0:
                        print("✗ List kosong! Tidak ada berita untuk dihapus.")
                    else:
                        self.lihat_daftar_berita()
                        nomor = int(input(f"Masukkan nomor berita yang akan dihapus (1-{self.count}): "))
                        self.hapus_berita(nomor)
                
                elif choice == 3:  # Tampilkan forward
                    self.tampilkan_forward()
                
                elif choice == 4:  # Tampilkan backward
                    self.tampilkan_backward()
                
                elif choice == 5:  # Tampilkan berita tertentu
                    if self.count == 0:
                        print("✗ List kosong!")
                    else:
                        nomor = int(input(f"Masukkan nomor berita (1-{self.count}): "))
                        self.tampilkan_berita_tertentu(nomor)
                
                elif choice == 6:  # Lihat daftar berita
                    self.lihat_daftar_berita()
                
                elif choice == 7:  # Exit
                    print("\n╔════════════════════════════════════════════════════════════╗")
                    print("║  Terima kasih telah menggunakan program ini!               ║")
                    print("║  Simulasi Teks Berjalan Berita - CDLL Edition              ║")
                    print("╚════════════════════════════════════════════════════════════╝")
                    break
                
                else:
                    print("✗ Pilihan tidak valid! Silakan pilih 1-7.")
            
            except ValueError:
                print("✗ Input tidak valid! Silakan masukkan angka.")
            except Exception as e:
                print(f"✗ Terjadi kesalahan: {e}")


def main():
    """
    Function main - entry point program
    """
    app = BeritaRunningText()
    app.run()


if __name__ == "__main__":
    main()
