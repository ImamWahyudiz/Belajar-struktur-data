package graph;

import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Point;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.*;
import java.awt.geom.*;


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
    
    public List<String> getNeighbors(String v) {
        v = v.toUpperCase();
        return adj.containsKey(v) ? adj.get(v) : new ArrayList<>();
    }

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
//  Class JavaGraphVisualizer (GUI Swing)
// ─────────────────────────────────────────────────────────────

class JavaGraphVisualizer extends JFrame {
    private final Graph graph;
    private final Map<String, Point2D> nodeCoords;
    private final double nodeRadius = 22;
    private String draggedNode = null;
    
    // Status Animasi
    private boolean animationRunning = false;
    private boolean animationPaused = false;
    private List<AnimationStep> animationSteps = new ArrayList<>();
    private int currentStepIdx = 0;
    private int animationDelay = 1000; // ms
    private String currentAlgo = "";
    
    // Timer
    private javax.swing.Timer stepTimer = null;
    
    // Status Animasi Partikel Aliran Edge
    private boolean particleAnimating = false;
    private Point2D.Double particlePos = null;
    private String particleStartNode = null;
    private String particleEndNode = null;
    private double particleProgress = 0.0;
    private javax.swing.Timer particleTimer = null;
    private Runnable particleCallback = null;
    
    // Komponen GUI Swing
    private final GraphPanel canvasPanel;
    private JComboBox<String> cbStartNode;
    private JButton btnDFS;
    private JButton btnBFS;
    private JButton btnPlay;
    private JButton btnStep;
    private JButton btnReset;
    private JSlider sliderSpeed;
    private JLabel lblStructTitle;
    private JLabel lblStructContent;
    private JTextArea txtLog;
    private JPanel sidebar;
    private JButton btnHamburger;
    private boolean sidebarVisible = true;
    
    // Tab 1 Kelola components
    private JButton btnMode;
    private JTextField tfAddVertex;
    private JButton btnAddVertex;
    private JComboBox<String> cbDelVertex;
    private JButton btnDelVertex;
    private JComboBox<String> cbEdgeSrc1;
    private JComboBox<String> cbEdgeDst1;
    private JButton btnAddEdge;
    private JComboBox<String> cbEdgeSrc2;
    private JComboBox<String> cbEdgeDst2;
    private JButton btnDelEdge;
    
    // Tab 3 View components
    private JTextArea txtAdjList;
    private JTextArea txtAdjMatrix;
    
    // Tema warna modern (Catppuccin Mocha Palette)
    private final Color bgColor = new Color(0x1E, 0x1E, 0x2E);
    private final Color panelBg = new Color(0x18, 0x18, 0x25);
    private final Color nodeDefault = new Color(0x31, 0x32, 0x44);
    private final Color nodeActive = new Color(0xF3, 0x8B, 0xA8);      // active (red/pink)
    private final Color nodeQueued = new Color(0xF9, 0xE2, 0xAF);      // queued (yellow)
    private final Color nodeVisited = new Color(0xA6, 0xE3, 0xA1);     // visited (green)
    private final Color edgeDefault = new Color(0x45, 0x47, 0x5A);
    private final Color edgeActive = new Color(0x89, 0xB4, 0xFA);      // path active (blue)
    private final Color textColor = new Color(0xCD, 0xD6, 0xF4);
    
    static class AnimationStep {
        String currentNode;
        Set<String> visited;
        Set<String> queued;
        Set<String> activeEdges; // format: "u->v"
        String logMessage;
        List<String> structureContents;
        
        AnimationStep(String currentNode, Set<String> visited, Set<String> queued, Set<String> activeEdges, String logMessage, List<String> structureContents) {
            this.currentNode = currentNode;
            this.visited = new HashSet<>(visited);
            this.queued = new HashSet<>(queued);
            this.activeEdges = new HashSet<>(activeEdges);
            this.logMessage = logMessage;
            this.structureContents = new ArrayList<>(structureContents);
        }
    }
    
    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void styleTextField(JTextField tf) {
        tf.setBackground(nodeDefault);
        tf.setForeground(textColor);
        tf.setCaretColor(textColor);
        tf.setFont(new Font("Arial", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0x45, 0x47, 0x5A)),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }
    
    private void styleComboBox(JComboBox<String> cb) {
        cb.setBackground(nodeDefault);
        cb.setForeground(textColor);
        cb.setFont(new Font("Arial", Font.PLAIN, 12));
        cb.setBorder(BorderFactory.createLineBorder(new Color(0x45, 0x47, 0x5A)));
    }
    
    public JavaGraphVisualizer(Graph graph) {
        this.graph = graph;
        this.nodeCoords = new HashMap<>();
        
        setTitle("Visualisasi Graf & Animasi Traversal (Java Swing)");
        setSize(1100, 700);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(bgColor);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                resetAnimation();
            }
        });
        
        setLayout(new BorderLayout());
        
        // 1. Panel Canvas
        canvasPanel = new GraphPanel();
        add(canvasPanel, BorderLayout.CENTER);
        
        // 2. Sidebar holding JTabbedPane
        sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(panelBg);
        sidebar.setPreferredSize(new Dimension(300, getHeight()));
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(panelBg);
        tabbedPane.setForeground(textColor);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 12));
        
        // ── Tab 1: Kelola ────────────────────────────────────
        JPanel tabManage = new JPanel();
        tabManage.setLayout(new BoxLayout(tabManage, BoxLayout.Y_AXIS));
        tabManage.setBackground(panelBg);
        tabManage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Mode Graf
        JLabel lblModeTitle = new JLabel("MODE GRAF");
        lblModeTitle.setForeground(new Color(0xF5, 0xC2, 0xE7));
        lblModeTitle.setFont(new Font("Arial", Font.BOLD, 10));
        lblModeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabManage.add(lblModeTitle);
        tabManage.add(Box.createRigidArea(new Dimension(0, 5)));
        
        btnMode = new JButton("Mode: UNDIRECTED");
        styleButton(btnMode, nodeDefault, textColor);
        btnMode.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnMode.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tabManage.add(btnMode);
        tabManage.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // KELOLA VERTEKS
        JLabel lblVTitle = new JLabel("KELOLA VERTEKS");
        lblVTitle.setForeground(new Color(0xF5, 0xC2, 0xE7));
        lblVTitle.setFont(new Font("Arial", Font.BOLD, 10));
        lblVTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabManage.add(lblVTitle);
        tabManage.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel lblAddV = new JLabel("Tambah Verteks baru:");
        lblAddV.setForeground(textColor);
        lblAddV.setFont(new Font("Arial", Font.PLAIN, 11));
        lblAddV.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabManage.add(lblAddV);
        tabManage.add(Box.createRigidArea(new Dimension(0, 2)));
        
        tfAddVertex = new JTextField();
        styleTextField(tfAddVertex);
        tfAddVertex.setAlignmentX(Component.LEFT_ALIGNMENT);
        tfAddVertex.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        tabManage.add(tfAddVertex);
        tabManage.add(Box.createRigidArea(new Dimension(0, 5)));
        
        btnAddVertex = new JButton("＋ Tambah Verteks");
        styleButton(btnAddVertex, new Color(0xA6, 0xE3, 0xA1), new Color(0x11, 0x11, 0x1B));
        btnAddVertex.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAddVertex.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        tabManage.add(btnAddVertex);
        tabManage.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblDelV = new JLabel("Hapus Verteks:");
        lblDelV.setForeground(textColor);
        lblDelV.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDelV.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabManage.add(lblDelV);
        tabManage.add(Box.createRigidArea(new Dimension(0, 2)));
        
        cbDelVertex = new JComboBox<>();
        styleComboBox(cbDelVertex);
        cbDelVertex.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbDelVertex.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        tabManage.add(cbDelVertex);
        tabManage.add(Box.createRigidArea(new Dimension(0, 5)));
        
        btnDelVertex = new JButton("－ Hapus Verteks");
        styleButton(btnDelVertex, new Color(0xF3, 0x8B, 0xA8), new Color(0x11, 0x11, 0x1B));
        btnDelVertex.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDelVertex.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        tabManage.add(btnDelVertex);
        tabManage.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // KELOLA EDGE
        JLabel lblETitle = new JLabel("KELOLA EDGE (SISI)");
        lblETitle.setForeground(new Color(0xF5, 0xC2, 0xE7));
        lblETitle.setFont(new Font("Arial", Font.BOLD, 10));
        lblETitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabManage.add(lblETitle);
        tabManage.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JLabel lblAddE = new JLabel("Tambah Edge (Asal → Tujuan):");
        lblAddE.setForeground(textColor);
        lblAddE.setFont(new Font("Arial", Font.PLAIN, 11));
        lblAddE.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabManage.add(lblAddE);
        tabManage.add(Box.createRigidArea(new Dimension(0, 2)));
        
        JPanel edgeFrame1 = new JPanel(new GridLayout(1, 2, 5, 0));
        edgeFrame1.setBackground(panelBg);
        edgeFrame1.setAlignmentX(Component.LEFT_ALIGNMENT);
        edgeFrame1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        cbEdgeSrc1 = new JComboBox<>();
        cbEdgeDst1 = new JComboBox<>();
        styleComboBox(cbEdgeSrc1);
        styleComboBox(cbEdgeDst1);
        edgeFrame1.add(cbEdgeSrc1);
        edgeFrame1.add(cbEdgeDst1);
        tabManage.add(edgeFrame1);
        tabManage.add(Box.createRigidArea(new Dimension(0, 5)));
        
        btnAddEdge = new JButton("＋ Hubungkan Edge");
        styleButton(btnAddEdge, new Color(0x89, 0xB4, 0xFA), new Color(0x11, 0x11, 0x1B));
        btnAddEdge.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAddEdge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        tabManage.add(btnAddEdge);
        tabManage.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel lblDelE = new JLabel("Hapus Edge (Asal → Tujuan):");
        lblDelE.setForeground(textColor);
        lblDelE.setFont(new Font("Arial", Font.PLAIN, 11));
        lblDelE.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabManage.add(lblDelE);
        tabManage.add(Box.createRigidArea(new Dimension(0, 2)));
        
        JPanel edgeFrame2 = new JPanel(new GridLayout(1, 2, 5, 0));
        edgeFrame2.setBackground(panelBg);
        edgeFrame2.setAlignmentX(Component.LEFT_ALIGNMENT);
        edgeFrame2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        cbEdgeSrc2 = new JComboBox<>();
        cbEdgeDst2 = new JComboBox<>();
        styleComboBox(cbEdgeSrc2);
        styleComboBox(cbEdgeDst2);
        edgeFrame2.add(cbEdgeSrc2);
        edgeFrame2.add(cbEdgeDst2);
        tabManage.add(edgeFrame2);
        tabManage.add(Box.createRigidArea(new Dimension(0, 5)));
        
        btnDelEdge = new JButton("－ Putuskan Edge");
        styleButton(btnDelEdge, new Color(0xF9, 0xE2, 0xAF), new Color(0x11, 0x11, 0x1B));
        btnDelEdge.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDelEdge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        tabManage.add(btnDelEdge);
        
        tabbedPane.addTab("Kelola", tabManage);
        
        // ── Tab 2: Traversal ─────────────────────────────────
        JPanel tabTraversal = new JPanel();
        tabTraversal.setLayout(new BoxLayout(tabTraversal, BoxLayout.Y_AXIS));
        tabTraversal.setBackground(panelBg);
        tabTraversal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblStartNodeTitle = new JLabel("Pilih Node Awal:");
        lblStartNodeTitle.setForeground(textColor);
        lblStartNodeTitle.setFont(new Font("Arial", Font.PLAIN, 11));
        lblStartNodeTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabTraversal.add(lblStartNodeTitle);
        tabTraversal.add(Box.createRigidArea(new Dimension(0, 5)));
        
        cbStartNode = new JComboBox<>();
        styleComboBox(cbStartNode);
        cbStartNode.setAlignmentX(Component.LEFT_ALIGNMENT);
        cbStartNode.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        tabTraversal.add(cbStartNode);
        tabTraversal.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel algBtns = new JPanel(new GridLayout(1, 2, 8, 0));
        algBtns.setBackground(panelBg);
        algBtns.setAlignmentX(Component.LEFT_ALIGNMENT);
        algBtns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnDFS = new JButton("Mulai DFS");
        btnBFS = new JButton("Mulai BFS");
        styleButton(btnDFS, new Color(0xA6, 0xE3, 0xA1), new Color(0x11, 0x11, 0x1B));
        styleButton(btnBFS, new Color(0x89, 0xB4, 0xFA), new Color(0x11, 0x11, 0x1B));
        algBtns.add(btnDFS);
        algBtns.add(btnBFS);
        tabTraversal.add(algBtns);
        tabTraversal.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JPanel mediaBtns = new JPanel(new GridLayout(1, 3, 5, 0));
        mediaBtns.setBackground(panelBg);
        mediaBtns.setAlignmentX(Component.LEFT_ALIGNMENT);
        mediaBtns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        btnPlay = new JButton("Pause");
        btnStep = new JButton("Step >");
        btnReset = new JButton("Reset");
        styleButton(btnPlay, new Color(0xCB, 0xA6, 0xF7), new Color(0x11, 0x11, 0x1B));
        styleButton(btnStep, new Color(0xF9, 0xE2, 0xAF), new Color(0x11, 0x11, 0x1B));
        styleButton(btnReset, new Color(0xEE, 0xBA, 0x90), new Color(0x11, 0x11, 0x1B));
        btnPlay.setEnabled(false);
        btnStep.setEnabled(false);
        mediaBtns.add(btnPlay);
        mediaBtns.add(btnStep);
        mediaBtns.add(btnReset);
        tabTraversal.add(mediaBtns);
        tabTraversal.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel lblDelaySpeed = new JLabel("Kecepatan Delay (ms):");
        lblDelaySpeed.setForeground(textColor);
        lblDelaySpeed.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDelaySpeed.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabTraversal.add(lblDelaySpeed);
        tabTraversal.add(Box.createRigidArea(new Dimension(0, 5)));
        
        sliderSpeed = new JSlider(200, 3000, animationDelay);
        sliderSpeed.setBackground(panelBg);
        sliderSpeed.setForeground(textColor);
        sliderSpeed.setAlignmentX(Component.LEFT_ALIGNMENT);
        sliderSpeed.addChangeListener(e -> {
            animationDelay = sliderSpeed.getValue();
            if (stepTimer != null && stepTimer.isRunning()) {
                stepTimer.setDelay(animationDelay);
            }
        });
        tabTraversal.add(sliderSpeed);
        tabTraversal.add(Box.createRigidArea(new Dimension(0, 20)));
        
        lblStructTitle = new JLabel("Isi Struktur Data:");
        lblStructTitle.setForeground(textColor);
        lblStructTitle.setFont(new Font("Arial", Font.BOLD, 12));
        lblStructTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabTraversal.add(lblStructTitle);
        tabTraversal.add(Box.createRigidArea(new Dimension(0, 5)));
        
        lblStructContent = new JLabel("(kosong)");
        lblStructContent.setBackground(nodeDefault);
        lblStructContent.setForeground(new Color(0xF9, 0xE2, 0xAF));
        lblStructContent.setFont(new Font("Courier New", Font.BOLD, 12));
        lblStructContent.setOpaque(true);
        lblStructContent.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        lblStructContent.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        lblStructContent.setAlignmentX(Component.LEFT_ALIGNMENT);
        tabTraversal.add(lblStructContent);
        
        tabbedPane.addTab("Traversal", tabTraversal);
        
        // ── Tab 3: View ──────────────────────────────────────
        JPanel tabView = new JPanel(new GridLayout(2, 1, 0, 10));
        tabView.setBackground(panelBg);
        tabView.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel pnlList = new JPanel(new BorderLayout());
        pnlList.setBackground(panelBg);
        JLabel lblList = new JLabel("Adjacency List:");
        lblList.setForeground(new Color(0xF5, 0xC2, 0xE7));
        lblList.setFont(new Font("Arial", Font.BOLD, 11));
        pnlList.add(lblList, BorderLayout.NORTH);
        
        txtAdjList = new JTextArea();
        txtAdjList.setBackground(bgColor);
        txtAdjList.setForeground(textColor);
        txtAdjList.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtAdjList.setEditable(false);
        JScrollPane scrollList = new JScrollPane(txtAdjList);
        scrollList.setBorder(BorderFactory.createLineBorder(new Color(0x45, 0x47, 0x5A)));
        pnlList.add(scrollList, BorderLayout.CENTER);
        
        JPanel pnlMatrix = new JPanel(new BorderLayout());
        pnlMatrix.setBackground(panelBg);
        JLabel lblMatrix = new JLabel("Adjacency Matrix:");
        lblMatrix.setForeground(new Color(0xF5, 0xC2, 0xE7));
        lblMatrix.setFont(new Font("Arial", Font.BOLD, 11));
        pnlMatrix.add(lblMatrix, BorderLayout.NORTH);
        
        txtAdjMatrix = new JTextArea();
        txtAdjMatrix.setBackground(bgColor);
        txtAdjMatrix.setForeground(textColor);
        txtAdjMatrix.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtAdjMatrix.setEditable(false);
        JScrollPane scrollMatrix = new JScrollPane(txtAdjMatrix);
        scrollMatrix.setBorder(BorderFactory.createLineBorder(new Color(0x45, 0x47, 0x5A)));
        pnlMatrix.add(scrollMatrix, BorderLayout.CENTER);
        
        tabView.add(pnlList);
        tabView.add(pnlMatrix);
        
        tabbedPane.addTab("View", tabView);
        
        // ── Tab 4: Log ───────────────────────────────────────
        JPanel tabLog = new JPanel(new BorderLayout(0, 5));
        tabLog.setBackground(panelBg);
        tabLog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblLogTitle = new JLabel("Log Penjelasan Langkah:");
        lblLogTitle.setForeground(textColor);
        lblLogTitle.setFont(new Font("Arial", Font.BOLD, 12));
        tabLog.add(lblLogTitle, BorderLayout.NORTH);
        
        txtLog = new JTextArea();
        txtLog.setBackground(bgColor);
        txtLog.setForeground(textColor);
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 11));
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(new Color(0x45, 0x47, 0x5A)));
        tabLog.add(scrollLog, BorderLayout.CENTER);
        
        tabbedPane.addTab("Log", tabLog);
        
        sidebar.add(tabbedPane, BorderLayout.CENTER);
        add(sidebar, BorderLayout.EAST);
        
        // Register Action Listeners
        btnMode.addActionListener(e -> {
            graph.toggleDirected();
            String modeStr = graph.isDirected() ? "Mode: DIRECTED" : "Mode: UNDIRECTED";
            btnMode.setText(modeStr);
            btnMode.setBackground(graph.isDirected() ? new Color(0xF5, 0xC2, 0xE7) : nodeDefault);
            btnMode.setForeground(graph.isDirected() ? new Color(0x11, 0x11, 0x1B) : textColor);
            updateStats();
            updateViewTab();
            canvasPanel.repaint();
        });
        
        btnAddVertex.addActionListener(e -> {
            String v = tfAddVertex.getText().trim().toUpperCase();
            if (v.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama vertex tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (graph.hasVertex(v)) {
                JOptionPane.showMessageDialog(this, "Vertex '" + v + "' sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            graph.addVertex(v);
            Random rand = new Random();
            int rx = 150 + rand.nextInt(400);
            int ry = 150 + rand.nextInt(300);
            nodeCoords.put(v, new Point2D.Double(rx, ry));
            tfAddVertex.setText("");
            
            updateStats();
            refreshDropdowns();
            updateViewTab();
            canvasPanel.repaint();
        });
        
        btnDelVertex.addActionListener(e -> {
            String v = (String) cbDelVertex.getSelectedItem();
            if (v == null || v.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Silakan pilih vertex yang ingin dihapus!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (graph.removeVertex(v)) {
                nodeCoords.remove(v);
                updateStats();
                refreshDropdowns();
                updateViewTab();
                canvasPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus vertex '" + v + "'!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnAddEdge.addActionListener(e -> {
            String u = (String) cbEdgeSrc1.getSelectedItem();
            String v = (String) cbEdgeDst1.getSelectedItem();
            if (u == null || v == null) {
                JOptionPane.showMessageDialog(this, "Silakan pilih kedua vertex!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (u.equals(v)) {
                JOptionPane.showMessageDialog(this, "Tidak bisa membuat self-loop (edge ke diri sendiri)!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (graph.hasEdge(u, v)) {
                JOptionPane.showMessageDialog(this, "Edge '" + u + "' - '" + v + "' sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            graph.addEdge(u, v);
            
            updateStats();
            refreshDropdowns();
            updateViewTab();
            canvasPanel.repaint();
        });
        
        btnDelEdge.addActionListener(e -> {
            String u = (String) cbEdgeSrc2.getSelectedItem();
            String v = (String) cbEdgeDst2.getSelectedItem();
            if (u == null || v == null) {
                JOptionPane.showMessageDialog(this, "Silakan pilih kedua vertex!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (graph.removeEdge(u, v)) {
                updateStats();
                refreshDropdowns();
                updateViewTab();
                canvasPanel.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Edge '" + u + "' - '" + v + "' tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnDFS.addActionListener(e -> startDFS());
        btnBFS.addActionListener(e -> startBFS());
        btnPlay.addActionListener(e -> togglePause());
        btnStep.addActionListener(e -> stepForward());
        btnReset.addActionListener(e -> resetAnimation());
        
        initLayout();
        updateStats();
        refreshDropdowns();
        updateViewTab();
    }
    
    private void initLayout() {
        Set<String> vertices = graph.getVertices();
        if (vertices.isEmpty()) return;
        
        double cx = 380;
        double cy = 320;
        double r = 180;
        int n = vertices.size();
        int i = 0;
        for (String v : vertices) {
            double angle = 2 * Math.PI * i / n;
            double x = cx + r * Math.cos(angle);
            double y = cy + r * Math.sin(angle);
            nodeCoords.put(v, new Point2D.Double(x, y));
            i++;
        }
    }
    
    private void updateStats() {
        int vCount = graph.vertexCount();
        int eCount = graph.edgeCount();
        String modeStr = graph.isDirected() ? "DIRECTED" : "UNDIRECTED";
        if (canvasPanel != null && canvasPanel.lblStats != null) {
            canvasPanel.lblStats.setText("Mode: " + modeStr + "  |  Verteks: " + vCount + "  |  Edge: " + eCount);
        }
    }
    
    private void refreshDropdowns() {
        Set<String> verticesSet = graph.getVertices();
        List<String> verticesList = new ArrayList<>(verticesSet);
        Collections.sort(verticesList);
        
        // Simpan item yang dipilih sebelumnya
        Object prevDel = cbDelVertex.getSelectedItem();
        Object prevSrc1 = cbEdgeSrc1.getSelectedItem();
        Object prevDst1 = cbEdgeDst1.getSelectedItem();
        Object prevSrc2 = cbEdgeSrc2.getSelectedItem();
        Object prevDst2 = cbEdgeDst2.getSelectedItem();
        Object prevStart = cbStartNode.getSelectedItem();
        
        // Bersihkan combobox
        cbDelVertex.removeAllItems();
        cbEdgeSrc1.removeAllItems();
        cbEdgeDst1.removeAllItems();
        cbEdgeSrc2.removeAllItems();
        cbEdgeDst2.removeAllItems();
        cbStartNode.removeAllItems();
        
        for (String v : verticesList) {
            cbDelVertex.addItem(v);
            cbEdgeSrc1.addItem(v);
            cbEdgeDst1.addItem(v);
            cbEdgeSrc2.addItem(v);
            cbEdgeDst2.addItem(v);
            cbStartNode.addItem(v);
        }
        
        // Set kembali pilihan sebelumnya jika masih valid
        if (verticesSet.contains(prevDel)) cbDelVertex.setSelectedItem(prevDel);
        if (verticesSet.contains(prevSrc1)) cbEdgeSrc1.setSelectedItem(prevSrc1);
        if (verticesSet.contains(prevDst1)) cbEdgeDst1.setSelectedItem(prevDst1);
        if (verticesSet.contains(prevSrc2)) cbEdgeSrc2.setSelectedItem(prevSrc2);
        if (verticesSet.contains(prevDst2)) cbEdgeDst2.setSelectedItem(prevDst2);
        
        if (verticesSet.contains(prevStart)) {
            cbStartNode.setSelectedItem(prevStart);
        } else if (!verticesList.isEmpty()) {
            cbStartNode.setSelectedIndex(0);
        }
    }
    
    private void updateViewTab() {
        // 1. Adjacency List
        txtAdjList.setText("");
        Set<String> vertices = graph.getVertices();
        String kind = graph.isDirected() ? "DIRECTED" : "UNDIRECTED";
        if (vertices.isEmpty()) {
            txtAdjList.append("(graf kosong)");
        } else {
            txtAdjList.append("=== ADJACENCY LIST (" + kind + ") ===\n\n");
            for (String v : vertices) {
                List<String> neighbors = graph.getNeighbors(v);
                String arrow = graph.isDirected() ? "→" : "─";
                if (neighbors.isEmpty()) {
                    txtAdjList.append("  " + v + " : (tidak ada tetangga)\n");
                } else {
                    txtAdjList.append("  " + v + " : " + String.join(" " + arrow + " ", neighbors) + "\n");
                }
            }
        }
        txtAdjList.setCaretPosition(0);
        
        // 2. Adjacency Matrix
        txtAdjMatrix.setText("");
        if (vertices.isEmpty()) {
            txtAdjMatrix.append("(graf kosong)");
        } else {
            txtAdjMatrix.append("=== ADJACENCY MATRIX ===\n\n");
            List<String> sortedV = new ArrayList<>(vertices);
            Collections.sort(sortedV);
            int n = sortedV.size();
            
            // Header
            txtAdjMatrix.append("    ");
            for (String v : sortedV) txtAdjMatrix.append(String.format("%4s", v));
            txtAdjMatrix.append("\n");
            txtAdjMatrix.append("   " + "─".repeat(4 * n + 1) + "\n");
            
            for (String u : sortedV) {
                txtAdjMatrix.append("  " + u + " │");
                for (String v : sortedV) {
                    if (u.equals(v)) txtAdjMatrix.append("   -");
                    else if (graph.hasEdge(u, v)) txtAdjMatrix.append("   1");
                    else txtAdjMatrix.append("   0");
                }
                txtAdjMatrix.append("\n");
            }
        }
        txtAdjMatrix.setCaretPosition(0);
    }
    
    // ── Traversal Recorders ───────────────────────────────────

    private List<AnimationStep> recordDFSSteps(String start) {
        List<AnimationStep> steps = new ArrayList<>();
        Set<String> simSeen = new HashSet<>();
        Stack<String> simStack = new Stack<>();
        List<String> simVisited = new ArrayList<>();
        Set<String> simActiveEdges = new HashSet<>();
        Map<String, String> simParent = new HashMap<>();
        
        simStack.push(start);
        steps.add(new AnimationStep(
            null, 
            new HashSet<>(simVisited), 
            new HashSet<>(simStack), 
            simActiveEdges, 
            "Memulai DFS dari vertex '" + start + "'. Memasukkan '" + start + "' ke Stack.", 
            new ArrayList<>(simStack)
        ));
        
        while (!simStack.isEmpty()) {
            String curr = simStack.pop();
            
            if (!simSeen.contains(curr)) {
                simSeen.add(curr);
                simVisited.add(curr);
                
                if (simParent.containsKey(curr)) {
                    simActiveEdges.add(simParent.get(curr) + "->" + curr);
                }
                
                List<String> currentStackList = new ArrayList<>(simStack);
                currentStackList.add(curr);
                steps.add(new AnimationStep(
                    curr, 
                    new HashSet<>(simVisited), 
                    new HashSet<>(simStack), 
                    simActiveEdges, 
                    "Pop '" + curr + "' dari Stack. Kunjungi '" + curr + "' (node berubah hijau).", 
                    currentStackList
                ));
                
                List<String> neighbors = new ArrayList<>(graph.getNeighbors(curr));
                Collections.sort(neighbors, java.util.Collections.reverseOrder());
                List<String> added = new ArrayList<>();
                for (String nb : neighbors) {
                    if (!simSeen.contains(nb)) {
                        simStack.push(nb);
                        simParent.put(nb, curr);
                        added.add(nb);
                    }
                }
                
                if (!added.isEmpty()) {
                    Collections.reverse(added);
                    steps.add(new AnimationStep(
                        curr, 
                        new HashSet<>(simVisited), 
                        new HashSet<>(simStack), 
                        simActiveEdges, 
                        "Masukkan tetangga '" + String.join(", ", added) + "' ke Stack (node berubah kuning).", 
                        new ArrayList<>(simStack)
                    ));
                }
            }
        }
        
        steps.add(new AnimationStep(
            null, 
            new HashSet<>(simVisited), 
            new HashSet<>(), 
            simActiveEdges, 
            "DFS Selesai! Semua vertex yang terjangkau telah dikunjungi.", 
            new ArrayList<>()
        ));
        return steps;
    }

    private List<AnimationStep> recordBFSSteps(String start) {
        List<AnimationStep> steps = new ArrayList<>();
        Set<String> simSeen = new HashSet<>();
        Queue<String> simQueue = new LinkedList<>();
        List<String> simVisited = new ArrayList<>();
        Set<String> simActiveEdges = new HashSet<>();
        Map<String, String> simParent = new HashMap<>();
        
        simQueue.offer(start);
        simSeen.add(start);
        
        steps.add(new AnimationStep(
            null, 
            new HashSet<>(simVisited), 
            new HashSet<>(simQueue), 
            simActiveEdges, 
            "Memulai BFS dari vertex '" + start + "'. Memasukkan '" + start + "' ke Queue.", 
            new ArrayList<>(simQueue)
        ));
        
        while (!simQueue.isEmpty()) {
            String curr = simQueue.poll();
            simVisited.add(curr);
            
            if (simParent.containsKey(curr)) {
                simActiveEdges.add(simParent.get(curr) + "->" + curr);
            }
            
            List<String> queueDisplay = new ArrayList<>();
            queueDisplay.add(curr);
            queueDisplay.addAll(simQueue);
            
            steps.add(new AnimationStep(
                curr, 
                new HashSet<>(simVisited), 
                new HashSet<>(simQueue), 
                simActiveEdges, 
                "De-queue '" + curr + "' dari Queue. Kunjungi '" + curr + "' (node berubah hijau).", 
                queueDisplay
            ));
            
            List<String> neighbors = new ArrayList<>(graph.getNeighbors(curr));
            Collections.sort(neighbors);
            List<String> added = new ArrayList<>();
            for (String nb : neighbors) {
                if (!simSeen.contains(nb)) {
                    simSeen.add(nb);
                    simQueue.offer(nb);
                    simParent.put(nb, curr);
                    added.add(nb);
                }
            }
            
            if (!added.isEmpty()) {
                steps.add(new AnimationStep(
                    curr, 
                    new HashSet<>(simVisited), 
                    new HashSet<>(simQueue), 
                    simActiveEdges, 
                    "En-queue tetangga '" + String.join(", ", added) + "' ke Queue (node berubah kuning).", 
                    new ArrayList<>(simQueue)
                ));
            }
        }
        
        steps.add(new AnimationStep(
            null, 
            new HashSet<>(simVisited), 
            new HashSet<>(), 
            simActiveEdges, 
            "BFS Selesai! Semua vertex yang terjangkau telah dikunjungi.", 
            new ArrayList<>()
        ));
        return steps;
    }
    
    // ── Particle Tracer Animation ─────────────────────────────

    private void animateParticle(String start, String end, Runnable callback) {
        if (!nodeCoords.containsKey(start) || !nodeCoords.containsKey(end)) {
            callback.run();
            return;
        }
        
        particleStartNode = start;
        particleEndNode = end;
        particleProgress = 0.0;
        particleAnimating = true;
        particleCallback = callback;
        
        final Point2D p1 = nodeCoords.get(start);
        final Point2D p2 = nodeCoords.get(end);
        
        if (particleTimer != null && particleTimer.isRunning()) {
            particleTimer.stop();
        }
        
        particleTimer = new javax.swing.Timer(20, new ActionListener() {
            int ticks = 0;
            final int maxTicks = 10;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                ticks++;
                particleProgress = (double) ticks / maxTicks;
                double px = p1.getX() + particleProgress * (p2.getX() - p1.getX());
                double py = p1.getY() + particleProgress * (p2.getY() - p1.getY());
                particlePos = new Point2D.Double(px, py);
                canvasPanel.repaint();
                
                if (ticks >= maxTicks) {
                    particleTimer.stop();
                    particleAnimating = false;
                    particlePos = null;
                    if (particleCallback != null) {
                        particleCallback.run();
                    }
                }
            }
        });
        particleTimer.start();
    }

    // ── Animation Controllers ──────────────────────────────────

    private void playNextStep() {
        if (!animationRunning || animationPaused) return;
        
        if (currentStepIdx >= animationSteps.size()) {
            animationRunning = false;
            updateControlsState();
            return;
        }
        
        final AnimationStep step = animationSteps.get(currentStepIdx);
        String curr = step.currentNode;
        String parent = null;
        
        if (curr != null && currentStepIdx > 0) {
            for (String edge : step.activeEdges) {
                String[] parts = edge.split("->");
                if (parts.length == 2 && parts[1].equals(curr)) {
                    parent = parts[0];
                    break;
                }
            }
        }
        
        Runnable complete = () -> {
            canvasPanel.repaint();
            txtLog.append("\n• " + step.logMessage);
            txtLog.setCaretPosition(txtLog.getDocument().getLength());
            
            String structName = currentAlgo.equals("DFS") ? "Stack" : "Queue";
            lblStructTitle.setText("Isi " + structName + ":");
            String contentStr = String.join(" -> ", step.structureContents);
            lblStructContent.setText(contentStr.isEmpty() ? "(kosong)" : contentStr);
            
            currentStepIdx++;
            
            if (animationRunning && !animationPaused) {
                if (stepTimer != null && stepTimer.isRunning()) stepTimer.stop();
                stepTimer = new javax.swing.Timer(animationDelay, e -> {
                    stepTimer.stop();
                    playNextStep();
                });
                stepTimer.start();
            }
        };
        
        if (parent != null && curr != null) {
            animateParticle(parent, curr, complete);
        } else {
            complete.run();
        }
    }

    private void playSingleStep() {
        if (currentStepIdx >= animationSteps.size()) {
            animationRunning = false;
            updateControlsState();
            return;
        }
        
        final AnimationStep step = animationSteps.get(currentStepIdx);
        String curr = step.currentNode;
        String parent = null;
        
        if (curr != null && currentStepIdx > 0) {
            for (String edge : step.activeEdges) {
                String[] parts = edge.split("->");
                if (parts.length == 2 && parts[1].equals(curr)) {
                    parent = parts[0];
                    break;
                }
            }
        }
        
        Runnable complete = () -> {
            canvasPanel.repaint();
            txtLog.append("\n• " + step.logMessage);
            txtLog.setCaretPosition(txtLog.getDocument().getLength());
            
            String structName = currentAlgo.equals("DFS") ? "Stack" : "Queue";
            lblStructTitle.setText("Isi " + structName + ":");
            String contentStr = String.join(" -> ", step.structureContents);
            lblStructContent.setText(contentStr.isEmpty() ? "(kosong)" : contentStr);
            
            currentStepIdx++;
            updateControlsState();
        };
        
        if (parent != null && curr != null) {
            animateParticle(parent, curr, complete);
        } else {
            complete.run();
        }
    }

    private void startDFS() {
        String start = (String) cbStartNode.getSelectedItem();
        if (start == null || start.isEmpty()) return;
        
        resetAnimation();
        currentAlgo = "DFS";
        animationSteps = recordDFSSteps(start);
        animationRunning = true;
        animationPaused = false;
        
        txtLog.setText("=== MEMULAI TRAVERSAL DFS ===\n");
        updateControlsState();
        playNextStep();
    }

    private void startBFS() {
        String start = (String) cbStartNode.getSelectedItem();
        if (start == null || start.isEmpty()) return;
        
        resetAnimation();
        currentAlgo = "BFS";
        animationSteps = recordBFSSteps(start);
        animationRunning = true;
        animationPaused = false;
        
        txtLog.setText("=== MEMULAI TRAVERSAL BFS ===\n");
        updateControlsState();
        playNextStep();
    }

    private void togglePause() {
        if (!animationRunning) return;
        animationPaused = !animationPaused;
        if (animationPaused) {
            btnPlay.setText("Resume");
            btnPlay.setBackground(new Color(0xA6, 0xE3, 0xA1));
            if (stepTimer != null) stepTimer.stop();
        } else {
            btnPlay.setText("Pause");
            btnPlay.setBackground(new Color(0xCB, 0xA6, 0xF7));
            playNextStep();
        }
        updateControlsState();
    }
    
    private void stepForward() {
        if (animationRunning && animationPaused) {
            playSingleStep();
        }
    }
    
    private void resetAnimation() {
        animationRunning = false;
        animationPaused = false;
        if (stepTimer != null) stepTimer.stop();
        if (particleTimer != null) particleTimer.stop();
        particleAnimating = false;
        particlePos = null;
        
        animationSteps.clear();
        currentStepIdx = 0;
        currentAlgo = "";
        
        txtLog.setText("Animasi di-reset. Graf kembali ke keadaan semula.");
        lblStructContent.setText("(kosong)");
        lblStructTitle.setText("Isi Struktur Data:");
        
        updateControlsState();
        canvasPanel.repaint();
    }
    
    private void updateControlsState() {
        if (animationRunning) {
            btnDFS.setEnabled(false);
            btnBFS.setEnabled(false);
            cbStartNode.setEnabled(false);
            btnPlay.setEnabled(true);
            if (animationPaused) {
                btnStep.setEnabled(true);
                btnPlay.setText("Resume");
                btnPlay.setBackground(new Color(0xA6, 0xE3, 0xA1));
            } else {
                btnStep.setEnabled(false);
                btnPlay.setText("Pause");
                btnPlay.setBackground(new Color(0xCB, 0xA6, 0xF7));
            }
        } else {
            btnDFS.setEnabled(true);
            btnBFS.setEnabled(true);
            cbStartNode.setEnabled(true);
            btnPlay.setEnabled(false);
            btnPlay.setText("Pause");
            btnPlay.setBackground(new Color(0xCB, 0xA6, 0xF7));
            btnStep.setEnabled(false);
        }
    }

    // ── Helper States ─────────────────────────────────────────

    private Color getNodeColorAtStep(String v) {
        if (!animationRunning || animationSteps.isEmpty()) return nodeDefault;
        int idx = Math.min(currentStepIdx, animationSteps.size() - 1);
        AnimationStep step = animationSteps.get(idx);
        if (v.equals(step.currentNode)) return nodeActive;
        if (step.visited.contains(v)) return nodeVisited;
        if (step.queued.contains(v)) return nodeQueued;
        return nodeDefault;
    }
    
    private boolean isNodeActiveAtStep(String v) {
        if (!animationRunning || animationSteps.isEmpty()) return false;
        int idx = Math.min(currentStepIdx, animationSteps.size() - 1);
        return v.equals(animationSteps.get(idx).currentNode);
    }
    
    private Set<String> getActiveEdgesAtStep() {
        if (!animationRunning || animationSteps.isEmpty()) return new HashSet<>();
        int idx = Math.min(currentStepIdx, animationSteps.size() - 1);
        return animationSteps.get(idx).activeEdges;
    }

    // ── Custom Drawing Panel Component ────────────────────────

    private class GraphPanel extends JPanel {
        final JLabel lblStats;
        final JLabel lblHint;
        
        GraphPanel() {
            setBackground(bgColor);
            setLayout(null);
            
            // Tombol Hamburger floating di pojok kanan atas
            btnHamburger = new JButton("✕");
            btnHamburger.setBackground(nodeActive);
            btnHamburger.setForeground(new Color(0x11, 0x11, 0x1B));
            btnHamburger.setFont(new Font("Arial", Font.BOLD, 14));
            btnHamburger.setFocusPainted(false);
            btnHamburger.setBorder(BorderFactory.createEmptyBorder());
            btnHamburger.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnHamburger.addActionListener(e -> toggleSidebar());
            add(btnHamburger);
            
            // Stats Badge floating
            lblStats = new JLabel("Mode: UNDIRECTED  |  Verteks: 0  |  Edge: 0");
            lblStats.setForeground(new Color(0xF5, 0xC2, 0xE7));
            lblStats.setFont(new Font("Arial", Font.BOLD, 11));
            lblStats.setBackground(new Color(0x18, 0x18, 0x25));
            lblStats.setOpaque(true);
            lblStats.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x45, 0x47, 0x5A)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
            ));
            lblStats.setBounds(25, 25, 300, 30);
            add(lblStats);
            
            // Hint floating
            lblHint = new JLabel("💡 Tips: Klik area kosong kanvas untuk tambah Verteks • Tarik & lepas Verteks untuk pindah posisi");
            lblHint.setForeground(new Color(0x58, 0x5B, 0x70));
            lblHint.setFont(new Font("Arial", Font.ITALIC, 11));
            lblHint.setBounds(25, 600, 600, 20);
            add(lblHint);
            
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent event) {
                    btnHamburger.setBounds(getWidth() - 65, 20, 45, 30);
                    lblHint.setBounds(25, getHeight() - 40, 600, 20);
                }
            });
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    Point p = e.getPoint();
                    String clickedNode = null;
                    for (Map.Entry<String, Point2D> entry : nodeCoords.entrySet()) {
                        double dist = p.distance(entry.getValue());
                        if (dist <= nodeRadius) {
                            clickedNode = entry.getKey();
                            break;
                        }
                    }
                    
                    if (clickedNode != null) {
                        draggedNode = clickedNode;
                    } else {
                        // Spawning vertex popup dialog
                        String vName = JOptionPane.showInputDialog(
                            JavaGraphVisualizer.this,
                            "Nama Verteks Baru (1-3 karakter):",
                            "Tambah Verteks",
                            JOptionPane.QUESTION_MESSAGE
                        );
                        if (vName != null) {
                            vName = vName.trim().toUpperCase();
                            if (vName.isEmpty()) return;
                            if (graph.hasVertex(vName)) {
                                JOptionPane.showMessageDialog(JavaGraphVisualizer.this, "Vertex '" + vName + "' sudah ada!", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            graph.addVertex(vName);
                            nodeCoords.put(vName, new Point2D.Double(e.getX(), e.getY()));
                            
                            // Sync updates
                            updateStats();
                            refreshDropdowns();
                            updateViewTab();
                            repaint();
                        }
                    }
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    draggedNode = null;
                }
            });
            
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (draggedNode != null) {
                        int x = Math.max((int)nodeRadius, Math.min(e.getX(), getWidth() - (int)nodeRadius));
                        int y = Math.max((int)nodeRadius, Math.min(e.getY(), getHeight() - (int)nodeRadius));
                        nodeCoords.put(draggedNode, new Point2D.Double(x, y));
                        repaint();
                    }
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            Set<String> vertices = graph.getVertices();
            Set<String> activeEdges = getActiveEdgesAtStep();
            Set<String> drawnEdges = new HashSet<>();
            
            // 1. Gambar Edges
            for (String u : vertices) {
                if (!nodeCoords.containsKey(u)) continue;
                Point2D p1 = nodeCoords.get(u);
                
                for (String v : graph.getNeighbors(u)) {
                    if (!nodeCoords.containsKey(v)) continue;
                    Point2D p2 = nodeCoords.get(v);
                    
                    String key = graph.isDirected() ? (u + "->" + v)
                            : (u.compareTo(v) < 0 ? u + "-" + v : v + "-" + u);
                    if (!graph.isDirected() && drawnEdges.contains(key)) continue;
                    drawnEdges.add(key);
                    
                    double dx = p2.getX() - p1.getX();
                    double dy = p2.getY() - p1.getY();
                    double dist = Math.sqrt(dx*dx + dy*dy);
                    double sx = p1.getX(), sy = p1.getY(), ex = p2.getX(), ey = p2.getY();
                    
                    if (dist > 0) {
                        double ux = dx / dist;
                        double uy = dy / dist;
                        sx = p1.getX() + nodeRadius * ux;
                        sy = p1.getY() + nodeRadius * uy;
                        ex = p2.getX() - nodeRadius * ux;
                        ey = p2.getY() - nodeRadius * uy;
                    }
                    
                    boolean isActive = activeEdges.contains(u + "->" + v) || 
                            (!graph.isDirected() && (activeEdges.contains(u + "->" + v) || activeEdges.contains(v + "->" + u)));
                    
                    g2d.setColor(isActive ? edgeActive : edgeDefault);
                    g2d.setStroke(new BasicStroke(isActive ? 3f : 2f));
                    
                    if (graph.isDirected()) {
                        drawArrow(g2d, sx, sy, ex, ey);
                    } else {
                        g2d.draw(new Line2D.Double(sx, sy, ex, ey));
                    }
                }
            }
            
            // 2. Gambar Nodes (Vertex)
            for (String v : vertices) {
                if (!nodeCoords.containsKey(v)) continue;
                Point2D cp = nodeCoords.get(v);
                
                Color fill = getNodeColorAtStep(v);
                boolean isActive = isNodeActiveAtStep(v);
                double r = nodeRadius + (isActive ? 4 : 0);
                
                g2d.setColor(fill);
                g2d.fill(new Ellipse2D.Double(cp.getX() - r, cp.getY() - r, r * 2, r * 2));
                
                g2d.setColor(new Color(0x45, 0x47, 0x5A));
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(new Ellipse2D.Double(cp.getX() - r, cp.getY() - r, r * 2, r * 2));
                
                g2d.setColor(textColor);
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                double tx = cp.getX() - fm.stringWidth(v) / 2.0;
                double ty = cp.getY() + fm.getAscent() / 2.0 - 2;
                g2d.drawString(v, (float)tx, (float)ty);
            }
            
            // 3. Gambar Tracer Partikel
            if (particleAnimating && particlePos != null) {
                g2d.setColor(new Color(0x89, 0xD4, 0xEB));
                g2d.fill(new Ellipse2D.Double(particlePos.getX() - 6, particlePos.getY() - 6, 12, 12));
                g2d.setColor(new Color(0xF3, 0x8B, 0xA8));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.draw(new Ellipse2D.Double(particlePos.getX() - 6, particlePos.getY() - 6, 12, 12));
            }
        }
        
        private void drawArrow(Graphics2D g2, double x1, double y1, double x2, double y2) {
            double angle = Math.atan2(y2 - y1, x2 - x1);
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
            
            int arrowSize = 10;
            Path2D path = new Path2D.Double();
            path.moveTo(x2, y2);
            path.lineTo(x2 - arrowSize * Math.cos(angle - Math.PI / 6), y2 - arrowSize * Math.sin(angle - Math.PI / 6));
            path.lineTo(x2 - arrowSize * Math.cos(angle + Math.PI / 6), y2 - arrowSize * Math.sin(angle + Math.PI / 6));
            path.closePath();
            g2.fill(path);
        }
    }
    
    private void toggleSidebar() {
        if (sidebarVisible) {
            remove(sidebar);
            sidebarVisible = false;
            btnHamburger.setText("☰");
            btnHamburger.setBackground(nodeDefault);
            btnHamburger.setForeground(textColor);
        } else {
            add(sidebar, BorderLayout.EAST);
            sidebarVisible = true;
            btnHamburger.setText("✕");
            btnHamburger.setBackground(nodeActive);
            btnHamburger.setForeground(new Color(0x11, 0x11, 0x1B));
        }
        revalidate();
        repaint();
    }
}


// ─────────────────────────────────────────────────────────────
//  Class utama (Main)
// ─────────────────────────────────────────────────────────────

public class javaX {
    public static void main(String[] args) {
        Graph graph = new Graph(false);

        // Pre-load contoh data awal
        for (String v : new String[]{"A","B","C","D","E"}) graph.addVertex(v);
        String[][] edges = {{"A","B"},{"A","C"},{"B","D"},{"C","D"},{"D","E"}};
        for (String[] e : edges) graph.addEdge(e[0], e[1]);

        SwingUtilities.invokeLater(() -> {
            JavaGraphVisualizer visualizer = new JavaGraphVisualizer(graph);
            visualizer.setVisible(true);
        });
    }
}
