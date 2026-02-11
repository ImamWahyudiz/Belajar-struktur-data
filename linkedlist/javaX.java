package linkedlist;

import java.util.Scanner;

/**
 * Class Mahasiswa untuk merepresentasikan data mahasiswa
 * Menyimpan informasi NIM dan Nama mahasiswa
 */
class Mahasiswa {
    String nim;   // Nomor Induk Mahasiswa
    String nama;  // Nama lengkap mahasiswa

    /**
     * Constructor untuk membuat objek Mahasiswa baru
     * @param nim Nomor Induk Mahasiswa
     * @param nama Nama lengkap mahasiswa
     */
    public Mahasiswa(String nim, String nama) {
        this.nim = nim;
        this.nama = nama;
    }

    /**
     * Override method toString untuk menampilkan informasi mahasiswa
     * @return String representasi data mahasiswa
     */
    @Override
    public String toString() {
        return "NIM: " + nim + ", Nama: " + nama;
    }
}

/**
 * Class Node untuk merepresentasikan satu node dalam LinkedList
 * Setiap node berisi data mahasiswa dan pointer ke node berikutnya
 */
class Node {
    Mahasiswa data;  // Data mahasiswa yang disimpan dalam node
    Node next;       // Pointer ke node berikutnya

    /**
     * Constructor untuk membuat node baru
     * @param data Data mahasiswa yang akan disimpan
     */
    public Node(Mahasiswa data) {
        this.data = data;
        this.next = null;
    }
}

/**
 * Class javaX - Sistem Manajemen Data Mahasiswa menggunakan Singly LinkedList
 * 
 * Implementasi struktur data Singly LinkedList tanpa menggunakan library
 * yang mendukung berbagai operasi insert dan delete.
 * 
 * Fitur utama:
 * - Insert: beginning, position, end
 * - Delete: beginning, position, end, by NIM
 * - Display: menampilkan semua data
 * 
 * LinkedList ini tidak memiliki batasan kapasitas (dynamic size).
 */
public class javaX {
    private Node head;   // Pointer ke node pertama dalam LinkedList
    private int count;   // Jumlah node dalam LinkedList

    /**
     * Constructor untuk inisialisasi LinkedList kosong
     */
    public javaX() {
        this.head = null;
        this.count = 0;
    }

    /**
     * Menambahkan data mahasiswa di awal LinkedList
     * Time Complexity: O(1)
     * 
     * @param nim Nomor Induk Mahasiswa
     * @param nama Nama lengkap mahasiswa
     */
    public void insertAtBeginning(String nim, String nama) {
        Mahasiswa mahasiswa = new Mahasiswa(nim, nama);
        Node newNode = new Node(mahasiswa);
        
        newNode.next = head;
        head = newNode;
        count++;
        
        System.out.println("✓ Data berhasil ditambahkan di awal!");
        System.out.println("  Total data: " + count);
    }

    /**
     * Menambahkan data mahasiswa di posisi tertentu (1-based index)
     * Time Complexity: O(n)
     * 
     * @param position Posisi untuk insert (1 sampai count+1)
     * @param nim Nomor Induk Mahasiswa
     * @param nama Nama lengkap mahasiswa
     */
    public void insertAtPosition(int position, String nim, String nama) {
        // Validasi posisi
        if (position < 1 || position > count + 1) {
            System.out.println("✗ Error: Posisi tidak valid!");
            System.out.println("  Posisi harus antara 1 sampai " + (count + 1));
            return;
        }

        // Jika posisi 1, gunakan insertAtBeginning
        if (position == 1) {
            insertAtBeginning(nim, nama);
            return;
        }

        Mahasiswa mahasiswa = new Mahasiswa(nim, nama);
        Node newNode = new Node(mahasiswa);
        
        Node current = head;
        // Traverse ke posisi sebelum target
        for (int i = 1; i < position - 1; i++) {
            current = current.next;
        }
        
        newNode.next = current.next;
        current.next = newNode;
        count++;
        
        System.out.println("✓ Data berhasil ditambahkan di posisi " + position + "!");
        System.out.println("  Total data: " + count);
    }

    /**
     * Menambahkan data mahasiswa di akhir LinkedList
     * Time Complexity: O(n)
     * 
     * @param nim Nomor Induk Mahasiswa
     * @param nama Nama lengkap mahasiswa
     */
    public void insertAtEnd(String nim, String nama) {
        Mahasiswa mahasiswa = new Mahasiswa(nim, nama);
        Node newNode = new Node(mahasiswa);
        
        // Jika LinkedList kosong
        if (head == null) {
            head = newNode;
            count++;
            System.out.println("✓ Data berhasil ditambahkan di akhir!");
            System.out.println("  Total data: " + count);
            return;
        }
        
        // Traverse ke node terakhir
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        
        current.next = newNode;
        count++;
        
        System.out.println("✓ Data berhasil ditambahkan di akhir!");
        System.out.println("  Total data: " + count);
    }

    /**
     * Menghapus data mahasiswa dari awal LinkedList
     * Time Complexity: O(1)
     */
    public void deleteFromBeginning() {
        if (head == null) {
            System.out.println("✗ Error: LinkedList kosong!");
            return;
        }
        
        Mahasiswa deletedData = head.data;
        head = head.next;
        count--;
        
        System.out.println("✓ Data berhasil dihapus dari awal!");
        System.out.println("  Data yang dihapus: " + deletedData);
        System.out.println("  Total data: " + count);
    }

    /**
     * Menghapus data mahasiswa dari posisi tertentu (1-based index)
     * Time Complexity: O(n)
     * 
     * @param position Posisi untuk delete (1 sampai count)
     */
    public void deleteAtPosition(int position) {
        if (head == null) {
            System.out.println("✗ Error: LinkedList kosong!");
            return;
        }
        
        // Validasi posisi
        if (position < 1 || position > count) {
            System.out.println("✗ Error: Posisi tidak valid!");
            System.out.println("  Posisi harus antara 1 sampai " + count);
            return;
        }
        
        // Jika posisi 1, hapus dari awal
        if (position == 1) {
            deleteFromBeginning();
            return;
        }
        
        Node current = head;
        // Traverse ke posisi sebelum target
        for (int i = 1; i < position - 1; i++) {
            current = current.next;
        }
        
        Mahasiswa deletedData = current.next.data;
        current.next = current.next.next;
        count--;
        
        System.out.println("✓ Data berhasil dihapus dari posisi " + position + "!");
        System.out.println("  Data yang dihapus: " + deletedData);
        System.out.println("  Total data: " + count);
    }

    /**
     * Menghapus data mahasiswa dari akhir LinkedList
     * Time Complexity: O(n)
     */
    public void deleteFromEnd() {
        if (head == null) {
            System.out.println("✗ Error: LinkedList kosong!");
            return;
        }
        
        // Jika hanya ada satu node
        if (head.next == null) {
            Mahasiswa deletedData = head.data;
            head = null;
            count--;
            System.out.println("✓ Data berhasil dihapus dari akhir!");
            System.out.println("  Data yang dihapus: " + deletedData);
            System.out.println("  Total data: " + count);
            return;
        }
        
        // Traverse ke node sebelum terakhir
        Node current = head;
        while (current.next.next != null) {
            current = current.next;
        }
        
        Mahasiswa deletedData = current.next.data;
        current.next = null;
        count--;
        
        System.out.println("✓ Data berhasil dihapus dari akhir!");
        System.out.println("  Data yang dihapus: " + deletedData);
        System.out.println("  Total data: " + count);
    }

    /**
     * Menghapus data mahasiswa berdasarkan NIM (first occurrence)
     * Time Complexity: O(n)
     * 
     * @param nim NIM mahasiswa yang akan dihapus
     */
    public void deleteByNim(String nim) {
        if (head == null) {
            System.out.println("✗ Error: LinkedList kosong!");
            return;
        }
        
        // Jika data yang dicari ada di head
        if (head.data.nim.equals(nim)) {
            Mahasiswa deletedData = head.data;
            head = head.next;
            count--;
            System.out.println("✓ Data mahasiswa dengan NIM " + nim + " berhasil dihapus!");
            System.out.println("  Data yang dihapus: " + deletedData);
            System.out.println("  Total data: " + count);
            return;
        }
        
        // Cari node dengan NIM yang sesuai
        Node current = head;
        while (current.next != null) {
            if (current.next.data.nim.equals(nim)) {
                Mahasiswa deletedData = current.next.data;
                current.next = current.next.next;
                count--;
                System.out.println("✓ Data mahasiswa dengan NIM " + nim + " berhasil dihapus!");
                System.out.println("  Data yang dihapus: " + deletedData);
                System.out.println("  Total data: " + count);
                return;
            }
            current = current.next;
        }
        
        System.out.println("✗ Error: Mahasiswa dengan NIM " + nim + " tidak ditemukan!");
    }

    /**
     * Menampilkan semua data mahasiswa dalam LinkedList
     * Time Complexity: O(n)
     */
    public void showData() {
        if (head == null) {
            System.out.println("LinkedList kosong!");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              DAFTAR DATA MAHASISWA                         ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("  Total data: " + count);
        System.out.println("────────────────────────────────────────────────────────────");
        
        Node current = head;
        int index = 1;
        while (current != null) {
            System.out.printf("  [%d] %s%n", index, current.data);
            current = current.next;
            index++;
        }
        
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Menampilkan menu utama program
     */
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEM MANAJEMEN DATA MAHASISWA - LINKED LIST          ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Insert at beginning                                    ║");
        System.out.println("║  2. Insert at given position                               ║");
        System.out.println("║  3. Insert at end                                          ║");
        System.out.println("║  4. Delete from beginning                                  ║");
        System.out.println("║  5. Delete given position                                  ║");
        System.out.println("║  6. Delete from end                                        ║");
        System.out.println("║  7. Delete first occurrence (by NIM)                       ║");
        System.out.println("║  8. Show data                                              ║");
        System.out.println("║  9. Exit                                                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.print("Pilih menu (1-9): ");
    }

    /**
     * Method utama untuk menjalankan program
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            displayMenu();
            
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Konsumsi newline
                
                switch (choice) {
                    case 1: // Insert at beginning
                        System.out.print("Masukkan NIM: ");
                        String nim1 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama1 = scanner.nextLine();
                        insertAtBeginning(nim1, nama1);
                        break;
                        
                    case 2: // Insert at given position
                        System.out.print("Masukkan Posisi (1-" + (count + 1) + "): ");
                        int pos1 = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Masukkan NIM: ");
                        String nim2 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama2 = scanner.nextLine();
                        insertAtPosition(pos1, nim2, nama2);
                        break;
                        
                    case 3: // Insert at end
                        System.out.print("Masukkan NIM: ");
                        String nim3 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama3 = scanner.nextLine();
                        insertAtEnd(nim3, nama3);
                        break;
                        
                    case 4: // Delete from beginning
                        deleteFromBeginning();
                        break;
                        
                    case 5: // Delete given position
                        System.out.print("Masukkan Posisi (1-" + count + "): ");
                        int pos2 = scanner.nextInt();
                        scanner.nextLine();
                        deleteAtPosition(pos2);
                        break;
                        
                    case 6: // Delete from end
                        deleteFromEnd();
                        break;
                        
                    case 7: // Delete first occurrence by NIM
                        System.out.print("Masukkan NIM yang akan dihapus: ");
                        String nimDelete = scanner.nextLine();
                        deleteByNim(nimDelete);
                        break;
                        
                    case 8: // Show data
                        showData();
                        break;
                        
                    case 9: // Exit
                        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
                        System.out.println("║  Terima kasih telah menggunakan program ini!               ║");
                        System.out.println("╚════════════════════════════════════════════════════════════╝");
                        break;
                        
                    default:
                        System.out.println("✗ Pilihan tidak valid! Silakan pilih 1-9.");
                }
            } catch (Exception e) {
                System.out.println("✗ Input tidak valid! Silakan masukkan angka.");
                scanner.nextLine(); // Clear buffer
                choice = 0; // Reset choice agar tidak exit
            }
            
        } while (choice != 9);
        
        scanner.close();
    }

    /**
     * Method main - entry point program
     */
    public static void main(String[] args) {
        javaX app = new javaX();
        app.run();
    }
}
