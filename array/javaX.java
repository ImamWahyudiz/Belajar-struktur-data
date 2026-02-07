package array;

import java.util.Scanner;

class Mahasiswa {
    String nim;
    String nama;

    public Mahasiswa(String nim, String nama) {
        this.nim = nim;
        this.nama = nama;
    }

    @Override
    public String toString() {
        return "NIM: " + nim + ", Nama: " + nama;
    }
}

public class javaX {
    private static final int CAPACITY = 10;
    private final Mahasiswa[] data;
    private int count;

    public javaX() {
        data = new Mahasiswa[CAPACITY];
        count = 0;
    }

    // Insert at beginning
    public void insertAtBeginning(String nim, String nama) {
        if (count >= CAPACITY) {
            System.out.println("Array penuh! Tidak bisa menambah data.");
            return;
        }
        // Shift semua elemen ke kanan
        for (int i = count; i > 0; i--) {
            data[i] = data[i - 1];
        }
        data[0] = new Mahasiswa(nim, nama);
        count++;
        System.out.println("Data berhasil ditambahkan di awal!");
    }

    // Insert at given position
    public void insertAtPosition(int position, String nim, String nama) {
        if (position < 0 || position > count) {
            System.out.println("Posisi tidak valid! (0 - " + count + ")");
            return;
        }
        if (count >= CAPACITY) {
            System.out.println("Array penuh! Tidak bisa menambah data.");
            return;
        }
        // Shift elemen dari posisi ke kanan
        for (int i = count; i > position; i--) {
            data[i] = data[i - 1];
        }
        data[position] = new Mahasiswa(nim, nama);
        count++;
        System.out.println("Data berhasil ditambahkan di posisi " + position + "!");
    }

    // Insert at end
    public void insertAtEnd(String nim, String nama) {
        if (count >= CAPACITY) {
            System.out.println("Array penuh! Tidak bisa menambah data.");
            return;
        }
        data[count] = new Mahasiswa(nim, nama);
        count++;
        System.out.println("Data berhasil ditambahkan di akhir!");
    }

    // Delete from beginning
    public void deleteFromBeginning() {
        if (count == 0) {
            System.out.println("Array kosong! Tidak ada data yang dihapus.");
            return;
        }
        // Shift semua elemen ke kiri
        for (int i = 0; i < count - 1; i++) {
            data[i] = data[i + 1];
        }
        data[count - 1] = null;
        count--;
        System.out.println("Data berhasil dihapus dari awal!");
    }

    // Delete from given position
    public void deleteFromPosition(int position) {
        if (position < 0 || position >= count) {
            System.out.println("Posisi tidak valid! (0 - " + (count - 1) + ")");
            return;
        }
        // Shift elemen dari posisi ke kiri
        for (int i = position; i < count - 1; i++) {
            data[i] = data[i + 1];
        }
        data[count - 1] = null;
        count--;
        System.out.println("Data berhasil dihapus dari posisi " + position + "!");
    }

    // Delete from end
    public void deleteFromEnd() {
        if (count == 0) {
            System.out.println("Array kosong! Tidak ada data yang dihapus.");
            return;
        }
        data[count - 1] = null;
        count--;
        System.out.println("Data berhasil dihapus dari akhir!");
    }

    // Delete first occurrence
    public void deleteFirstOccurrence(String nim) {
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (data[i].nim.equals(nim)) {
                // Shift elemen dari posisi ke kiri
                for (int j = i; j < count - 1; j++) {
                    data[j] = data[j + 1];
                }
                data[count - 1] = null;
                count--;
                System.out.println("Data dengan NIM " + nim + " berhasil dihapus!");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Data dengan NIM " + nim + " tidak ditemukan!");
        }
    }

    // Show all data
    public void showData() {
        if (count == 0) {
            System.out.println("Array kosong! Tidak ada data untuk ditampilkan.");
            return;
        }
        System.out.println("\n========== DATA MAHASISWA ==========");
        System.out.println("Jumlah data: " + count);
        for (int i = 0; i < count; i++) {
            System.out.println((i + 1) + ". " + data[i]);
        }
        System.out.println("===================================\n");
    }

    // Display menu
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

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            javaX managementSystem = new javaX();
            int choice;
            boolean running = true;

            System.out.println("=== PROGRAM MANAJEMEN DATA MAHASISWA ===");
            System.out.println("Kapasitas Array: " + CAPACITY);
            System.out.println("=========================================\n");

            while (running) {
                managementSystem.displayMenu();
                System.out.print("Pilih menu (1-9): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> {
                        System.out.print("Masukkan NIM: ");
                        String nim1 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama1 = scanner.nextLine();
                        managementSystem.insertAtBeginning(nim1, nama1);
                    }

                    case 2 -> {
                        System.out.print("Masukkan posisi (0 - " + managementSystem.count + "): ");
                        int position = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Masukkan NIM: ");
                        String nim2 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama2 = scanner.nextLine();
                        managementSystem.insertAtPosition(position, nim2, nama2);
                    }

                    case 3 -> {
                        System.out.print("Masukkan NIM: ");
                        String nim3 = scanner.nextLine();
                        System.out.print("Masukkan Nama: ");
                        String nama3 = scanner.nextLine();
                        managementSystem.insertAtEnd(nim3, nama3);
                    }

                    case 4 -> managementSystem.deleteFromBeginning();

                    case 5 -> {
                        System.out.print("Masukkan posisi (0 - " + (managementSystem.count - 1) + "): ");
                        int delPosition = scanner.nextInt();
                        scanner.nextLine();
                        managementSystem.deleteFromPosition(delPosition);
                    }

                    case 6 -> managementSystem.deleteFromEnd();

                    case 7 -> {
                        System.out.print("Masukkan NIM yang ingin dihapus: ");
                        String nimToDelete = scanner.nextLine();
                        managementSystem.deleteFirstOccurrence(nimToDelete);
                    }

                    case 8 -> managementSystem.showData();

                    case 9 -> {
                        running = false;
                        System.out.println("Terima kasih! Program selesai.");
                    }

                    default -> System.out.println("Menu tidak valid! Silakan pilih menu 1-9.");
                }
            }
        }
    }
}
