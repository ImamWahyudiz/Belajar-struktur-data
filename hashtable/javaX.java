package hashtable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

/**
 * Class Node untuk merepresentasikan satu node dalam chain (Linked List)
 * Digunakan sebagai entry dalam setiap bucket Hash Table
 */
class Node {
    int data;   // Data yang disimpan
    Node next;  // Pointer ke node berikutnya dalam chain

    /**
     * Constructor untuk membuat node baru
     * @param data Nilai numerik yang disimpan
     */
    public Node(int data) {
        this.data = data;
        this.next = null;
    }
}

/**
 * Class HashTable - Implementasi Hash Table dengan Separate Chaining
 *
 * Separate Chaining adalah teknik collision handling di mana setiap bucket
 * berupa Linked List. Elemen-elemen yang memiliki hash value yang sama
 * dirangkai dalam satu chain (rantai) di bucket yang sama.
 *
 * Hash Function : h(key) = |key| % TABLE_SIZE
 * Collision Tech: Separate Chaining (Open Hashing)
 * TABLE_SIZE    : 151 (bilangan prima untuk distribusi merata)
 *
 * Kompleksitas Waktu (rata-rata):
 * - Insert : O(1)
 * - Delete : O(1)
 * - Search : O(1)
 * Kompleksitas Waktu (terburuk, semua elemen satu bucket):
 * - Insert : O(n)
 * - Delete : O(n)
 * - Search : O(n)
 */
class HashTable {

    /** Ukuran tabel hash - bilangan prima untuk distribusi optimal */
    private static final int TABLE_SIZE = 151;

    /** Array of Node - setiap elemen adalah head dari sebuah chain */
    private Node[] table;

    /** Jumlah elemen yang tersimpan dalam hash table */
    private int count;

    /**
     * Constructor - Inisialisasi hash table kosong
     * Semua bucket diinisialisasi dengan null (chain kosong)
     */
    public HashTable() {
        table = new Node[TABLE_SIZE];
        count = 0;
    }

    /**
     * Hash Function - menggunakan metode Modulo Division
     * h(key) = |key| % TABLE_SIZE
     *
     * Menggunakan Math.abs() agar angka negatif tetap menghasilkan
     * index yang valid.
     *
     * @param key Nilai kunci yang akan di-hash
     * @return Index bucket (0 hingga TABLE_SIZE-1)
     */
    private int hashFunction(int key) {
        return Math.abs(key) % TABLE_SIZE;
    }

    /**
     * Insert data ke dalam hash table
     * Jika terjadi collision, elemen baru disisipkan di depan chain (prepend).
     * Time Complexity: O(1) average, O(n) worst case
     *
     * @param value Nilai numerik yang akan dimasukkan
     * @return true jika berhasil dimasukkan, false jika nilai sudah ada (duplikat)
     */
    public boolean insert(int value) {
        int index = hashFunction(value);

        // Traversal chain untuk cek duplikat
        Node current = table[index];
        while (current != null) {
            if (current.data == value) {
                return false; // Duplikat - tolak
            }
            current = current.next;
        }

        // Sisipkan di depan chain (prepend) - O(1)
        Node newNode = new Node(value);
        newNode.next = table[index];
        table[index] = newNode;
        count++;
        return true;
    }

    /**
     * Hapus data dari hash table
     * Time Complexity: O(1) average, O(n) worst case
     *
     * @param value Nilai numerik yang akan dihapus
     * @return true jika berhasil dihapus, false jika tidak ditemukan
     */
    public boolean delete(int value) {
        int index = hashFunction(value);
        Node current = table[index];
        Node prev = null;

        // Traversal chain untuk mencari nilai
        while (current != null) {
            if (current.data == value) {
                // Putus sambungan node dari chain
                if (prev == null) {
                    table[index] = current.next; // Hapus head chain
                } else {
                    prev.next = current.next;    // Bypass node yang dihapus
                }
                count--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false; // Tidak ditemukan
    }

    /**
     * Cari data dalam hash table
     * Time Complexity: O(1) average, O(n) worst case
     *
     * @param value Nilai numerik yang dicari
     * @return int[] berisi [0/1 (found), bucket_index]
     */
    public int[] search(int value) {
        int index = hashFunction(value);
        Node current = table[index];
        int position = 1; // Posisi dalam chain (1-based)

        while (current != null) {
            if (current.data == value) {
                return new int[]{1, index, position}; // Ditemukan
            }
            current = current.next;
            position++;
        }
        return new int[]{0, index, 0}; // Tidak ditemukan
    }

    /**
     * Tampilkan semua data dalam hash table secara terurut ascending
     * Time Complexity: O(n log n) karena sorting
     */
    public void display() {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 0; i < TABLE_SIZE; i++) {
            Node current = table[i];
            while (current != null) {
                values.add(current.data);
                current = current.next;
            }
        }
        Collections.sort(values);

        double loadFactor = (double) count / TABLE_SIZE;

        System.out.println("\n\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
        System.out.println("\u2551          DAFTAR DATA NUMERIK DALAM HASH TABLE             \u2551");
        System.out.println("\u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563");
        System.out.printf("  Total data  : %d%n", count);
        System.out.printf("  Table size  : %d%n", TABLE_SIZE);
        System.out.printf("  Load factor : %.4f%n", loadFactor);
        System.out.println("  --------------------------------------------------------");

        if (values.isEmpty()) {
            System.out.println("  Hash table kosong!");
        } else {
            for (int i = 0; i < values.size(); i++) {
                System.out.printf("  %6d", values.get(i));
                if ((i + 1) % 10 == 0) {
                    System.out.println();
                }
            }
            if (values.size() % 10 != 0) {
                System.out.println();
            }
        }
        System.out.println("\u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d\n");
    }

    /**
     * Tampilkan visualisasi internal hash table (bucket view)
     * Hanya menampilkan bucket yang tidak kosong
     */
    public void displayInternal() {
        System.out.println("\n\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
        System.out.println("\u2551            VISUALISASI INTERNAL HASH TABLE                \u2551");
        System.out.println("\u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563");

        int nonEmpty = 0;
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (table[i] != null) {
                nonEmpty++;
                System.out.printf("  Bucket[%3d]: ", i);
                Node current = table[i];
                StringBuilder sb = new StringBuilder();
                while (current != null) {
                    sb.append(current.data);
                    if (current.next != null) {
                        sb.append(" -> ");
                    }
                    current = current.next;
                }
                System.out.println(sb);
            }
        }

        System.out.println("  --------------------------------------------------------");
        System.out.printf("  Bucket terpakai: %d / %d%n", nonEmpty, TABLE_SIZE);
        System.out.printf("  Bucket kosong  : %d / %d%n", TABLE_SIZE - nonEmpty, TABLE_SIZE);
        System.out.println("\u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d\n");
    }

    /** @return Jumlah elemen yang tersimpan */
    public int getCount() {
        return count;
    }
}

/**
 * Class javaX - Program Utama Hash Table
 *
 * Implementasi Hash Table dengan Separate Chaining untuk menyimpan
 * himpunan data numerik. Program secara otomatis mengisi 100 angka
 * random unik saat dijalankan.
 *
 * Fitur:
 * 1. Input Data  - Masukkan angka baru ke hash table
 * 2. Hapus Data  - Hapus angka dari hash table
 * 3. Cari Data   - Cari apakah angka ada di hash table
 * 4. Tampilkan   - Lihat semua data secara terurut
 * 5. Visualisasi - Lihat struktur internal bucket per bucket
 */
public class javaX {

    /**
     * Generate 100 angka random unik (range 1000-9999) dan masukkan ke hash table
     * @param ht HashTable yang akan diisi data awal
     */
    private static void generateRandomData(HashTable ht) {
        Random random = new Random();
        java.util.HashSet<Integer> generated = new java.util.HashSet<>();

        System.out.println("  Generating 100 data random unik (range 1000-9999)...");
        while (generated.size() < 100) {
            int val = random.nextInt(9000) + 1000; // Range: 1000 - 9999
            if (generated.add(val)) {
                ht.insert(val);
            }
        }
        System.out.println("  \u2713 100 data random berhasil diinputkan!\n");
    }

    /**
     * Tampilkan menu utama program
     */
    private static void displayMenu() {
        System.out.println("\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
        System.out.println("\u2551     SISTEM MANAJEMEN DATA NUMERIK - HASH TABLE            \u2551");
        System.out.println("\u2551       Collision Technique: Separate Chaining              \u2551");
        System.out.println("\u2560\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2563");
        System.out.println("\u2551  1. Input Data  (Insert)                                  \u2551");
        System.out.println("\u2551  2. Hapus Data  (Delete)                                  \u2551");
        System.out.println("\u2551  3. Cari Data   (Search)                                  \u2551");
        System.out.println("\u2551  4. Tampilkan Semua Data                                  \u2551");
        System.out.println("\u2551  5. Visualisasi Internal Hash Table                       \u2551");
        System.out.println("\u2551  0. Keluar                                                \u2551");
        System.out.println("\u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d");
    }

    /**
     * Main method - entry point program
     * @param args Arguments (tidak digunakan)
     */
    public static void main(String[] args) {
        HashTable ht = new HashTable();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550");
        System.out.println("       PROGRAM HASH TABLE - HIMPUNAN DATA NUMERIK");
        System.out.println("\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550");
        System.out.println("  Teknik Collision : Separate Chaining (Open Hashing)");
        System.out.println("  Hash Function    : h(key) = |key| % 151");
        System.out.println("  Ukuran Tabel     : 151 bucket (bilangan prima)");
        System.out.println("\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550");

        // Pre-load 100 angka random unik saat program dimulai
        generateRandomData(ht);

        boolean running = true;
        while (running) {
            displayMenu();
            System.out.print("Pilih menu (0-5): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Bersihkan buffer newline

                switch (choice) {
                    case 1: // Input Data
                        System.out.print("Masukkan angka yang akan diinput: ");
                        int insertVal = scanner.nextInt();
                        scanner.nextLine();
                        if (ht.insert(insertVal)) {
                            System.out.println("\u2713 Angka " + insertVal + " berhasil diinputkan!");
                            System.out.println("  \u2192 Disimpan di bucket: " + (Math.abs(insertVal) % 151));
                        } else {
                            System.out.println("\u2717 Angka " + insertVal + " sudah ada dalam hash table! (duplikat ditolak)");
                        }
                        break;

                    case 2: // Hapus Data
                        System.out.print("Masukkan angka yang akan dihapus: ");
                        int deleteVal = scanner.nextInt();
                        scanner.nextLine();
                        if (ht.delete(deleteVal)) {
                            System.out.println("\u2713 Angka " + deleteVal + " berhasil dihapus!");
                        } else {
                            System.out.println("\u2717 Angka " + deleteVal + " tidak ditemukan dalam hash table!");
                        }
                        break;

                    case 3: // Cari Data
                        System.out.print("Masukkan angka yang akan dicari: ");
                        int searchVal = scanner.nextInt();
                        scanner.nextLine();
                        int[] result = ht.search(searchVal);
                        if (result[0] == 1) {
                            System.out.println("\u2713 Angka " + searchVal + " DITEMUKAN dalam hash table!");
                            System.out.println("  \u2192 Posisi bucket : " + result[1]);
                            System.out.println("  \u2192 Posisi di chain: ke-" + result[2]);
                        } else {
                            System.out.println("\u2717 Angka " + searchVal + " TIDAK DITEMUKAN dalam hash table!");
                            System.out.println("  \u2192 Dicek di bucket: " + result[1] + " (kosong / tidak cocok)");
                        }
                        break;

                    case 4: // Tampilkan semua data
                        ht.display();
                        break;

                    case 5: // Visualisasi internal
                        ht.displayInternal();
                        break;

                    case 0: // Keluar
                        System.out.println("\n\u2554\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2557");
                        System.out.println("\u2551  Terima kasih telah menggunakan program ini!               \u2551");
                        System.out.println("\u255a\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u255d");
                        running = false;
                        break;

                    default:
                        System.out.println("\u2717 Pilihan tidak valid! Silakan pilih 0-5.");
                }

            } catch (InputMismatchException e) {
                System.out.println("\u2717 Input tidak valid! Masukkan angka bulat.");
                scanner.nextLine(); // Bersihkan buffer
            }
        }

        scanner.close();
    }
}
