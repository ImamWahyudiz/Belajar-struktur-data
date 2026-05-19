package graph;

import java.util.*;

/**
 * Program Manajemen Graf (Graph)
 * Implementasi Graf dengan Adjacency List
 *
 * Fitur:
 *   1. Tambah Vertex
 *   2. Hapus Vertex
 *   3. Tambah Edge
 *   4. Hapus Edge
 *   5. Tampilkan Graph (Adjacency List / Matrix / ASCII)
 *   6. Traversal DFS
 *   7. Traversal BFS
 *   8. Toggle Directed / Undirected
 *   0. Quit
 *
 * Representasi : Adjacency List (TreeMap of LinkedList)
 * Jenis Graf   : Undirected / Directed (dapat di-toggle)
 */

// ─────────────────────────────────────────────────────────────
//  Class Graph
// ─────────────────────────────────────────────────────────────

class Graph {

    /**
     * Adjacency list: key = nama vertex, value = daftar tetangga (sorted)
     * TreeMap digunakan agar vertex selalu terurut secara alfabet.
     */
    private final TreeMap<String, List<String>> adj;

    /** true = directed graph, false = undirected graph */
    private boolean directed;

    /** Konstruktor: buat graf kosong */
    public Graph(boolean directed) {
        this.adj = new TreeMap<>();
        this.directed = directed;
    }

    // ── Getters ───────────────────────────────────────────────

    public boolean isDirected()        { return directed; }
    public void    toggleDirected()    { directed = !directed; }
    public Set<String> getVertices()   { return adj.keySet(); }
    public int vertexCount()           { return adj.size(); }

    public int edgeCount() {
        int total = adj.values().stream().mapToInt(List::size).sum();
        return directed ? total : total / 2;
    }

    public boolean hasVertex(String v) { return adj.containsKey(v.toUpperCase()); }

    public boolean hasEdge(String u, String v) {
        u = u.toUpperCase(); v = v.toUpperCase();
        return adj.containsKey(u) && adj.get(u).contains(v);
    }

    // ── Tambah / Hapus Vertex ─────────────────────────────────

    /**
     * Tambah vertex baru.
     * @return true jika berhasil, false jika sudah ada
     */
    public boolean addVertex(String v) {
        v = v.trim().toUpperCase();
        if (adj.containsKey(v)) return false;
        adj.put(v, new ArrayList<>());
        return true;
    }

    /**
     * Hapus vertex beserta semua edge yang terhubung.
     * @return true jika berhasil, false jika tidak ditemukan
     */
    public boolean removeVertex(String v) {
        v = v.trim().toUpperCase();
        if (!adj.containsKey(v)) return false;

        // Hapus semua referensi ke v dari vertex lain
        for (List<String> neighbors : adj.values()) {
            neighbors.remove(v);
        }
        adj.remove(v);
        return true;
    }

    // ── Tambah / Hapus Edge ───────────────────────────────────

    /**
     * Tambah edge antara u dan v.
     * @return 1=berhasil, 0=edge sudah ada, -1=vertex tidak ditemukan, -2=self-loop
     */
    public int addEdge(String u, String v) {
        u = u.trim().toUpperCase();
        v = v.trim().toUpperCase();

        if (!adj.containsKey(u) || !adj.containsKey(v)) return -1;
        if (u.equals(v)) return -2;
        if (adj.get(u).contains(v)) return 0;

        adj.get(u).add(v);
        Collections.sort(adj.get(u));
        if (!directed) {
            adj.get(v).add(u);
            Collections.sort(adj.get(v));
        }
        return 1;
    }

    /**
     * Hapus edge antara u dan v.
     * @return true jika berhasil, false jika edge tidak ditemukan
     */
    public boolean removeEdge(String u, String v) {
        u = u.trim().toUpperCase();
        v = v.trim().toUpperCase();

        if (!adj.containsKey(u) || !adj.containsKey(v)) return false;
        if (!adj.get(u).contains(v)) return false;

        adj.get(u).remove(v);
        if (!directed) adj.get(v).remove(u);
        return true;
    }

    // ── Traversal ─────────────────────────────────────────────

    /**
     * DFS iteratif menggunakan Stack.
     * @return Daftar vertex sesuai urutan kunjungan, null jika start tidak ada
     */
    public List<String> dfs(String start) {
        start = start.trim().toUpperCase();
        if (!adj.containsKey(start)) return null;

        List<String> visited = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        Set<String> seen = new HashSet<>();

        stack.push(start);
        while (!stack.isEmpty()) {
            String curr = stack.pop();
            if (!seen.contains(curr)) {
                seen.add(curr);
                visited.add(curr);
                List<String> neighbors = new ArrayList<>(adj.get(curr));
                Collections.sort(neighbors, Collections.reverseOrder());
                for (String nb : neighbors) {
                    if (!seen.contains(nb)) stack.push(nb);
                }
            }
        }
        return visited;
    }

    /**
     * BFS iteratif menggunakan Queue.
     * @return Daftar vertex sesuai urutan kunjungan, null jika start tidak ada
     */
    public List<String> bfs(String start) {
        start = start.trim().toUpperCase();
        if (!adj.containsKey(start)) return null;

        List<String> visited = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> seen = new HashSet<>();

        queue.offer(start);
        seen.add(start);

        while (!queue.isEmpty()) {
            String curr = queue.poll();
            visited.add(curr);
            List<String> neighbors = new ArrayList<>(adj.get(curr));
            Collections.sort(neighbors);
            for (String nb : neighbors) {
                if (!seen.contains(nb)) {
                    seen.add(nb);
                    queue.offer(nb);
                }
            }
        }
        return visited;
    }

    // ── Display ───────────────────────────────────────────────

    /** Tampilkan Adjacency List */
    public void displayAdjacencyList() {
        String kind = directed ? "DIRECTED" : "UNDIRECTED";
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.printf ("║        ADJACENCY LIST — %-37s║%n", kind);
        System.out.println("╠══════════════════════════════════════════════════════════════╣");

        if (adj.isEmpty()) {
            System.out.println("║  (graf kosong)                                               ║");
        } else {
            for (Map.Entry<String, List<String>> e : adj.entrySet()) {
                String v = e.getKey();
                List<String> nb = e.getValue();
                if (nb.isEmpty()) {
                    System.out.printf("  %4s  :  (tidak ada tetangga)%n", v);
                } else {
                    System.out.printf("  %4s  :  %s%n", v, String.join(" → ", nb));
                }
            }
        }

        System.out.println("╠══════════════════════════════════════════════════════════════╣");
        System.out.printf ("  Vertex: %-3d | Edge: %-3d | Jenis: %s%n",
                            vertexCount(), edgeCount(), kind);
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /** Tampilkan Adjacency Matrix */
    public void displayAdjacencyMatrix() {
        List<String> vertices = new ArrayList<>(adj.keySet());
        int n = vertices.size();

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                   ADJACENCY MATRIX                          ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");

        if (n == 0) {
            System.out.println("║  (graf kosong)                                               ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();
            return;
        }

        // Header
        System.out.print("       ");
        for (String v : vertices) System.out.printf("%4s", v);
        System.out.println();
        System.out.println("     " + "─".repeat(5 + 4 * n));

        for (String u : vertices) {
            System.out.printf("  %3s │", u);
            for (String v : vertices) {
                if (u.equals(v))       System.out.print("   -");
                else if (hasEdge(u,v)) System.out.print("   1");
                else                   System.out.print("   0");
            }
            System.out.println();
        }

        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /** Tampilkan visualisasi ASCII edge list */
    public void displayAsciiGraph() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              VISUALISASI GRAF (ASCII)                        ║");
        System.out.println("╠══════════════════════════════════════════════════════════════╣");

        if (adj.isEmpty()) {
            System.out.println("║  (graf kosong)                                               ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();
            return;
        }

        Set<String> printed = new HashSet<>();
        boolean anyEdge = false;
        for (Map.Entry<String, List<String>> e : adj.entrySet()) {
            String u = e.getKey();
            for (String v : e.getValue()) {
                String key = directed ? (u + ">" + v)
                        : (u.compareTo(v) < 0 ? u + "-" + v : v + "-" + u);
                if (!printed.contains(key)) {
                    printed.add(key);
                    String conn = directed ? "──►" : "───";
                    System.out.printf("  [%s] %s [%s]%n", u, conn, v);
                    anyEdge = true;
                }
            }
        }
        if (!anyEdge) System.out.println("  (tidak ada edge)");

        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }

    /** Tampilkan hasil traversal */
    public void displayTraversal(List<String> order, String algoName, String start) {
        System.out.println();
        System.out.printf("╔══════════════════════════════════════════════════════════════╗%n");
        System.out.printf("║          HASIL TRAVERSAL %-37s║%n", algoName);
        System.out.printf("╠══════════════════════════════════════════════════════════════╣%n");
        System.out.printf("  Start  : %s%n", start);
        System.out.print ("  Urutan : ");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < order.size(); i++) {
            sb.append("[").append(i + 1).append("]").append(order.get(i));
            if (i < order.size() - 1) sb.append(" → ");
        }
        System.out.println(sb);
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
}

// ─────────────────────────────────────────────────────────────
//  Class utama (Main)
// ─────────────────────────────────────────────────────────────

public class javaX {

    private static final Scanner sc = new Scanner(System.in);

    /** Tampilkan header program */
    private static void displayHeader() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║         PROGRAM MANAJEMEN GRAF — STRUKTUR DATA               ║");
        System.out.println("║       Representasi: Adjacency List  |  Java                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    /** Tampilkan menu utama */
    private static void displayMenu(Graph g) {
        String kind = g.isDirected() ? "DIRECTED" : "UNDIRECTED";
        System.out.printf("┌──────────────────────────────────────────────────────────────┐%n");
        System.out.printf("│  Graf: %-12s | Vertex: %-3d | Edge: %-3d%n",
                kind, g.vertexCount(), g.edgeCount());
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("│  MENU UTAMA                                                  │");
        System.out.println("├──────────────────────────────────────────────────────────────┤");
        System.out.println("  1.  Tambah Vertex");
        System.out.println("  2.  Hapus Vertex");
        System.out.println("  3.  Tambah Edge");
        System.out.println("  4.  Hapus Edge");
        System.out.println("  5.  Tampilkan Graph (List / Matrix / ASCII)");
        System.out.println("  6.  Traversal DFS");
        System.out.println("  7.  Traversal BFS");
        System.out.println("  8.  Toggle Directed / Undirected");
        System.out.println("  0.  Quit");
        System.out.println("└──────────────────────────────────────────────────────────────┘");
    }

    private static String prompt(String text) {
        System.out.print("  ► " + text + ": ");
        return sc.nextLine().trim();
    }

    private static void success(String msg) { System.out.println("  ✓ " + msg); }
    private static void error(String msg)   { System.out.println("  ✗ " + msg); }
    private static void info(String msg)    { System.out.println("  ℹ " + msg); }

    private static void waitEnter() {
        System.out.print("\n  [Tekan Enter untuk kembali ke menu...] ");
        sc.nextLine();
    }

    private static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                System.out.print("\033[H\033[2J");
        } catch (Exception ignored) {}
        System.out.flush();
    }

    public static void main(String[] args) {
        Graph graph = new Graph(false);

        // Pre-load contoh data
        for (String v : new String[]{"A","B","C","D","E"}) graph.addVertex(v);
        String[][] edges = {{"A","B"},{"A","C"},{"B","D"},{"C","D"},{"D","E"}};
        for (String[] e : edges) graph.addEdge(e[0], e[1]);

        while (true) {
            clearScreen();
            displayHeader();
            displayMenu(graph);

            String choice = prompt("Pilih menu (0–8)");

            switch (choice) {

                // ── 1. Tambah Vertex ──────────────────────────
                case "1": {
                    System.out.println();
                    String name = prompt("Nama vertex baru");
                    if (name.isEmpty()) {
                        error("Nama vertex tidak boleh kosong!");
                    } else if (graph.addVertex(name)) {
                        success("Vertex '" + name.toUpperCase() + "' berhasil ditambahkan!");
                        info("Total vertex sekarang: " + graph.vertexCount());
                    } else {
                        error("Vertex '" + name.toUpperCase() + "' sudah ada dalam graf!");
                    }
                    waitEnter();
                    break;
                }

                // ── 2. Hapus Vertex ───────────────────────────
                case "2": {
                    System.out.println();
                    if (graph.vertexCount() == 0) {
                        error("Graf kosong, tidak ada vertex yang bisa dihapus.");
                    } else {
                        info("Vertex yang ada: " + String.join(", ", graph.getVertices()));
                        String name = prompt("Nama vertex yang akan dihapus");
                        if (graph.removeVertex(name)) {
                            success("Vertex '" + name.toUpperCase() + "' berhasil dihapus!");
                            info("Total vertex sekarang: " + graph.vertexCount());
                        } else {
                            error("Vertex '" + name.toUpperCase() + "' tidak ditemukan!");
                        }
                    }
                    waitEnter();
                    break;
                }

                // ── 3. Tambah Edge ────────────────────────────
                case "3": {
                    System.out.println();
                    if (graph.vertexCount() < 2) {
                        error("Graf butuh minimal 2 vertex untuk membuat edge.");
                    } else {
                        info("Vertex yang ada: " + String.join(", ", graph.getVertices()));
                        String u = prompt("Vertex asal");
                        String v = prompt("Vertex tujuan");
                        int result = graph.addEdge(u, v);
                        switch (result) {
                            case  1: success("Edge '" + u.toUpperCase() + "' — '" + v.toUpperCase() + "' berhasil ditambahkan!");
                                     info("Total edge sekarang: " + graph.edgeCount()); break;
                            case  0: error("Edge '" + u.toUpperCase() + "' — '" + v.toUpperCase() + "' sudah ada!"); break;
                            case -1: error("Salah satu vertex tidak ditemukan!"); break;
                            case -2: error("Self-loop tidak diizinkan!"); break;
                        }
                    }
                    waitEnter();
                    break;
                }

                // ── 4. Hapus Edge ─────────────────────────────
                case "4": {
                    System.out.println();
                    if (graph.edgeCount() == 0) {
                        error("Tidak ada edge dalam graf.");
                    } else {
                        info("Vertex yang ada: " + String.join(", ", graph.getVertices()));
                        String u = prompt("Vertex asal edge");
                        String v = prompt("Vertex tujuan edge");
                        if (graph.removeEdge(u, v)) {
                            success("Edge '" + u.toUpperCase() + "' — '" + v.toUpperCase() + "' berhasil dihapus!");
                            info("Total edge sekarang: " + graph.edgeCount());
                        } else {
                            error("Edge '" + u.toUpperCase() + "' — '" + v.toUpperCase() + "' tidak ditemukan!");
                        }
                    }
                    waitEnter();
                    break;
                }

                // ── 5. Tampilkan Graph ────────────────────────
                case "5": {
                    System.out.println();
                    System.out.println("  Pilih tampilan:");
                    System.out.println("  a. Adjacency List");
                    System.out.println("  b. Adjacency Matrix");
                    System.out.println("  c. ASCII Visual");
                    System.out.println("  d. Semua Tampilan");
                    String sub = prompt("Pilih (a/b/c/d)");
                    switch (sub) {
                        case "a": graph.displayAdjacencyList();  break;
                        case "b": graph.displayAdjacencyMatrix(); break;
                        case "c": graph.displayAsciiGraph();      break;
                        case "d": graph.displayAdjacencyList();
                                  graph.displayAdjacencyMatrix();
                                  graph.displayAsciiGraph();       break;
                        default:  error("Pilihan tidak valid!");
                    }
                    waitEnter();
                    break;
                }

                // ── 6. DFS ────────────────────────────────────
                case "6": {
                    System.out.println();
                    if (graph.vertexCount() == 0) {
                        error("Graf kosong!");
                    } else {
                        info("Vertex yang ada: " + String.join(", ", graph.getVertices()));
                        String start = prompt("Vertex awal DFS");
                        List<String> result = graph.dfs(start);
                        if (result == null) {
                            error("Vertex '" + start.toUpperCase() + "' tidak ditemukan!");
                        } else {
                            graph.displayTraversal(result, "DFS (Depth-First Search)", start.toUpperCase());
                            info("DFS menggunakan STACK — sedalam mungkin sebelum backtrack.");
                        }
                    }
                    waitEnter();
                    break;
                }

                // ── 7. BFS ────────────────────────────────────
                case "7": {
                    System.out.println();
                    if (graph.vertexCount() == 0) {
                        error("Graf kosong!");
                    } else {
                        info("Vertex yang ada: " + String.join(", ", graph.getVertices()));
                        String start = prompt("Vertex awal BFS");
                        List<String> result = graph.bfs(start);
                        if (result == null) {
                            error("Vertex '" + start.toUpperCase() + "' tidak ditemukan!");
                        } else {
                            graph.displayTraversal(result, "BFS (Breadth-First Search)", start.toUpperCase());
                            info("BFS menggunakan QUEUE — level demi level.");
                        }
                    }
                    waitEnter();
                    break;
                }

                // ── 8. Toggle Directed ────────────────────────
                case "8": {
                    System.out.println();
                    graph.toggleDirected();
                    String mode = graph.isDirected() ? "DIRECTED (berarah)" : "UNDIRECTED (tidak berarah)";
                    success("Mode graf diganti ke: " + mode);
                    info("Edge yang sudah ada tidak diubah secara otomatis.");
                    waitEnter();
                    break;
                }

                // ── 0. Quit ───────────────────────────────────
                case "0": {
                    System.out.println();
                    System.out.println("╔══════════════════════════════════════════════════════════════╗");
                    System.out.println("║       Terima kasih telah menggunakan program ini!            ║");
                    System.out.println("║              PROGRAM MANAJEMEN GRAF — Java                   ║");
                    System.out.println("╚══════════════════════════════════════════════════════════════╝");
                    System.out.println();
                    sc.close();
                    return;
                }

                default:
                    error("Pilihan tidak valid! Masukkan angka 0–8.");
                    waitEnter();
            }
        }
    }
}
