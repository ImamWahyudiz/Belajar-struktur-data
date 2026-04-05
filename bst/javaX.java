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
class Barang {
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
}

/**
 * Class Node untuk merepresentasikan node dalam Binary Search Tree
 */
class Node {
    Barang data;  // Data barang
    Node left;    // Pointer ke node kiri
    Node right;   // Pointer ke node kanan

    /**
     * Constructor untuk membuat node baru
     * @param data Data barang yang disimpan di node
     */
    public Node(Barang data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }
}

/**
 * Class javaX - Sistem Manajemen Data Barang menggunakan Binary Search Tree
 * 
 * Implementasi struktur data Binary Search Tree (BST) yang mendukung berbagai operasi:
 * - Insert: menambahkan data barang
 * - Search: mencari data barang berdasarkan ID
 * - Delete: menghapus data barang berdasarkan ID
 * - Traversal: Inorder, Preorder, Postorder
 * 
 * Karakteristik BST:
 * - Setiap node memiliki paling banyak 2 children (left dan right)
 * - Nilai di left subtree < nilai node
 * - Nilai di right subtree > nilai node
 */
public class javaX {
    private Node root;  // Root node dari BST
    private int count;  // Jumlah node dalam tree

    /**
     * Constructor - Inisialisasi BST kosong
     */
    public javaX() {
        root = null;
        count = 0;
    }

    /**
     * Insert data barang ke dalam BST
     * Time Complexity: O(log n) - average case, O(n) - worst case (skewed tree)
     * 
     * @param id ID barang
     * @param nama Nama barang
     */
    public void insert(int id, String nama) {
        if (searchNode(root, id) != null) {
            System.out.println("Error: ID sudah ada dalam tree!");
            return;
        }
        root = insertRecursive(root, new Barang(id, nama));
        count++;
        System.out.println("Data berhasil ditambahkan!");
    }

    /**
     * Helper method untuk insert secara recursive
     * 
     * @param node Node saat ini dalam proses traversal
     * @param data Data barang yang akan diinsert
     * @return Node yang sudah diupdate
     */
    private Node insertRecursive(Node node, Barang data) {
        // Base case: jika node kosong, buat node baru
        if (node == null) {
            return new Node(data);
        }

        // Jika ID lebih kecil, insert ke left subtree
        if (data.id < node.data.id) {
            node.left = insertRecursive(node.left, data);
        }
        // Jika ID lebih besar, insert ke right subtree
        else if (data.id > node.data.id) {
            node.right = insertRecursive(node.right, data);
        }

        return node;
    }

    /**
     * Search/Cari data barang berdasarkan ID
     * Time Complexity: O(log n) - average case, O(n) - worst case
     * 
     * @param id ID barang yang dicari
     * @return Objek Barang jika ditemukan, null jika tidak ada
     */
    public boolean search(int id) {
        Node result = searchNode(root, id);
        if (result != null) {
            System.out.println("Data ditemukan: " + result.data);
            return true;
        } else {
            System.out.println("Data dengan ID " + id + " tidak ditemukan!");
            return false;
        }
    }

    /**
     * Helper method untuk search secara recursive
     * 
     * @param node Node saat ini
     * @param id ID yang dicari
     * @return Node jika ditemukan, null jika tidak
     */
    private Node searchNode(Node node, int id) {
        // Base case: node kosong atau ID ditemukan
        if (node == null) {
            return null;
        }

        if (id == node.data.id) {
            return node;
        }
        // Jika ID lebih kecil, cari di left subtree
        else if (id < node.data.id) {
            return searchNode(node.left, id);
        }
        // Jika ID lebih besar, cari di right subtree
        else {
            return searchNode(node.right, id);
        }
    }

    /**
     * Delete data barang berdasarkan ID
     * Time Complexity: O(log n) - average case, O(n) - worst case
     * Menangani 3 kasus: leaf node, node dengan 1 child, node dengan 2 children
     * 
     * @param id ID barang yang akan dihapus
     */
    public void delete(int id) {
        if (searchNode(root, id) == null) {
            System.out.println("Data dengan ID " + id + " tidak ditemukan!");
            return;
        }
        root = deleteRecursive(root, id);
        count--;
        System.out.println("Data berhasil dihapus!");
    }

    /**
     * Helper method untuk delete secara recursive
     * 
     * @param node Node saat ini
     * @param id ID yang akan dihapus
     * @return Node yang sudah diupdate
     */
    private Node deleteRecursive(Node node, int id) {
        if (node == null) {
            return null;
        }

        if (id < node.data.id) {
            node.left = deleteRecursive(node.left, id);
        } else if (id > node.data.id) {
            node.right = deleteRecursive(node.right, id);
        } else {
            // Node ditemukan, sekarang delete-nya

            // Case 1: Node adalah leaf node (tidak punya child)
            if (node.left == null && node.right == null) {
                return null;
            }

            // Case 2: Node punya hanya right child
            if (node.left == null) {
                return node.right;
            }

            // Case 2: Node punya hanya left child
            if (node.right == null) {
                return node.left;
            }

            // Case 3: Node punya 2 children
            // Cari successor (node terkecil di right subtree)
            Node successor = findMin(node.right);
            node.data = successor.data;
            node.right = deleteRecursive(node.right, successor.data.id);
        }

        return node;
    }

    /**
     * Helper method untuk mencari node dengan nilai minimum (paling kiri)
     * 
     * @param node Node awal pencarian
     * @return Node dengan nilai minimum
     */
    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Traversal Inorder (Left-Root-Right)
     * Menghasilkan output dalam urutan ascending/sorted
     * Time Complexity: O(n)
     */
    public void inorderTraversal() {
        if (root == null) {
            System.out.println("\nTree kosong!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("INORDER TRAVERSAL (Left-Root-Right)");
        System.out.println("=".repeat(60));
        System.out.println("\nPenjelasan:");
        System.out.println("  • Inorder menghasilkan data dalam urutan ASCENDING (terkecil ke terbesar)");
        System.out.println("  • Urutan: Kunjungi LEFT subtree → ROOT → RIGHT subtree");
        System.out.println("\nHasil Traversal:");
        List<String> result = new ArrayList<>();
        inorderRecursive(root, result);
        for (String item : result) {
            System.out.println("  " + item);
        }
        
        System.out.println("\nInfo Tree:");
        System.out.println("  • Total node: " + count);
        System.out.println("  • Tinggi tree: " + getHeight(root));
        System.out.println("  • Status: " + (isBalanced(root) ? "✓ BALANCED" : "✗ NOT BALANCED"));
        System.out.println("=".repeat(60) + "\n");
    }
    
    private void inorderRecursive(Node node, List<String> result) {
        if (node == null) {
            return;
        }
        inorderRecursive(node.left, result);
        result.add(node.data.toString());
        inorderRecursive(node.right, result);
    }

    /**
     * Traversal Preorder (Root-Left-Right)
     * Time Complexity: O(n)
     */
    public void preorderTraversal() {
        if (root == null) {
            System.out.println("\nTree kosong!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PREORDER TRAVERSAL (Root-Left-Right)");
        System.out.println("=".repeat(60));
        System.out.println("\nPenjelasan:");
        System.out.println("  • Preorder mengunjungi ROOT terlebih dahulu");
        System.out.println("  • Sering digunakan untuk membuat COPY tree");
        System.out.println("  • Urutan: ROOT → Kunjungi LEFT subtree → Kunjungi RIGHT subtree");
        System.out.println("\nHasil Traversal:");
        List<String> result = new ArrayList<>();
        preorderRecursive(root, result);
        for (String item : result) {
            System.out.println("  " + item);
        }
        
        System.out.println("\nInfo Tree:");
        System.out.println("  • Total node: " + count);
        System.out.println("  • Tinggi tree: " + getHeight(root));
        System.out.println("  • Status: " + (isBalanced(root) ? "✓ BALANCED" : "✗ NOT BALANCED"));
        System.out.println("=".repeat(60) + "\n");
    }
    
    private void preorderRecursive(Node node, List<String> result) {
        if (node == null) {
            return;
        }
        result.add(node.data.toString());
        preorderRecursive(node.left, result);
        preorderRecursive(node.right, result);
    }

    /**
     * Traversal Postorder (Left-Right-Root)
     * Time Complexity: O(n)
     */
    public void postorderTraversal() {
        if (root == null) {
            System.out.println("\nTree kosong!");
            return;
        }
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("POSTORDER TRAVERSAL (Left-Right-Root)");
        System.out.println("=".repeat(60));
        System.out.println("\nPenjelasan:");
        System.out.println("  • Postorder mengunjungi ROOT terakhir");
        System.out.println("  • Sering digunakan untuk MENGHAPUS tree (leaf dulu)");
        System.out.println("  • Urutan: Kunjungi LEFT subtree → Kunjungi RIGHT subtree → ROOT");
        System.out.println("\nHasil Traversal:");
        List<String> result = new ArrayList<>();
        postorderRecursive(root, result);
        
        for (String item : result) {
            System.out.println("  " + item);
        }
        
        System.out.println("\nInfo Tree:");
        System.out.println("  • Total node: " + count);
        System.out.println("  • Tinggi tree: " + getHeight(root));
        System.out.println("  • Status: " + (isBalanced(root) ? "✓ BALANCED" : "✗ NOT BALANCED"));
        System.out.println("=".repeat(60) + "\n");
    }
    
    private void postorderRecursive(Node node, List<String> result) {
        if (node == null) {
            return;
        }
        postorderRecursive(node.left, result);
        postorderRecursive(node.right, result);
        result.add(node.data.toString());
    }

    /**
     * Tampilkan semua data dalam tree (menggunakan inorder traversal)
     */
    public void displayAll() {
        System.out.println("\n========== SEMUA DATA BARANG ==========");
        if (root == null) {
            System.out.println("Tree kosong!");
        } else {
            List<String> result = new ArrayList<>();
            inorderRecursive(root, result);
            for (String item : result) {
                System.out.println(item);
            }
        }
        System.out.println("======================================\n");
    }

    /**
     * Tampilkan informasi statistik tree
     */
    public void showStatistics() {
        System.out.println("\n========== STATISTIK TREE ==========");
        System.out.println("Jumlah node: " + count);
        System.out.println("Tinggi tree: " + getHeight(root));
        System.out.println("Status balance: " + (isBalanced(root) ? "✓ BALANCED" : "✗ NOT BALANCED"));
        System.out.println("====================================\n");
    }
    
    /**
     * Balance tree menggunakan in-order traversal dan rebuild
     * Menggunakan divide & conquer pada sorted data untuk membuat balanced tree
     */
    public void balanceTree() {
        if (count == 0) {
            System.out.println("Tree kosong, tidak ada yang di-balance!");
            return;
        }
        
        // Dapatkan semua data dalam urutan sorted (inorder)
        ArrayList<Barang> sortedData = new ArrayList<>();
        inorderCollect(root, sortedData);
        
        // Clear tree
        root = null;
        
        // Rebuild tree dari data yang sudah sorted (membuat balanced tree)
        buildBalancedTree(sortedData);
        System.out.println("\n✓ Tree berhasil di-balance! (Sebelumnya: NOT BALANCED, Sekarang: BALANCED)");
    }
    
    /**
     * Build balanced tree dari sorted data menggunakan divide & conquer
     */
    private void buildBalancedTree(ArrayList<Barang> sortedData) {
        if (sortedData.isEmpty()) return;
        root = buildBalancedRecursive(sortedData, 0, sortedData.size() - 1);
    }
    
    /**
     * Recursive helper untuk build balanced tree
     * Mengambil middle element sebagai root
     */
    private Node buildBalancedRecursive(ArrayList<Barang> data, int start, int end) {
        if (start > end) return null;
        
        int mid = (start + end) / 2;
        Node node = new Node(data.get(mid));
        
        node.left = buildBalancedRecursive(data, start, mid - 1);
        node.right = buildBalancedRecursive(data, mid + 1, end);
        
        return node;
    }
    
    /**
     * Collect semua data dari tree dalam urutan inorder ke dalam ArrayList
     */
    private void inorderCollect(Node node, ArrayList<Barang> result) {
        if (node == null) return;
        
        inorderCollect(node.left, result);
        result.add(node.data);
        inorderCollect(node.right, result);
    }
    

    /**
     * Check apakah tree balanced atau tidak
     * Tree balanced jika untuk setiap node, perbedaan tinggi left dan right subtree <= 1
     */
    private boolean isBalanced(Node node) {
        if (node == null) return true;
        
        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);
        
        if (Math.abs(leftHeight - rightHeight) > 1) {
            return false;
        }
        
        return isBalanced(node.left) && isBalanced(node.right);
    }

    /**
     * Load data barang dari file CSV
     * Format CSV: ID,Nama (dengan header)
     * 
     * @param filename Path ke file CSV
     * @return Jumlah data yang berhasil ditambahkan
     */
    public int loadFromCSV(String filename) {
        // Cek beberapa kemungkinan lokasi file
        File csvFile = null;
        String[] possiblePaths = {
            filename,
            "bst" + File.separator + filename,
            "." + File.separator + filename,
            "." + File.separator + "bst" + File.separator + filename
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
                
                try {
                    // Split dengan komma
                    String[] parts = line.split(",", 2);
                    
                    if (parts.length < 2) {
                        System.out.println("  Warning: Format baris tidak sesuai, dilewati: " + line);
                        errorCount++;
                        continue;
                    }
                    
                    int id = Integer.parseInt(parts[0].trim());
                    String nama = parts[1].trim();
                    
                    if (nama.isEmpty()) {
                        System.out.println("  Warning: Row dengan ID " + id + " memiliki nama kosong, dilewati.");
                        errorCount++;
                        continue;
                    }
                    
                    // Cek duplikasi ID
                    if (searchNode(root, id) != null) {
                        System.out.println("  Warning: ID " + id + " sudah ada, dilewati.");
                        errorCount++;
                        continue;
                    }
                    
                    root = insertRecursive(root, new Barang(id, nama));
                    count++;
                    successCount++;
                
                } catch (NumberFormatException e) {
                    System.out.println("  Warning: ID '" + line.split(",")[0] + "' bukan angka, dilewati.");
                    errorCount++;
                }
            }
            
            if (successCount > 0) {
                System.out.println("\nBerhasil menambahkan " + successCount + " data dari file CSV");
                if (errorCount > 0) {
                    System.out.println("(" + errorCount + " data gagal/dilewati)");
                }
            } else {
                System.out.println("\nGagal menambahkan data dari CSV");
            }
            
            return successCount;
        
        } catch (IOException e) {
            System.out.println("Error membaca file CSV: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Helper method untuk mendapatkan tinggi tree
     * 
     * @param node Node awal
     * @return Tinggi tree
     */
    private int getHeight(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    /**
     * Main method - Program utama dengan menu interaktif
     */
    public static void main(String[] args) {
        javaX bst = new javaX();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            System.out.println("========== PROGRAM BINARY SEARCH TREE ==========");
            System.out.println("Program Manajemen Data Barang");
            System.out.println("================================================\n");

            while (running) {
                System.out.println("========== MENU BINARY SEARCH TREE ==========");
                System.out.println("1. Tambah Data");
                System.out.println("2. Cari Data");
                System.out.println("3. Hapus Data");
                System.out.println("4. Traversal (Inorder)");
                System.out.println("5. Traversal (Preorder)");
                System.out.println("6. Traversal (Postorder)");
                System.out.println("7. Tampilkan Semua Data");
                System.out.println("8. Informasi Tree");
                System.out.println("9. Tambah Data dari CSV");
                System.out.println("10. Balance/Rebalance Tree");
                System.out.println("0. Keluar");
                System.out.println("=============================================");
                System.out.print("Pilih menu (0-10): ");

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
                            bst.insert(id, nama);
                            break;

                        case 2:
                            System.out.print("Masukkan ID Barang yang dicari: ");
                            int searchId = scanner.nextInt();
                            scanner.nextLine();
                            bst.search(searchId);
                            break;

                        case 3:
                            System.out.print("Masukkan ID Barang yang akan dihapus: ");
                            int deleteId = scanner.nextInt();
                            scanner.nextLine();
                            bst.delete(deleteId);
                            break;

                        case 4:
                            bst.inorderTraversal();
                            break;

                        case 5:
                            bst.preorderTraversal();
                            break;

                        case 6:
                            bst.postorderTraversal();
                            break;

                        case 7:
                            bst.displayAll();
                            break;

                        case 8:
                            bst.showStatistics();
                            break;

                        case 9:
                            System.out.print("Masukkan nama file CSV (default: data.csv): ");
                            String filename = scanner.nextLine().trim();
                            if (filename.isEmpty()) {
                                filename = "data.csv";
                            }
                            bst.loadFromCSV(filename);
                            break;

                        case 10:
                            System.out.println("\nMengubah tree menjadi balanced...");
                            bst.balanceTree();
                            bst.showStatistics();
                            break;

                        case 0:
                            System.out.println("Terima kasih! Program selesai.");
                            running = false;
                            break;

                        default:
                            System.out.println("Pilihan tidak valid! Silakan coba lagi.");
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
