import java.util.Scanner;

class Mahasiswa {
    java.lang.String nim;
    java.lang.String nama;

    public Mahasiswa(java.lang.String nim, java.lang.String nama) {
        this.nim = nim;
        this.nama = nama;
    }

    @Override
    public java.lang.String toString() {
        return "NIM: " + nim + ", Nama: " + nama;
    }
}

public class MahasiswaAppString {
    private static final int CAPACITY = 10;
    private final Mahasiswa[] data;
    private int count;

    public MahasiswaAppString() {
        data = new Mahasiswa[CAPACITY];
        count = 0;
    }

    // Insert at beginning
    public void insertAtBeginning(java.lang.String nim, java.lang.String nama) {
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
    public void insertAtPosition(int position, java.lang.String nim, java.lang.String nama) {
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
    public void insertAtEnd(java.lang.String nim, java.lang.String nama) {
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
    public void deleteFirstOccurrence(java.lang.String nim) {
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
        System.out.println("1. Insert at beginning");
        System.out.println("2. Insert at given position");
        System.out.println("3. Insert at end");
        System.out.println("4. Delete from beginning");
        System.out.println("5. Delete given position");
        System.out.println("6. Delete from end");
        System.out.println("7. Delete first occurence");
        System.out.println("8. Show data");
        System.out.println("9. Exit");
        System.out.println("========================================");
    }

    public static void main(java.lang.String[] args) {
        Scanner scanner = new Scanner(System.in);
        MahasiswaAppString managementSystem = new MahasiswaAppString();
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
                case 1:
                    System.out.print("Masukkan NIM: ");
                    java.lang.String nim1 = scanner.nextLine();
                    System.out.print("Masukkan Nama: ");
                    java.lang.String nama1 = scanner.nextLine();
                    managementSystem.insertAtBeginning(nim1, nama1);
                    break;

                case 2:
                    System.out.print("Masukkan posisi (0 - " + managementSystem.count + "): ");
                    int position = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Masukkan NIM: ");
                    java.lang.String nim2 = scanner.nextLine();
                    System.out.print("Masukkan Nama: ");
                    java.lang.String nama2 = scanner.nextLine();
                    managementSystem.insertAtPosition(position, nim2, nama2);
                    break;

                case 3:
                    System.out.print("Masukkan NIM: ");
                    java.lang.String nim3 = scanner.nextLine();
                    System.out.print("Masukkan Nama: ");
                    java.lang.String nama3 = scanner.nextLine();
                    managementSystem.insertAtEnd(nim3, nama3);
                    break;

                case 4:
                    managementSystem.deleteFromBeginning();
                    break;

                case 5:
                    System.out.print("Masukkan posisi (0 - " + (managementSystem.count - 1) + "): ");
                    int delPosition = scanner.nextInt();
                    scanner.nextLine();
                    managementSystem.deleteFromPosition(delPosition);
                    break;

                case 6:
                    managementSystem.deleteFromEnd();
                    break;

                case 7:
                    System.out.print("Masukkan NIM yang ingin dihapus: ");
                    java.lang.String nimToDelete = scanner.nextLine();
                    managementSystem.deleteFirstOccurrence(nimToDelete);
                    break;

                case 8:
                    managementSystem.showData();
                    break;

                case 9:
                    running = false;
                    System.out.println("Terima kasih! Program selesai.");
                    break;

                default:
                    System.out.println("Menu tidak valid! Silakan pilih menu 1-9.");
            }
        }
        scanner.close();
    }
}
