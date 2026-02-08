package array;

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
 * Class javaX - Sistem Manajemen Data Mahasiswa menggunakan Array
 * 
 * Implementasi struktur data array dengan kapasitas tetap (fixed-size)
 * yang mendukung berbagai operasi insert dan delete.
 * 
 * Fitur utama:
 * - Insert: beginning, position, end
 * - Delete: beginning, position, end, by NIM
 * - Display: menampilkan semua data
 * 
 * Array ini menggunakan kapasitas tetap 10 elemen dan tidak bisa di-resize.
 */
public class javaX {
    private static final int CAPACITY = 10;  // Kapasitas maksimal array (fixed)
    private final Mahasiswa[] data;           // Array untuk menyimpan data mahasiswa
    private int count;                        // Jumlah elemen yang terisi dalam array

    /**
     * Constructor - Inisialisasi array dengan kapasitas tetap
     * Array diinisialisasi dengan ukuran CAPACITY dan count dimulai dari 0
     */
    public javaX() {
        data = new Mahasiswa[CAPACITY];
        count = 0;
    }

    /**
     * Insert data mahasiswa di awal array (index 0)
     * Time Complexity: O(n) - karena perlu shift semua elemen ke kanan
     * 
     * @param nim NIM mahasiswa yang akan ditambahkan
     * @param nama Nama mahasiswa yang akan ditambahkan
     */
    public void insertAtBeginning(String nim, String nama) {
        // Validasi: cek apakah array sudah penuh
        if (count >= CAPACITY) {
            System.out.println("Array penuh! Tidak bisa menambah data.");
            return;
        }
        
        // Shift semua elemen ke kanan untuk memberi ruang di index 0
        // Mulai dari elemen terakhir agar tidak tertimpa
        for (int i = count; i > 0; i--) {
            data[i] = data[i - 1];
        }
        
        // Masukkan data baru di posisi pertama
        data[0] = new Mahasiswa(nim, nama);
        count++;  // Increment jumlah elemen
        System.out.println("Data berhasil ditambahkan di awal!");
    }

    /**
     * Insert data mahasiswa di posisi tertentu
     * Time Complexity: O(n) - karena perlu shift elemen dari posisi tersebut
     * 
     * @param position Posisi index dimana data akan disisipkan (0 sampai count)
     * @param nim NIM mahasiswa yang akan ditambahkan
     * @param nama Nama mahasiswa yang akan ditambahkan
     */
    public void insertAtPosition(int position, String nim, String nama) {
        // Validasi: cek apakah posisi valid
        if (position < 0 || position > count) {
            System.out.println("Posisi tidak valid! (0 - " + count + ")");
            return;
        }
        
        // Validasi: cek apakah array sudah penuh
        if (count >= CAPACITY) {
            System.out.println("Array penuh! Tidak bisa menambah data.");
            return;
        }
        
        // Shift elemen dari posisi yang diinginkan ke kanan
        for (int i = count; i > position; i--) {
            data[i] = data[i - 1];
        }
        
        // Masukkan data baru di posisi yang diminta
        data[position] = new Mahasiswa(nim, nama);
        count++;
        System.out.println("Data berhasil ditambahkan di posisi " + position + "!");
    }

    /**
     * Insert data mahasiswa di akhir array
     * Time Complexity: O(1) - tidak perlu shift, langsung tambah di belakang
     * 
     * @param nim NIM mahasiswa yang akan ditambahkan
     * @param nama Nama mahasiswa yang akan ditambahkan
     */
    public void insertAtEnd(String nim, String nama) {
        // Validasi: cek apakah array sudah penuh
        if (count >= CAPACITY) {
            System.out.println("Array penuh! Tidak bisa menambah data.");
            return;
        }
        
        // Langsung masukkan data di posisi count (akhir)
        data[count] = new Mahasiswa(nim, nama);
        count++;
        System.out.println("Data berhasil ditambahkan di akhir!");
    }

    /**
     * Delete data mahasiswa dari awal array (index 0)
     * Time Complexity: O(n) - karena perlu shift semua elemen ke kiri
     */
    public void deleteFromBeginning() {
        // Validasi: cek apakah array kosong
        if (count == 0) {
            System.out.println("Array kosong! Tidak ada data yang dihapus.");
            return;
        }
        
        // Shift semua elemen ke kiri untuk menimpa elemen pertama
        for (int i = 0; i < count - 1; i++) {
            data[i] = data[i + 1];
        }
        
        // Set posisi terakhir jadi null untuk cleanup
        data[count - 1] = null;
        count--;  // Decrement jumlah elemen
        System.out.println("Data berhasil dihapus dari awal!");
    }

    /**
     * Delete data mahasiswa dari posisi tertentu
     * Time Complexity: O(n) - karena perlu shift elemen dari posisi tersebut
     * 
     * @param position Posisi index yang akan dihapus (0 sampai count-1)
     */
    public void deleteFromPosition(int position) {
        // Validasi: cek apakah posisi valid
        if (position < 0 || position >= count) {
            System.out.println("Posisi tidak valid! (0 - " + (count - 1) + ")");
            return;
        }
        
        // Shift elemen dari posisi selanjutnya ke kiri untuk menimpa elemen yang dihapus
        for (int i = position; i < count - 1; i++) {
            data[i] = data[i + 1];
        }
        
        // Set posisi terakhir jadi null untuk cleanup
        data[count - 1] = null;
        count--;
        System.out.println("Data berhasil dihapus dari posisi " + position + "!");
    }

    /**
     * Delete data mahasiswa dari akhir array
     * Time Complexity: O(1) - tidak perlu shift, langsung hapus dari belakang
     */
    public void deleteFromEnd() {
        // Validasi: cek apakah array kosong
        if (count == 0) {
            System.out.println("Array kosong! Tidak ada data yang dihapus.");
            return;
        }
        
        // Set posisi terakhir jadi null dan kurangi count
        data[count - 1] = null;
        count--;
        System.out.println("Data berhasil dihapus dari akhir!");
    }

    /**
     * Delete kemunculan pertama dari mahasiswa dengan NIM tertentu
     * Time Complexity: O(n) - perlu search linear + shift
     * 
     * @param nim NIM mahasiswa yang akan dihapus
     */
    public void deleteFirstOccurrence(String nim) {
        boolean found = false;
        
        // Linear search untuk mencari NIM yang cocok
        for (int i = 0; i < count; i++) {
            if (data[i].nim.equals(nim)) {
                // Jika ditemukan, shift elemen ke kiri dari posisi tersebut
                for (int j = i; j < count - 1; j++) {
                    data[j] = data[j + 1];
                }
                
                // Cleanup posisi terakhir
                data[count - 1] = null;
                count--;
                System.out.println("Data dengan NIM " + nim + " berhasil dihapus!");
                found = true;
                break;  // Hanya hapus kemunculan pertama
            }
        }
        
        // Jika tidak ditemukan, beri notifikasi
        if (!found) {
            System.out.println("Data dengan NIM " + nim + " tidak ditemukan!");
        }
    }

    /**
     * Menampilkan semua data mahasiswa yang ada di array
     * Menampilkan jumlah data dan list semua mahasiswa dengan nomor urut
     */
    public void showData() {
        if (count == 0) {
            System.out.println("Array kosong! Tidak ada data untuk ditampilkan.");
            return;
        }
        
        System.out.println("\n========== DATA MAHASISWA ==========");
        System.out.println("Jumlah data: " + count);
        
        // Loop untuk menampilkan setiap data mahasiswa
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + data[i]);
        }
        System.out.println("===================================\n");
    }

    /**
     * Menampilkan menu pilihan operasi yang tersedia
     */
    public void displayMenu() {
        System.out.println("\n===== MENU MANAJEMEN DATA MAHASISWA =====");
        System.out.println("1. Tambah di awal");
        System.out.println("2. Tambah di posisi tertentu");
        System.out.println("3. Tambah di akhir");
        System.out.println("4. Hapus dari awal");
        System.out.println("5. Hapus dari posisi tertentu");
        System.out.println("6. Hapus dari akhir");
        System.out.println("7. Hapus berdasarkan NIM");
        System.out.println("8. Tampilkan data");
        System.out.println("9. Keluar");
        System.out.println("========================================");
    }

    /**
     * Method main - Entry point program
     * Menjalankan loop utama untuk menerima input user dan memproses pilihan menu
     * 
     * @param args Command line arguments (tidak digunakan)
     */
    public static void main(String[] args) {
        // Try-with-resources untuk auto-close Scanner
        try (Scanner scanner = new Scanner(System.in)) {
            javaX managementSystem = new javaX();  // Inisialisasi sistem
            int choice;
            boolean running = true;

            // Header program
            System.out.println("=== PROGRAM MANAJEMEN DATA MAHASISWA ===");
            System.out.println("Kapasitas Array: " + CAPACITY);
            System.out.println("=========================================\n");

            // Loop utama program
            while (running) {
                managementSystem.displayMenu();
                System.out.print("Pilih menu (1-9): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                // Switch case untuk memproses pilihan user
                switch (choice) {
                    case 1 -> {  // Insert at beginning
                        System.out.print("Masukkan NIM: ");
                        String nim1 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama1 = scanner.nextLine();
                        managementSystem.insertAtBeginning(nim1, nama1);
                    }

                    case 2 -> {  // Insert at position
                        System.out.print("Masukkan posisi (0 - " + managementSystem.count + "): ");
                        int position = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Masukkan NIM: ");
                        String nim2 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama2 = scanner.nextLine();
                        managementSystem.insertAtPosition(position, nim2, nama2);
                    }

                    case 3 -> {  // Insert at end
                        System.out.print("Masukkan NIM: ");
                        String nim3 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama3 = scanner.nextLine();
                        managementSystem.insertAtEnd(nim3, nama3);
                    }

                    case 4 -> managementSystem.deleteFromBeginning();  // Delete from beginning

                    case 5 -> {  // Delete from position
                        System.out.print("Masukkan posisi (0 - " + (managementSystem.count - 1) + "): ");
                        int delPosition = scanner.nextInt();
                        scanner.nextLine();
                        managementSystem.deleteFromPosition(delPosition);
                    }

                    case 6 -> managementSystem.deleteFromEnd();  // Delete from end

                    case 7 -> {  // Delete by NIM
                        System.out.print("Masukkan NIM yang ingin dihapus: ");
                        String nimToDelete = scanner.nextLine();
                        managementSystem.deleteFirstOccurrence(nimToDelete);
                    }

                    case 8 -> managementSystem.showData();  // Show all data

                    case 9 -> {  // Exit program
                        running = false;
                        System.out.println("Terima kasih! Program selesai.");
                    }

                    default -> System.out.println("Menu tidak valid! Silakan pilih menu 1-9.");
                }
            }
        }
    }
}
