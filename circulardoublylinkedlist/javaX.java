package circulardoublylinkedlist;

import java.util.Scanner;

/**
 * Class Node untuk merepresentasikan satu node dalam Circular Doubly Linked List
 * Setiap node berisi data berita dan pointer ke node sebelumnya dan berikutnya
 */
class Node {
    String berita;   // Isi teks berita
    Node prev;       // Pointer ke node sebelumnya
    Node next;       // Pointer ke node berikutnya

    /**
     * Constructor untuk membuat node baru
     * @param berita Teks berita yang akan disimpan
     */
    public Node(String berita) {
        this.berita = berita;
        this.prev = null;
        this.next = null;
    }
}

/**
 * Class javaX - Sistem Simulasi Teks Berjalan Berita
 * menggunakan Circular Doubly Linked List
 * 
 * Implementasi struktur data Circular Doubly Linked List tanpa menggunakan library
 * yang mendukung berbagai operasi insert, delete, dan display dengan animasi.
 * 
 * Fitur utama:
 * - Insert berita di akhir
 * - Delete berita berdasarkan nomor urut
 * - Display forward (depan ke belakang dengan delay)
 * - Display backward (belakang ke depan dengan delay)
 * - Display berita tertentu
 * 
 * Karakteristik Circular Doubly Linked List:
 * - Node terakhir menunjuk kembali ke node pertama (circular)
 * - Node pertama menunjuk kembali ke node terakhir (circular)
 * - Bisa traversal dua arah (forward dan backward)
 */
public class javaX {
    private Node head;   // Pointer ke node pertama dalam list
    private int count;   // Jumlah berita dalam list

    /**
     * Constructor untuk inisialisasi Circular Doubly Linked List kosong
     */
    public javaX() {
        this.head = null;
        this.count = 0;
    }

    /**
     * Menambahkan berita baru di akhir list
     * Time Complexity: O(n) karena perlu traverse ke akhir
     * 
     * @param berita Teks berita yang akan ditambahkan
     */
    public void insertBerita(String berita) {
        Node newNode = new Node(berita);
        
        // Jika list kosong
        if (head == null) {
            head = newNode;
            head.next = head;  // Menunjuk ke dirinya sendiri (circular)
            head.prev = head;  // Menunjuk ke dirinya sendiri (circular)
            count++;
            System.out.println("✓ Berita berhasil ditambahkan!");
            System.out.println("  Total berita: " + count);
            return;
        }
        
        // Dapatkan node terakhir (prev dari head)
        Node tail = head.prev;
        
        // Insert node baru di akhir
        newNode.next = head;        // New node next -> head
        newNode.prev = tail;        // New node prev -> tail lama
        tail.next = newNode;        // Tail lama next -> new node
        head.prev = newNode;        // Head prev -> new node
        
        count++;
        System.out.println("✓ Berita berhasil ditambahkan!");
        System.out.println("  Total berita: " + count);
    }

    /**
     * Menghapus berita berdasarkan nomor urut (1-based index)
     * Time Complexity: O(n)
     * 
     * @param nomor Nomor urut berita yang akan dihapus (1 sampai count)
     */
    public void hapusBerita(int nomor) {
        if (head == null) {
            System.out.println("✗ Error: List kosong!");
            return;
        }
        
        // Validasi nomor
        if (nomor < 1 || nomor > count) {
            System.out.println("✗ Error: Nomor tidak valid!");
            System.out.println("  Nomor harus antara 1 sampai " + count);
            return;
        }
        
        // Jika hanya ada satu node
        if (count == 1) {
            String deletedBerita = head.berita;
            head = null;
            count--;
            System.out.println("✓ Berita berhasil dihapus!");
            System.out.println("  Berita yang dihapus: " + deletedBerita);
            System.out.println("  Total berita: " + count);
            return;
        }
        
        // Traverse ke node yang akan dihapus
        Node current = head;
        for (int i = 1; i < nomor; i++) {
            current = current.next;
        }
        
        String deletedBerita = current.berita;
        
        // Update pointer
        current.prev.next = current.next;
        current.next.prev = current.prev;
        
        // Jika yang dihapus adalah head, update head
        if (current == head) {
            head = current.next;
        }
        
        count--;
        System.out.println("✓ Berita berhasil dihapus!");
        System.out.println("  Berita yang dihapus: " + deletedBerita);
        System.out.println("  Total berita: " + count);
    }

    /**
     * Menampilkan semua berita secara forward (depan ke belakang)
     * dengan animasi scrolling horizontal seperti teks berjalan di TV
     * Time Complexity: O(n)
     */
    public void tampilkanForward() {
        if (head == null) {
            System.out.println("✗ List kosong! Tidak ada berita untuk ditampilkan.");
            return;
        }
        
        // Clear terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        // Kumpulkan semua berita ke dalam list
        java.util.ArrayList<String> newsList = new java.util.ArrayList<>();
        Node current = head;
        do {
            newsList.add("     >>>  " + current.berita + "  <<<     ");
            current = current.next;
        } while (current != head);
        
        int screenWidth = 80; // Lebar layar
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     TEKS BERJALAN BERITA - MODE FORWARD                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("  Total berita: " + count + " | Tekan ENTER untuk berhenti");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println(); // Baris kosong
        
        // Flag untuk menghentikan scrolling
        final boolean[] stopScrolling = {false};
        
        // Thread untuk mendengarkan input keyboard
        Thread inputThread = new Thread(() -> {
            try {
                System.in.read();
                stopScrolling[0] = true;
            } catch (Exception e) {
                // Ignore
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();
        
        // Animasi scrolling dari kanan ke kiri (seperti TV) - LOOP TERUS MENERUS
        try {
            outerLoop:
            while (!stopScrolling[0]) { // Loop sampai user tekan Enter
                // Loop setiap berita
                for (String newsText : newsList) {
                    if (stopScrolling[0]) {
                        break outerLoop;
                    }
                    
                    int totalScrolls = newsText.length() + screenWidth;
                    
                    // Scroll berita dari kanan ke kiri
                    for (int i = 0; i < totalScrolls; i++) {
                        if (stopScrolling[0]) {
                            break outerLoop;
                        }
                        
                        // Buat string yang akan ditampilkan
                        StringBuilder displayLine = new StringBuilder();
                        
                        for (int j = 0; j < screenWidth; j++) {
                            int textIndex = i - screenWidth + j;
                            if (textIndex >= 0 && textIndex < newsText.length()) {
                                displayLine.append(newsText.charAt(textIndex));
                            } else {
                                displayLine.append(" ");
                            }
                        }
                        
                        // Clear line dan print, lalu kembali ke awal baris
                        // \033[2K = clear entire line, \r = carriage return to beginning
                        System.out.print("\033[2K\r" + displayLine.toString());
                        System.out.flush();
                        
                        Thread.sleep(80); // Delay untuk kecepatan scrolling
                    }
                    
                    // Jeda 3 detik setelah berita selesai di-scroll
                    if (!stopScrolling[0]) {
                        Thread.sleep(3000);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n\n✓ Semua berita telah ditampilkan (forward).\n");
    }

    /**
     * Menampilkan semua berita secara backward (belakang ke depan)
     * dengan animasi scrolling horizontal seperti teks berjalan di TV
     * Time Complexity: O(n)
     */
    public void tampilkanBackward() {
        if (head == null) {
            System.out.println("✗ List kosong! Tidak ada berita untuk ditampilkan.");
            return;
        }
        
        // Clear terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        // Kumpulkan semua berita ke dalam list (dari belakang ke depan)
        java.util.ArrayList<String> newsList = new java.util.ArrayList<>();
        Node current = head.prev; // Mulai dari tail
        for (int i = 0; i < count; i++) {
            newsList.add("     >>>  " + current.berita + "  <<<     ");
            current = current.prev;
        }
        
        int screenWidth = 80; // Lebar layar
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     TEKS BERJALAN BERITA - MODE BACKWARD                      ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("  Total berita: " + count + " | Tekan ENTER untuk berhenti");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println(); // Baris kosong
        
        // Flag untuk menghentikan scrolling
        final boolean[] stopScrolling = {false};
        
        // Thread untuk mendengarkan input keyboard
        Thread inputThread = new Thread(() -> {
            try {
                System.in.read();
                stopScrolling[0] = true;
            } catch (Exception e) {
                // Ignore
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();
        
        // Animasi scrolling dari kanan ke kiri (seperti TV) - LOOP TERUS MENERUS
        try {
            outerLoop:
            while (!stopScrolling[0]) { // Loop sampai user tekan Enter
                // Loop setiap berita
                for (String newsText : newsList) {
                    if (stopScrolling[0]) {
                        break outerLoop;
                    }
                    
                    int totalScrolls = newsText.length() + screenWidth;
                    
                    // Scroll berita dari kanan ke kiri
                    for (int i = 0; i < totalScrolls; i++) {
                        if (stopScrolling[0]) {
                            break outerLoop;
                        }
                        
                        // Buat string yang akan ditampilkan
                        StringBuilder displayLine = new StringBuilder();
                        
                        for (int j = 0; j < screenWidth; j++) {
                            int textIndex = i - screenWidth + j;
                            if (textIndex >= 0 && textIndex < newsText.length()) {
                                displayLine.append(newsText.charAt(textIndex));
                            } else {
                                displayLine.append(" ");
                            }
                        }
                        
                        // Clear line dan print, lalu kembali ke awal baris
                        // \033[2K = clear entire line, \r = carriage return to beginning
                        System.out.print("\033[2K\r" + displayLine.toString());
                        System.out.flush();
                        
                        Thread.sleep(80); // Delay untuk kecepatan scrolling
                    }
                    
                    // Jeda 3 detik setelah berita selesai di-scroll
                    if (!stopScrolling[0]) {
                        Thread.sleep(3000);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n\n✓ Semua berita telah ditampilkan (backward).\n");
    }

    /**
     * Menampilkan berita tertentu berdasarkan nomor urut
     * Time Complexity: O(n)
     * 
     * @param nomor Nomor urut berita yang akan ditampilkan (1 sampai count)
     */
    public void tampilkanBeritaTertentu(int nomor) {
        if (head == null) {
            System.out.println("✗ List kosong!");
            return;
        }
        
        // Validasi nomor
        if (nomor < 1 || nomor > count) {
            System.out.println("✗ Error: Nomor tidak valid!");
            System.out.println("  Nomor harus antara 1 sampai " + count);
            return;
        }
        
        // Traverse ke node yang diminta
        Node current = head;
        for (int i = 1; i < nomor; i++) {
            current = current.next;
        }
        
        // Clear terminal
        System.out.print("\033[H\033[2J");
        System.out.flush();
        
        // Gabungkan berita terpilih dengan separator untuk scrolling
        String newsText = "     >>>  " + current.berita + "  <<<     ";
        int screenWidth = 80; // Lebar layar
        
        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                     TEKS BERJALAN BERITA - BERITA #" + nomor + "                       ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════════════╣");
        System.out.println("  Berita nomor: " + nomor + " dari " + count + " | Tekan ENTER untuk berhenti");
        System.out.println("╚═══════════════════════════════════════════════════════════════════════════════╝");
        System.out.println(); // Baris kosong
        
        // Flag untuk menghentikan scrolling
        final boolean[] stopScrolling = {false};
        
        // Thread untuk mendengarkan input keyboard
        Thread inputThread = new Thread(() -> {
            try {
                System.in.read();
                stopScrolling[0] = true;
            } catch (Exception e) {
                // Ignore
            }
        });
        inputThread.setDaemon(true);
        inputThread.start();
        
        // Animasi scrolling dari kanan ke kiri (seperti TV) - LOOP TERUS MENERUS
        try {
            outerLoop:
            while (!stopScrolling[0]) { // Loop sampai user tekan Enter
                int totalScrolls = newsText.length() + screenWidth;
                
                // Scroll berita dari kanan ke kiri
                for (int i = 0; i < totalScrolls; i++) {
                    if (stopScrolling[0]) {
                        break outerLoop;
                    }
                    
                    // Buat string yang akan ditampilkan
                    StringBuilder displayLine = new StringBuilder();
                    
                    for (int j = 0; j < screenWidth; j++) {
                        int textIndex = i - screenWidth + j;
                        if (textIndex >= 0 && textIndex < newsText.length()) {
                            displayLine.append(newsText.charAt(textIndex));
                        } else {
                            displayLine.append(" ");
                        }
                    }
                    
                    // Clear line dan print, lalu kembali ke awal baris
                    // \033[2K = clear entire line, \r = carriage return to beginning
                    System.out.print("\033[2K\r" + displayLine.toString());
                    System.out.flush();
                    
                    Thread.sleep(80); // Delay untuk kecepatan scrolling
                }
                
                // Jeda 3 detik setelah berita selesai di-scroll, sebelum loop lagi
                if (!stopScrolling[0]) {
                    Thread.sleep(3000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\n\n✓ Berita telah ditampilkan.\n");
    }

    /**
     * Menampilkan daftar semua berita tanpa animasi
     * Time Complexity: O(n)
     */
    public void lihatDaftarBerita() {
        if (head == null) {
            System.out.println("✗ List kosong!");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  DAFTAR SEMUA BERITA                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("  Total berita: " + count);
        System.out.println("────────────────────────────────────────────────────────────");
        
        Node current = head;
        int index = 1;
        
        do {
            System.out.printf("  [%d] %s%n", index, current.berita);
            current = current.next;
            index++;
        } while (current != head);
        
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Menampilkan menu utama program
     */
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       Circular Doubly Linked List Implementation           ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  1. Insert berita baru                                     ║");
        System.out.println("║  2. Hapus berita                                           ║");
        System.out.println("║  3. Tampilkan berita (Forward - Depan ke Belakang)         ║");
        System.out.println("║  4. Tampilkan berita (Backward - Belakang ke Depan)        ║");
        System.out.println("║  5. Tampilkan berita tertentu                              ║");
        System.out.println("║  6. Lihat daftar semua berita                              ║");
        System.out.println("║  7. Exit                                                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.print("Pilih menu (1-7): ");
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
                    case 1: // Insert berita
                        System.out.print("\nMasukkan teks berita: ");
                        String berita = scanner.nextLine();
                        if (!berita.trim().isEmpty()) {
                            insertBerita(berita);
                        } else {
                            System.out.println("✗ Error: Berita tidak boleh kosong!");
                        }
                        break;
                        
                    case 2: // Hapus berita
                        if (count == 0) {
                            System.out.println("✗ List kosong! Tidak ada berita untuk dihapus.");
                        } else {
                            lihatDaftarBerita();
                            System.out.print("Masukkan nomor berita yang akan dihapus (1-" + count + "): ");
                            int nomor = scanner.nextInt();
                            scanner.nextLine();
                            hapusBerita(nomor);
                        }
                        break;
                        
                    case 3: // Tampilkan forward
                        tampilkanForward();
                        break;
                        
                    case 4: // Tampilkan backward
                        tampilkanBackward();
                        break;
                        
                    case 5: // Tampilkan berita tertentu
                        if (count == 0) {
                            System.out.println("✗ List kosong!");
                        } else {
                            System.out.print("Masukkan nomor berita (1-" + count + "): ");
                            int nomor = scanner.nextInt();
                            scanner.nextLine();
                            tampilkanBeritaTertentu(nomor);
                        }
                        break;
                        
                    case 6: // Lihat daftar berita
                        lihatDaftarBerita();
                        break;
                        
                    case 7: // Exit
                        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
                        System.out.println("║  Terima kasih telah menggunakan program ini!               ║");
                        System.out.println("║  Simulasi Teks Berjalan Berita - CDLL Edition              ║");
                        System.out.println("╚════════════════════════════════════════════════════════════╝");
                        break;
                        
                    default:
                        System.out.println("✗ Pilihan tidak valid! Silakan pilih 1-7.");
                }
            } catch (Exception e) {
                System.out.println("✗ Input tidak valid! Silakan masukkan angka.");
                scanner.nextLine(); // Clear buffer
                choice = 0; // Reset choice agar tidak exit
            }
            
        } while (choice != 7);
        
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
