package heap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class Barang untuk merepresentasikan data barang
 * Menyimpan informasi ID dan Nama barang
 */
class Barang implements Comparable<Barang> {
    int id;      // ID Barang
    String nama; // Nama barang

    /**
     * Constructor untuk membuat objek Barang baru
     * @param id ID Barang
     * @param nama Nama barang
     */
    public Barang(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    /**
     * Override method toString
     * @return String representasi data barang
     */
    @Override
    public String toString() {
        return "ID: " + id + ", Nama: " + nama;
    }

    /**
     * Implementasi Comparable untuk perbandingan berdasarkan ID
     * @param other Barang lain untuk dibandingkan
     * @return Hasil perbandingan (-1, 0, 1)
     */
    @Override
    public int compareTo(Barang other) {
        return Integer.compare(this.id, other.id);
    }
}

/**
 * Class MinHeap untuk implementasi Min-Heap
 * 
 * Min-Heap adalah struktur data binary tree yang memenuhi min-heap property:
 * - Parent node <= Children nodes
 * - Root adalah elemen terkecil
 * 
 * Direpresentasikan menggunakan array dengan:
 * - Index 0: root
 * - Index i: node parent
 * - Index 2*i+1: left child
 * - Index 2*i+2: right child
 */
class MinHeap {
    private ArrayList<Barang> heap;

    /**
     * Constructor - Inisialisasi Min-Heap kosong
     */
    public MinHeap() {
        heap = new ArrayList<>();
    }

    /**
     * Mendapatkan index parent dari node di index i
     * @param i Index node
     * @return Index parent
     */
    private int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Mendapatkan index left child dari node di index i
     * @param i Index node
     * @return Index left child
     */
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * Mendapatkan index right child dari node di index i
     * @param i Index node
     * @return Index right child
     */
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    /**
     * Menukar dua elemen dalam heap
     * @param i Index elemen pertama
     * @param j Index elemen kedua
     */
    private void swap(int i, int j) {
        Barang temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Memindahkan elemen ke atas untuk mempertahankan min-heap property
     * Time Complexity: O(log n)
     * @param i Index elemen yang akan diheapify
     */
    private void heapifyUp(int i) {
        while (i > 0 && heap.get(parent(i)).compareTo(heap.get(i)) > 0) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * Memindahkan elemen ke bawah untuk mempertahankan min-heap property
     * Time Complexity: O(log n)
     * @param i Index elemen yang akan diheapify
     */
    private void heapifyDown(int i) {
        int smallest = i;
        int left = leftChild(i);
        int right = rightChild(i);

        if (left < heap.size() && heap.get(left).compareTo(heap.get(smallest)) < 0) {
            smallest = left;
        }

        if (right < heap.size() && heap.get(right).compareTo(heap.get(smallest)) < 0) {
            smallest = right;
        }

        if (smallest != i) {
            swap(i, smallest);
            heapifyDown(smallest);
        }
    }

    /**
     * Menambahkan data barang ke dalam min-heap
     * Time Complexity: O(log n)
     * @param barang Data barang yang akan ditambahkan
     */
    public void insert(Barang barang) {
        heap.add(barang);
        heapifyUp(heap.size() - 1);
    }

    /**
     * Menghapus elemen terkecil (root) dari min-heap
     * Time Complexity: O(log n)
     * @return Elemen yang dihapus atau null jika heap kosong
     */
    public Barang deleteMin() {
        if (heap.isEmpty()) {
            return null;
        }

        if (heap.size() == 1) {
            return heap.remove(0);
        }

        Barang root = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        heapifyDown(0);

        return root;
    }

    /**
     * Menghapus data barang berdasarkan ID dari min-heap
     * Time Complexity: O(n)
     * @param id ID barang yang akan dihapus
     * @return true jika berhasil dihapus, false jika tidak ditemukan
     */
    public boolean deleteById(int id) {
        int index = -1;
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).id == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        if (index == heap.size() - 1) {
            heap.remove(index);
        } else {
            heap.set(index, heap.remove(heap.size() - 1));
            if (index > 0 && heap.get(index).compareTo(heap.get(parent(index))) < 0) {
                heapifyUp(index);
            } else {
                heapifyDown(index);
            }
        }

        return true;
    }

    /**
     * Mendapatkan semua elemen dalam urutan ascending (sorted)
     * Time Complexity: O(n log n)
     * @return List berisi semua elemen yang sudah diurutkan ascending
     */
    public List<Barang> getSorted() {
        List<Barang> result = new ArrayList<>();
        ArrayList<Barang> tempHeap = new ArrayList<>(heap);

        while (!tempHeap.isEmpty()) {
            Barang root = tempHeap.get(0);
            result.add(root);
            tempHeap.set(0, tempHeap.remove(tempHeap.size() - 1));

            int i = 0;
            while (true) {
                int smallest = i;
                int left = 2 * i + 1;
                int right = 2 * i + 2;

                if (left < tempHeap.size() && tempHeap.get(left).compareTo(tempHeap.get(smallest)) < 0) {
                    smallest = left;
                }

                if (right < tempHeap.size() && tempHeap.get(right).compareTo(tempHeap.get(smallest)) < 0) {
                    smallest = right;
                }

                if (smallest != i) {
                    Barang temp = tempHeap.get(i);
                    tempHeap.set(i, tempHeap.get(smallest));
                    tempHeap.set(smallest, temp);
                    i = smallest;
                } else {
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Cek apakah heap kosong
     * @return true jika heap kosong
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Mendapatkan jumlah elemen dalam heap
     * @return Jumlah elemen
     */
    public int size() {
        return heap.size();
    }

    /**
     * Mendapatkan reference ke ArrayList heap (untuk keperluan tertentu)
     * @return ArrayList heap
     */
    public ArrayList<Barang> getHeap() {
        return heap;
    }
}

/**
 * Class MaxHeap untuk implementasi Max-Heap
 * 
 * Max-Heap adalah struktur data binary tree yang memenuhi max-heap property:
 * - Parent node >= Children nodes
 * - Root adalah elemen terbesar
 * 
 * Direpresentasikan menggunakan array dengan:
 * - Index 0: root
 * - Index i: node parent
 * - Index 2*i+1: left child
 * - Index 2*i+2: right child
 */
class MaxHeap {
    private ArrayList<Barang> heap;

    /**
     * Constructor - Inisialisasi Max-Heap kosong
     */
    public MaxHeap() {
        heap = new ArrayList<>();
    }

    /**
     * Mendapatkan index parent dari node di index i
     * @param i Index node
     * @return Index parent
     */
    private int parent(int i) {
        return (i - 1) / 2;
    }

    /**
     * Mendapatkan index left child dari node di index i
     * @param i Index node
     * @return Index left child
     */
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * Mendapatkan index right child dari node di index i
     * @param i Index node
     * @return Index right child
     */
    private int rightChild(int i) {
        return 2 * i + 2;
    }

    /**
     * Menukar dua elemen dalam heap
     * @param i Index elemen pertama
     * @param j Index elemen kedua
     */
    private void swap(int i, int j) {
        Barang temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Memindahkan elemen ke atas untuk mempertahankan max-heap property
     * Time Complexity: O(log n)
     * @param i Index elemen yang akan diheapify
     */
    private void heapifyUp(int i) {
        while (i > 0 && heap.get(parent(i)).compareTo(heap.get(i)) < 0) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * Memindahkan elemen ke bawah untuk mempertahankan max-heap property
     * Time Complexity: O(log n)
     * @param i Index elemen yang akan diheapify
     */
    private void heapifyDown(int i) {
        int largest = i;
        int left = leftChild(i);
        int right = rightChild(i);

        if (left < heap.size() && heap.get(left).compareTo(heap.get(largest)) > 0) {
            largest = left;
        }

        if (right < heap.size() && heap.get(right).compareTo(heap.get(largest)) > 0) {
            largest = right;
        }

        if (largest != i) {
            swap(i, largest);
            heapifyDown(largest);
        }
    }

    /**
     * Menambahkan data barang ke dalam max-heap
     * Time Complexity: O(log n)
     * @param barang Data barang yang akan ditambahkan
     */
    public void insert(Barang barang) {
        heap.add(barang);
        heapifyUp(heap.size() - 1);
    }

    /**
     * Menghapus elemen terbesar (root) dari max-heap
     * Time Complexity: O(log n)
     * @return Elemen yang dihapus atau null jika heap kosong
     */
    public Barang deleteMax() {
        if (heap.isEmpty()) {
            return null;
        }

        if (heap.size() == 1) {
            return heap.remove(0);
        }

        Barang root = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        heapifyDown(0);

        return root;
    }

    /**
     * Menghapus data barang berdasarkan ID dari max-heap
     * Time Complexity: O(n)
     * @param id ID barang yang akan dihapus
     * @return true jika berhasil dihapus, false jika tidak ditemukan
     */
    public boolean deleteById(int id) {
        int index = -1;
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).id == id) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        if (index == heap.size() - 1) {
            heap.remove(index);
        } else {
            heap.set(index, heap.remove(heap.size() - 1));
            if (index > 0 && heap.get(index).compareTo(heap.get(parent(index))) > 0) {
                heapifyUp(index);
            } else {
                heapifyDown(index);
            }
        }

        return true;
    }

    /**
     * Mendapatkan semua elemen dalam urutan descending (sorted)
     * Time Complexity: O(n log n)
     * @return List berisi semua elemen yang sudah diurutkan descending
     */
    public List<Barang> getSortedDescending() {
        List<Barang> result = new ArrayList<>();
        ArrayList<Barang> tempHeap = new ArrayList<>(heap);

        while (!tempHeap.isEmpty()) {
            Barang root = tempHeap.get(0);
            result.add(root);
            tempHeap.set(0, tempHeap.remove(tempHeap.size() - 1));

            int i = 0;
            while (true) {
                int largest = i;
                int left = 2 * i + 1;
                int right = 2 * i + 2;

                if (left < tempHeap.size() && tempHeap.get(left).compareTo(tempHeap.get(largest)) > 0) {
                    largest = left;
                }

                if (right < tempHeap.size() && tempHeap.get(right).compareTo(tempHeap.get(largest)) > 0) {
                    largest = right;
                }

                if (largest != i) {
                    Barang temp = tempHeap.get(i);
                    tempHeap.set(i, tempHeap.get(largest));
                    tempHeap.set(largest, temp);
                    i = largest;
                } else {
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Cek apakah heap kosong
     * @return true jika heap kosong
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Mendapatkan jumlah elemen dalam heap
     * @return Jumlah elemen
     */
    public int size() {
        return heap.size();
    }

    /**
     * Mendapatkan reference ke ArrayList heap (untuk keperluan tertentu)
     * @return ArrayList heap
     */
    public ArrayList<Barang> getHeap() {
        return heap;
    }
}

/**
 * Class javaX - Sistem Manajemen Data Barang menggunakan Heap (Min-Heap dan Max-Heap)
 * 
 * Implementasi struktur data Heap yang mendukung berbagai operasi:
 * - Insert: menambahkan data barang ke kedua heap sekaligus
 * - Delete: menghapus data dari min-heap atau max-heap
 * - Display: menampilkan data terurut ascending/descending
 * - Load CSV: membaca data dari file CSV
 */
public class javaX {
    private MinHeap minHeap;
    private MaxHeap maxHeap;

    /**
     * Constructor - Inisialisasi kedua heap
     */
    public javaX() {
        minHeap = new MinHeap();
        maxHeap = new MaxHeap();
    }

    /**
     * Menambahkan data barang ke kedua min-heap dan max-heap sekaligus
     * Time Complexity: O(log n)
     * @param id ID barang
     * @param nama Nama barang
     */
    public boolean insert(int id, String nama) {
        // Cek duplikasi
        for (Barang b : minHeap.getHeap()) {
            if (b.id == id) {
                System.out.println("Error: ID sudah ada dalam heap!");
                return false;
            }
        }

        Barang barang = new Barang(id, nama);
        minHeap.insert(barang);
        maxHeap.insert(new Barang(id, nama));
        System.out.println("✓ Data berhasil ditambahkan ke Min-Heap dan Max-Heap!");
        return true;
    }

    /**
     * Menampilkan data urut berdasarkan ID secara ascending menggunakan Min-Heap
     * Time Complexity: O(n log n)
     */
    public void displayAscending() {
        if (minHeap.isEmpty()) {
            System.out.println("\n✗ Heap kosong! Tidak ada data untuk ditampilkan.");
            return;
        }

        List<Barang> sortedData = minHeap.getSorted();

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     DAFTAR DATA BARANG (ASCENDING - MIN-HEAP)             ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("  Total data: " + sortedData.size());
        System.out.println("────────────────────────────────────────────────────────────");

        for (int i = 0; i < sortedData.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + sortedData.get(i));
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Menampilkan data urut berdasarkan ID secara descending menggunakan Max-Heap
     * Time Complexity: O(n log n)
     */
    public void displayDescending() {
        if (maxHeap.isEmpty()) {
            System.out.println("\n✗ Heap kosong! Tidak ada data untuk ditampilkan.");
            return;
        }

        List<Barang> sortedData = maxHeap.getSortedDescending();

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║    DAFTAR DATA BARANG (DESCENDING - MAX-HEAP)             ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("  Total data: " + sortedData.size());
        System.out.println("────────────────────────────────────────────────────────────");

        for (int i = 0; i < sortedData.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + sortedData.get(i));
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Menghapus data dari min-heap berdasarkan ID
     * Time Complexity: O(n)
     * @param id ID barang yang akan dihapus
     */
    public void deleteFromMinHeap(int id) {
        if (minHeap.deleteById(id)) {
            System.out.println("✓ Data berhasil dihapus dari Min-Heap!");
        } else {
            System.out.println("✗ Data dengan ID " + id + " tidak ditemukan di Min-Heap!");
        }
    }

    /**
     * Menghapus data dari max-heap berdasarkan ID
     * Time Complexity: O(n)
     * @param id ID barang yang akan dihapus
     */
    public void deleteFromMaxHeap(int id) {
        if (maxHeap.deleteById(id)) {
            System.out.println("✓ Data berhasil dihapus dari Max-Heap!");
        } else {
            System.out.println("✗ Data dengan ID " + id + " tidak ditemukan di Max-Heap!");
        }
    }

    /**
     * Load data barang dari file CSV ke kedua min-heap dan max-heap
     * Format CSV: ID,Nama (dengan header)
     * @param filename Path ke file CSV
     * @return Jumlah data yang berhasil ditambahkan
     */
    public int loadFromCSV(String filename) {
        // Cek beberapa kemungkinan lokasi file
        File csvFile = null;
        String[] possiblePaths = {
            filename,
            "heap" + File.separator + filename,
            "." + File.separator + filename,
            "." + File.separator + "heap" + File.separator + filename
        };

        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists()) {
                csvFile = f;
                break;
            }
        }

        if (csvFile == null) {
            System.out.println("Error: File '" + filename + "' tidak ditemukan!");
            System.out.println("Lokasi yang dicari:");
            for (String path : possiblePaths) {
                File f = new File(path);
                System.out.println("  - " + f.getAbsolutePath());
            }
            return 0;
        }

        int successCount = 0;
        int errorCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean isFirstLine = true;

            System.out.println("\nMembaca file CSV dari: " + csvFile.getAbsolutePath());

            while ((line = br.readLine()) != null) {
                // Skip header
                if (isFirstLine) {
                    isFirstLine = false;
                    if (!line.trim().startsWith("ID,Nama")) {
                        System.out.println("Error: Format CSV tidak sesuai. Header harus berisi 'ID,Nama'");
                        return 0;
                    }
                    continue;
                }

                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = null;
                try {
                    parts = line.split(",", 2);

                    if (parts.length < 2) {
                        System.out.println("  Warning: Format baris tidak sesuai, dilewati.");
                        errorCount++;
                        continue;
                    }

                    int idBarang = Integer.parseInt(parts[0].trim());
                    String namaBarang = parts[1].trim();

                    if (namaBarang.isEmpty()) {
                        System.out.println("  Warning: Row dengan ID " + idBarang + " memiliki nama kosong, dilewati.");
                        errorCount++;
                        continue;
                    }

                    // Cek duplikasi
                    boolean duplicate = false;
                    for (Barang b : minHeap.getHeap()) {
                        if (b.id == idBarang) {
                            System.out.println("  Warning: ID " + idBarang + " sudah ada, dilewati.");
                            duplicate = true;
                            errorCount++;
                            break;
                        }
                    }

                    if (duplicate) {
                        continue;
                    }

                    Barang barang = new Barang(idBarang, namaBarang);
                    minHeap.insert(barang);
                    maxHeap.insert(new Barang(idBarang, namaBarang));
                    successCount++;

                } catch (NumberFormatException e) {
                    if (parts != null && parts.length > 0) {
                        System.out.println("  Warning: ID '" + parts[0] + "' bukan angka, dilewati.");
                    } else {
                        System.out.println("  Warning: Format data tidak sesuai, dilewati.");
                    }
                    errorCount++;
                }
            }

            if (successCount > 0) {
                System.out.println("\n✓ Berhasil menambahkan " + successCount + " data dari file CSV");
                if (errorCount > 0) {
                    System.out.println("  (" + errorCount + " data gagal/dilewati)");
                }
            } else {
                System.out.println("\n✗ Gagal menambahkan data dari CSV");
            }

            return successCount;

        } catch (IOException e) {
            System.out.println("Error membaca file CSV: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Menampilkan menu utama program
     */
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       SISTEM MANAJEMEN DATA BARANG - MIN-HEAP & MAX-HEAP   ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Tambah Data (Insert)                                   ║");
        System.out.println("║  2. Tampilkan Data (Ascending - Min-Heap)                  ║");
        System.out.println("║  3. Tampilkan Data (Descending - Max-Heap)                 ║");
        System.out.println("║  4. Hapus Data dari Min-Heap                               ║");
        System.out.println("║  5. Hapus Data dari Max-Heap                               ║");
        System.out.println("║  6. Tambah Data dari File CSV                              ║");
        System.out.println("║  0. Keluar                                                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Main method - entry point program
     * @param args Arguments (tidak digunakan)
     */
    public static void main(String[] args) {
        javaX heapApp = new javaX();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            System.out.println("════════════════════════════════════════════════════════════");
            System.out.println("       PROGRAM SISTEM MANAJEMEN DATA BARANG - HEAP");
            System.out.println("════════════════════════════════════════════════════════════");
            System.out.println("  Menggunakan Min-Heap dan Max-Heap");
            System.out.println("════════════════════════════════════════════════════════════\n");

            while (running) {
                heapApp.displayMenu();
                System.out.print("Pilih menu (0-6): ");

                try {
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer

                    switch (choice) {
                        case 1:
                            System.out.print("Masukkan ID Barang: ");
                            int id = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Masukkan Nama Barang: ");
                            String nama = scanner.nextLine();
                            heapApp.insert(id, nama);
                            break;

                        case 2:
                            heapApp.displayAscending();
                            break;

                        case 3:
                            heapApp.displayDescending();
                            break;

                        case 4:
                            System.out.print("Masukkan ID Barang yang akan dihapus dari Min-Heap: ");
                            int deleteIdMin = scanner.nextInt();
                            scanner.nextLine();
                            heapApp.deleteFromMinHeap(deleteIdMin);
                            break;

                        case 5:
                            System.out.print("Masukkan ID Barang yang akan dihapus dari Max-Heap: ");
                            int deleteIdMax = scanner.nextInt();
                            scanner.nextLine();
                            heapApp.deleteFromMaxHeap(deleteIdMax);
                            break;

                        case 6:
                            System.out.print("Masukkan nama file CSV (default: data.csv): ");
                            String filename = scanner.nextLine().trim();
                            if (filename.isEmpty()) {
                                filename = "data.csv";
                            }
                            heapApp.loadFromCSV(filename);
                            break;

                        case 0:
                            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
                            System.out.println("║  Terima kasih telah menggunakan program ini!               ║");
                            System.out.println("╚════════════════════════════════════════════════════════════╝\n");
                            running = false;
                            break;

                        default:
                            System.out.println("✗ Pilihan tidak valid! Silakan pilih 0-6.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: Input tidak valid!");
                    scanner.nextLine(); // Clear buffer
                }

                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
