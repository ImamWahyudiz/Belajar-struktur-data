#!/usr/bin/env python3
"""
Program Manajemen Graf (Graph)
Implementasi Graf dengan Adjacency List (undirected/directed)

Program ini mendemonstrasikan operasi-operasi dasar pada struktur data Graf:
  - Tambah / Hapus Vertex
  - Tambah / Hapus Edge
  - Tampilkan Graph (ASCII visual + Adjacency Matrix)
  - Traversal DFS (Depth-First Search)
  - Traversal BFS (Breadth-First Search)

Representasi Internal : Adjacency List (dict of list)
Jenis Graf            : Undirected (tidak berarah) secara default
"""

import sys
import os
from collections import deque
import math
import random

try:
    import tkinter as tk
    from tkinter import ttk, messagebox, simpledialog
    TK_AVAILABLE = True
except ImportError:
    TK_AVAILABLE = False


# Paksa stdout menggunakan UTF-8 agar karakter box-drawing tampil benar di Windows
if sys.stdout.encoding and sys.stdout.encoding.lower() != 'utf-8':
    sys.stdout.reconfigure(encoding='utf-8')


# ─────────────────────────────────────────────────────────────
#  Konstanta warna ANSI (terminal)
# ─────────────────────────────────────────────────────────────
RESET   = "\033[0m"
BOLD    = "\033[1m"
CYAN    = "\033[96m"
GREEN   = "\033[92m"
YELLOW  = "\033[93m"
RED     = "\033[91m"
MAGENTA = "\033[95m"
BLUE    = "\033[94m"
DIM     = "\033[2m"


def supports_ansi():
    """Cek apakah terminal mendukung warna ANSI."""
    if os.name == 'nt':
        try:
            import ctypes
            kernel = ctypes.windll.kernel32
            kernel.SetConsoleMode(kernel.GetStdHandle(-11), 7)
            return True
        except Exception:
            return False
    return True


USE_COLOR = supports_ansi()


def c(text, color):
    """Wrapper pewarnaan teks."""
    return f"{color}{text}{RESET}" if USE_COLOR else text


# ─────────────────────────────────────────────────────────────
#  Class Graph
# ─────────────────────────────────────────────────────────────

class Graph:
    """
    Implementasi Graf menggunakan Adjacency List.

    Mendukung graf undirected (tidak berarah). Setiap vertex diwakili
    oleh sebuah kunci string. Edge disimpan sebagai list tetangga.

    Attributes:
        adj (dict): Adjacency list, key = nama vertex, value = list tetangga
        directed (bool): True jika graf berarah (directed)

    Kompleksitas:
        Tambah Vertex : O(1)
        Hapus Vertex  : O(V + E)
        Tambah Edge   : O(1)
        Hapus Edge    : O(degree)
        DFS / BFS     : O(V + E)
    """

    def __init__(self, directed=False):
        """
        Inisialisasi graf kosong.

        Args:
            directed (bool): True = directed graph, False = undirected graph
        """
        self.adj = {}          # { vertex: [tetangga1, tetangga2, ...] }
        self.directed = directed

    # ── Vertex ────────────────────────────────────────────────

    def add_vertex(self, v):
        """
        Tambah vertex baru ke dalam graf.

        Args:
            v (str): Nama vertex

        Returns:
            bool: True jika berhasil, False jika vertex sudah ada
        """
        v = str(v).strip().upper()
        if v in self.adj:
            return False
        self.adj[v] = []
        return True

    def remove_vertex(self, v):
        """
        Hapus vertex beserta semua edge yang terhubung.

        Args:
            v (str): Nama vertex yang akan dihapus

        Returns:
            bool: True jika berhasil, False jika vertex tidak ditemukan
        """
        v = str(v).strip().upper()
        if v not in self.adj:
            return False

        # Hapus semua edge yang mengarah ke v dari vertex lain
        for u in self.adj:
            if v in self.adj[u]:
                self.adj[u].remove(v)

        del self.adj[v]
        return True

    def get_vertices(self):
        """Kembalikan daftar vertex terurut."""
        return sorted(self.adj.keys())

    def vertex_count(self):
        """Jumlah vertex dalam graf."""
        return len(self.adj)

    # ── Edge ──────────────────────────────────────────────────

    def add_edge(self, u, v):
        """
        Tambah edge antara vertex u dan v.

        Untuk undirected graph, edge ditambahkan di kedua arah.

        Args:
            u (str): Vertex asal
            v (str): Vertex tujuan

        Returns:
            bool: True jika berhasil, False jika gagal
        """
        u = str(u).strip().upper()
        v = str(v).strip().upper()

        if u not in self.adj or v not in self.adj:
            return False   # Salah satu vertex tidak ada
        if u == v:
            return False   # Self-loop tidak diizinkan
        if v in self.adj[u]:
            return False   # Edge sudah ada

        self.adj[u].append(v)
        if not self.directed:
            self.adj[v].append(u)
        return True

    def remove_edge(self, u, v):
        """
        Hapus edge antara vertex u dan v.

        Args:
            u (str): Vertex asal
            v (str): Vertex tujuan

        Returns:
            bool: True jika berhasil, False jika edge tidak ditemukan
        """
        u = str(u).strip().upper()
        v = str(v).strip().upper()

        if u not in self.adj or v not in self.adj:
            return False
        if v not in self.adj[u]:
            return False

        self.adj[u].remove(v)
        if not self.directed and u in self.adj[v]:
            self.adj[v].remove(u)
        return True

    def edge_count(self):
        """Jumlah edge dalam graf."""
        total = sum(len(neighbors) for neighbors in self.adj.values())
        return total if self.directed else total // 2

    def has_edge(self, u, v):
        """Cek apakah edge u→v ada."""
        u = str(u).strip().upper()
        v = str(v).strip().upper()
        return u in self.adj and v in self.adj[u]

    # ── Traversal ─────────────────────────────────────────────

    def dfs(self, start):
        """
        Depth-First Search dari vertex start.
        Menggunakan stack (rekursif / iteratif).

        Args:
            start (str): Vertex awal traversal

        Returns:
            list: Urutan vertex yang dikunjungi
            None: Jika vertex start tidak ada
        """
        start = str(start).strip().upper()
        if start not in self.adj:
            return None

        visited = []
        stack = [start]
        seen = set()

        while stack:
            vertex = stack.pop()
            if vertex not in seen:
                seen.add(vertex)
                visited.append(vertex)
                # Tambah tetangga ke stack (dibalik agar urutan terjaga)
                for neighbor in reversed(sorted(self.adj[vertex])):
                    if neighbor not in seen:
                        stack.append(neighbor)

        return visited

    def bfs(self, start):
        """
        Breadth-First Search dari vertex start.
        Menggunakan queue.

        Args:
            start (str): Vertex awal traversal

        Returns:
            list: Urutan vertex yang dikunjungi
            None: Jika vertex start tidak ada
        """
        start = str(start).strip().upper()
        if start not in self.adj:
            return None

        visited = []
        queue = deque([start])
        seen = {start}

        while queue:
            vertex = queue.popleft()
            visited.append(vertex)
            for neighbor in sorted(self.adj[vertex]):
                if neighbor not in seen:
                    seen.add(neighbor)
                    queue.append(neighbor)

        return visited

    # ── Display ───────────────────────────────────────────────

    def display_adjacency_list(self):
        """Tampilkan graf sebagai adjacency list."""
        vertices = self.get_vertices()
        kind = "DIRECTED" if self.directed else "UNDIRECTED"

        print()
        print(c("╔══════════════════════════════════════════════════════════════╗", CYAN))
        print(c(f"║        ADJACENCY LIST — {kind:<37}║", CYAN))
        print(c("╠══════════════════════════════════════════════════════════════╣", CYAN))

        if not vertices:
            print(c("║  (graf kosong)                                               ║", DIM))
        else:
            for v in vertices:
                neighbors = sorted(self.adj[v])
                arrow = c("→", YELLOW)
                if neighbors:
                    chain = f" {arrow} ".join(c(n, GREEN) for n in neighbors)
                    print(f"  {c(v, BOLD + CYAN):>4}  :  {chain}")
                else:
                    print(f"  {c(v, BOLD + CYAN):>4}  :  {c('(tidak ada tetangga)', DIM)}")

        print(c("╠══════════════════════════════════════════════════════════════╣", CYAN))
        print(f"  Vertex : {c(str(self.vertex_count()), YELLOW)}   |   "
              f"Edge : {c(str(self.edge_count()), YELLOW)}   |   "
              f"Jenis : {c(kind, MAGENTA)}")
        print(c("╚══════════════════════════════════════════════════════════════╝", CYAN))
        print()

    def display_adjacency_matrix(self):
        """Tampilkan graf sebagai adjacency matrix."""
        vertices = self.get_vertices()
        n = len(vertices)

        print()
        print(c("╔══════════════════════════════════════════════════════════════╗", CYAN))
        print(c("║                   ADJACENCY MATRIX                          ║", CYAN))
        print(c("╠══════════════════════════════════════════════════════════════╣", CYAN))

        if n == 0:
            print(c("║  (graf kosong)                                               ║", DIM))
            print(c("╚══════════════════════════════════════════════════════════════╝", CYAN))
            print()
            return

        idx = {v: i for i, v in enumerate(vertices)}

        # Baris header
        header = "     " + "  ".join(f"{c(v, CYAN):>3}" for v in vertices)
        print(f"  {header}")
        print("  " + "─" * (5 + 5 * n))

        for u in vertices:
            row = f"  {c(u, BOLD + CYAN):>3} │"
            for v in vertices:
                if u == v:
                    row += f"  {c('-', DIM)}"
                elif self.has_edge(u, v):
                    row += f"  {c('1', GREEN)}"
                else:
                    row += f"  {c('0', DIM)}"
            print(row)

        print(c("╚══════════════════════════════════════════════════════════════╝", CYAN))
        print()

    def display_ascii_graph(self):
        """Tampilkan visualisasi ASCII sederhana dari graf."""
        vertices = self.get_vertices()
        print()
        print(c("╔══════════════════════════════════════════════════════════════╗", MAGENTA))
        print(c("║              VISUALISASI GRAF (ASCII)                        ║", MAGENTA))
        print(c("╠══════════════════════════════════════════════════════════════╣", MAGENTA))

        if not vertices:
            print(c("║  (graf kosong)                                               ║", DIM))
            print(c("╚══════════════════════════════════════════════════════════════╝", MAGENTA))
            print()
            return

        printed_edges = set()
        for v in vertices:
            neighbors = sorted(self.adj[v])
            for n in neighbors:
                edge_key = tuple(sorted([v, n])) if not self.directed else (v, n)
                if edge_key not in printed_edges:
                    printed_edges.add(edge_key)
                    connector = c("──►", YELLOW) if self.directed else c("───", YELLOW)
                    print(f"  [{c(v, BOLD + CYAN)}] {connector} [{c(n, BOLD + GREEN)}]")

        if not printed_edges:
            print(c("  (tidak ada edge)", DIM))

        print(c("╚══════════════════════════════════════════════════════════════╝", MAGENTA))
        print()

    def display_traversal(self, order, algo_name, start):
        """Tampilkan hasil traversal dengan visualisasi panah."""
        print()
        print(c(f"╔══════════════════════════════════════════════════════════════╗", BLUE))
        print(c(f"║          HASIL TRAVERSAL {algo_name:<37}║", BLUE))
        print(c(f"╠══════════════════════════════════════════════════════════════╣", BLUE))
        print(f"  Start  : {c(start, YELLOW)}")
        print(f"  Urutan : ", end="")

        arrow = c(" → ", CYAN)
        parts = []
        for i, v in enumerate(order):
            num = c(f"[{i+1}]", DIM)
            node = c(v, BOLD + GREEN)
            parts.append(f"{num}{node}")

        print(arrow.join(parts))
        print(c("╚══════════════════════════════════════════════════════════════╝", BLUE))
        print()


# ─────────────────────────────────────────────────────────────
#  Class GraphVisualizer (GUI)
# ─────────────────────────────────────────────────────────────

class GraphVisualizer:
    """
    Jendela GUI interaktif untuk visualisasi graf dan animasi DFS/BFS.
    Menggunakan library standard Tkinter (tanpa dependensi eksternal).
    """
    def __init__(self, graph):
        self.graph = graph
        self.root = tk.Tk()
        self.root.title("Visualisasi Graf & Animasi Traversal (Belajar Struktur Data)")
        self.root.geometry("1100x700")
        self.root.configure(bg="#1e1e2e")
        self.root.minsize(950, 600)
        
        # Tema warna modern (Catppuccin Mocha Palette)
        self.bg_color = "#1e1e2e"
        self.panel_bg = "#181825"
        self.node_default = "#313244"
        self.node_active = "#f38ba8"      # current active node (pink/red)
        self.node_queued = "#f9e2af"      # in queue or stack (yellow)
        self.node_visited = "#a6e3a1"     # completed visit (green)
        self.edge_default = "#45475a"
        self.edge_active = "#89b4fa"      # traversed edge (blue)
        self.text_color = "#cdd6f4"
        
        # Container utama dengan layout pack
        self.main_container = tk.Frame(self.root, bg=self.bg_color)
        self.main_container.pack(fill=tk.BOTH, expand=True)
        
        # Container Canvas (kiri)
        self.canvas_frame = tk.Frame(self.main_container, bg=self.bg_color)
        self.canvas_frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        # Canvas gambar graf
        self.canvas = tk.Canvas(self.canvas_frame, bg=self.bg_color, highlightthickness=0)
        self.canvas.pack(fill=tk.BOTH, expand=True, padx=15, pady=15)
        
        # Panel kontrol (kanan)
        self.sidebar = tk.Frame(self.main_container, bg=self.panel_bg, padx=15, pady=15, width=300)
        self.sidebar.pack(side=tk.RIGHT, fill=tk.Y)
        self.sidebar.pack_propagate(False) # Mengunci lebar 300px
        self.sidebar.columnconfigure(0, weight=1)
        
        self.sidebar_visible = True
        
        # Tombol Hamburger melayang (floating) di pojok kanan atas canvas_frame
        self.btn_hamburger = tk.Button(
            self.canvas_frame, 
            text="✕", 
            bg="#f38ba8", 
            fg="#11111b", 
            font=("Arial", 14, "bold"), 
            relief="flat", 
            bd=0, 
            width=3, 
            height=1,
            activebackground="#e07a96",
            activeforeground="#11111b",
            cursor="hand2",
            command=self.toggle_sidebar
        )
        self.btn_hamburger.place(relx=1.0, rely=0.0, anchor="ne", x=-25, y=25)
        
        # Label Statistik melayang di pojok kiri atas canvas
        self.lbl_stats = tk.Label(
            self.canvas_frame, 
            text="Verteks: 0  |  Edge: 0", 
            bg="#181825", 
            fg="#f5c2e7", 
            font=("Arial", 10, "bold"), 
            padx=12, 
            pady=6, 
            highlightthickness=1, 
            highlightbackground="#45475a"
        )
        self.lbl_stats.place(x=25, y=25)
        
        # Petunjuk penggunaan melayang di pojok kiri bawah canvas
        self.lbl_hint = tk.Label(
            self.canvas_frame,
            text="💡 Tips: Klik area kosong kanvas untuk tambah Verteks • Tarik & lepas Verteks untuk pindah posisi",
            bg=self.bg_color,
            fg="#585b70",
            font=("Arial", 9, "italic")
        )
        self.lbl_hint.place(x=25, rely=1.0, anchor="sw", y=-25)
        
        # Koordinat dan ukuran vertex
        self.node_coords = {} # {vertex: (x, y)}
        self.node_radius = 22
        
        # Event handlers drag-and-drop
        self.dragged_node = None
        self.canvas.bind("<Button-1>", self.on_canvas_click)
        self.canvas.bind("<B1-Motion>", self.on_canvas_drag)
        self.canvas.bind("<ButtonRelease-1>", self.on_canvas_release)
        self.canvas.bind("<Configure>", lambda e: self.draw_graph())
        
        # Status animasi
        self.animation_running = False
        self.animation_paused = False
        self.animation_steps = []
        self.current_step_idx = 0
        self.animation_speed = 1000 # default: 1 detik delay
        self.current_algo = ""
        self.animation_timer = None
        
        # Setup UI Notebook & Tabs
        self.setup_controls()
        self.init_layout()
        self.update_stats()
        self.refresh_dropdowns()
        self.update_view_tab()
        self.draw_graph()
        
        self.root.protocol("WM_DELETE_WINDOW", self.on_close)

    def init_layout(self):
        """Memposisikan vertex secara melingkar di tengah canvas."""
        vertices = self.graph.get_vertices()
        if not vertices:
            return
        
        cx, cy = 350, 300
        r = 180
        n = len(vertices)
        
        for i, v in enumerate(vertices):
            angle = 2 * math.pi * i / n
            x = cx + r * math.cos(angle)
            y = cy + r * math.sin(angle)
            self.node_coords[v] = (x, y)

    def setup_controls(self):
        """Membangun sistem Tabs Notebook di sidebar."""
        # Style Notebook
        style = ttk.Style()
        style.theme_use('clam')
        style.configure('TNotebook', background=self.panel_bg, borderwidth=0)
        style.configure('TNotebook.Tab', background='#313244', foreground='#cdd6f4', padding=[12, 4], font=('Arial', 9, 'bold'))
        style.map('TNotebook.Tab', background=[('selected', '#1e1e2e')], foreground=[('selected', '#f5c2e7')])
        style.configure('TCombobox', fieldbackground='#313244', background='#181825', foreground='#cdd6f4')
        
        self.notebook = ttk.Notebook(self.sidebar)
        self.notebook.pack(fill=tk.BOTH, expand=True)
        
        # Tab Frames
        self.tab_manage = tk.Frame(self.notebook, bg=self.panel_bg, padx=10, pady=10)
        self.tab_traversal = tk.Frame(self.notebook, bg=self.panel_bg, padx=10, pady=10)
        self.tab_view = tk.Frame(self.notebook, bg=self.panel_bg, padx=10, pady=10)
        self.tab_log = tk.Frame(self.notebook, bg=self.panel_bg, padx=10, pady=10)
        
        self.notebook.add(self.tab_manage, text="Kelola")
        self.notebook.add(self.tab_traversal, text="Traversal")
        self.notebook.add(self.tab_view, text="View")
        self.notebook.add(self.tab_log, text="Log")
        
        # ── 1. Tab Kelola ─────────────────────────────────────
        # Mode Graf
        title_mode = tk.Label(self.tab_manage, text="MODE GRAF", bg=self.panel_bg, fg="#f5c2e7", font=("Arial", 10, "bold"))
        title_mode.pack(anchor="w", pady=(0, 5))
        
        self.btn_mode = tk.Button(
            self.tab_manage, 
            text="Mode: UNDIRECTED", 
            bg="#313244", 
            fg=self.text_color, 
            font=("Arial", 10, "bold"), 
            relief="flat", 
            command=self.toggle_mode,
            cursor="hand2"
        )
        self.btn_mode.pack(fill="x", pady=(0, 15))
        
        # CRUD Verteks
        title_v = tk.Label(self.tab_manage, text="KELOLA VERTEKS", bg=self.panel_bg, fg="#f5c2e7", font=("Arial", 10, "bold"))
        title_v.pack(anchor="w", pady=(0, 5))
        
        lbl_add_v = tk.Label(self.tab_manage, text="Tambah Verteks baru:", bg=self.panel_bg, fg=self.text_color, font=("Arial", 9))
        lbl_add_v.pack(anchor="w")
        self.ent_add_v = tk.Entry(self.tab_manage, bg="#313244", fg=self.text_color, insertbackground=self.text_color, relief="flat", font=("Arial", 10))
        self.ent_add_v.pack(fill="x", pady=(2, 5))
        self.btn_add_v = tk.Button(self.tab_manage, text="＋ Tambah Verteks", bg="#a6e3a1", fg="#11111b", font=("Arial", 9, "bold"), relief="flat", command=self.add_vertex_gui, cursor="hand2")
        self.btn_add_v.pack(fill="x", pady=(0, 10))
        
        lbl_del_v = tk.Label(self.tab_manage, text="Hapus Verteks:", bg=self.panel_bg, fg=self.text_color, font=("Arial", 9))
        lbl_del_v.pack(anchor="w")
        self.cb_del_v = ttk.Combobox(self.tab_manage, state="readonly")
        self.cb_del_v.pack(fill="x", pady=(2, 5))
        self.btn_del_v = tk.Button(self.tab_manage, text="－ Hapus Verteks", bg="#f38ba8", fg="#11111b", font=("Arial", 9, "bold"), relief="flat", command=self.del_vertex_gui, cursor="hand2")
        self.btn_del_v.pack(fill="x", pady=(0, 20))
        
        # CRUD Edge
        title_e = tk.Label(self.tab_manage, text="KELOLA EDGE (SISI)", bg=self.panel_bg, fg="#f5c2e7", font=("Arial", 10, "bold"))
        title_e.pack(anchor="w", pady=(0, 5))
        
        lbl_add_e = tk.Label(self.tab_manage, text="Tambah Edge (Asal → Tujuan):", bg=self.panel_bg, fg=self.text_color, font=("Arial", 9))
        lbl_add_e.pack(anchor="w")
        edge_frame1 = tk.Frame(self.tab_manage, bg=self.panel_bg)
        edge_frame1.pack(fill="x", pady=(2, 5))
        self.cb_edge_u1 = ttk.Combobox(edge_frame1, state="readonly", width=8)
        self.cb_edge_u1.pack(side=tk.LEFT, fill="x", expand=True, padx=(0, 5))
        self.cb_edge_v1 = ttk.Combobox(edge_frame1, state="readonly", width=8)
        self.cb_edge_v1.pack(side=tk.RIGHT, fill="x", expand=True, padx=(5, 0))
        self.btn_add_e = tk.Button(self.tab_manage, text="＋ Hubungkan Edge", bg="#89b4fa", fg="#11111b", font=("Arial", 9, "bold"), relief="flat", command=self.add_edge_gui, cursor="hand2")
        self.btn_add_e.pack(fill="x", pady=(0, 10))
        
        lbl_del_e = tk.Label(self.tab_manage, text="Hapus Edge (Asal → Tujuan):", bg=self.panel_bg, fg=self.text_color, font=("Arial", 9))
        lbl_del_e.pack(anchor="w")
        edge_frame2 = tk.Frame(self.tab_manage, bg=self.panel_bg)
        edge_frame2.pack(fill="x", pady=(2, 5))
        self.cb_edge_u2 = ttk.Combobox(edge_frame2, state="readonly", width=8)
        self.cb_edge_u2.pack(side=tk.LEFT, fill="x", expand=True, padx=(0, 5))
        self.cb_edge_v2 = ttk.Combobox(edge_frame2, state="readonly", width=8)
        self.cb_edge_v2.pack(side=tk.RIGHT, fill="x", expand=True, padx=(5, 0))
        self.btn_del_e = tk.Button(self.tab_manage, text="－ Putuskan Edge", bg="#f9e2af", fg="#11111b", font=("Arial", 9, "bold"), relief="flat", command=self.del_edge_gui, cursor="hand2")
        self.btn_del_e.pack(fill="x", pady=(0, 10))
        
        # ── 2. Tab Traversal ──────────────────────────────────
        lbl_start = tk.Label(self.tab_traversal, text="Pilih Node Awal:", bg=self.panel_bg, fg=self.text_color, font=("Arial", 10))
        lbl_start.pack(anchor="w", pady=(0, 5))
        
        self.cb_start = ttk.Combobox(self.tab_traversal, state="readonly")
        self.cb_start.pack(fill="x", pady=(0, 15))
        
        # Tombol Jalankan
        btn_frame = tk.Frame(self.tab_traversal, bg=self.panel_bg)
        btn_frame.pack(fill="x", pady=(0, 15))
        btn_frame.columnconfigure(0, weight=1)
        btn_frame.columnconfigure(1, weight=1)
        
        self.btn_dfs = tk.Button(btn_frame, text="Mulai DFS", bg="#a6e3a1", fg="#11111b", font=("Arial", 10, "bold"), relief="flat", command=self.start_dfs, activebackground="#8de087", cursor="hand2")
        self.btn_dfs.grid(row=0, column=0, padx=(0, 5), sticky="ew")
        
        self.btn_bfs = tk.Button(btn_frame, text="Mulai BFS", bg="#89b4fa", fg="#11111b", font=("Arial", 10, "bold"), relief="flat", command=self.start_bfs, activebackground="#74a2ef", cursor="hand2")
        self.btn_bfs.grid(row=0, column=1, padx=(5, 0), sticky="ew")
        
        # Kontrol Media
        media_frame = tk.Frame(self.tab_traversal, bg=self.panel_bg)
        media_frame.pack(fill="x", pady=(0, 15))
        media_frame.columnconfigure(0, weight=1)
        media_frame.columnconfigure(1, weight=1)
        media_frame.columnconfigure(2, weight=1)
        
        self.btn_play = tk.Button(media_frame, text="Pause", state="disabled", bg="#cba6f7", fg="#11111b", font=("Arial", 9, "bold"), relief="flat", command=self.toggle_pause, cursor="hand2")
        self.btn_play.grid(row=0, column=0, padx=2, sticky="ew")
        
        self.btn_step = tk.Button(media_frame, text="Step >", state="disabled", bg="#f9e2af", fg="#11111b", font=("Arial", 9, "bold"), relief="flat", command=self.step_forward, cursor="hand2")
        self.btn_step.grid(row=0, column=1, padx=2, sticky="ew")
        
        self.btn_reset = tk.Button(media_frame, text="Reset", bg="#eeba90", fg="#11111b", font=("Arial", 9, "bold"), relief="flat", command=self.reset_animation)
        self.btn_reset.grid(row=0, column=2, padx=2, sticky="ew")
        
        # Slider Kecepatan
        lbl_speed = tk.Label(self.tab_traversal, text="Kecepatan Delay (ms):", bg=self.panel_bg, fg=self.text_color, font=("Arial", 10))
        lbl_speed.pack(anchor="w", pady=(0, 5))
        
        self.slider_speed = tk.Scale(self.tab_traversal, from_=200, to=3000, orient="horizontal", bg=self.panel_bg, fg=self.text_color, highlightbackground=self.panel_bg, resolution=100, command=self.on_speed_change)
        self.slider_speed.set(self.animation_speed)
        self.slider_speed.pack(fill="x", pady=(0, 15))
        
        # Status Isi Stack/Queue
        self.struct_label = tk.Label(self.tab_traversal, text="Isi Struktur Data:", bg=self.panel_bg, fg=self.text_color, font=("Arial", 10, "bold"))
        self.struct_label.pack(anchor="w", pady=(0, 5))
        
        self.struct_content = tk.Label(self.tab_traversal, text="(kosong)", bg="#313244", fg="#f9e2af", font=("Courier", 11, "bold"), anchor="w", padx=10, pady=6, justify="left", wraplength=220)
        self.struct_content.pack(fill="x", pady=(0, 15))
        
        # ── 3. Tab View ───────────────────────────────────────
        view_lbl = tk.Label(self.tab_view, text="Adjacency List:", bg=self.panel_bg, fg="#f5c2e7", font=("Arial", 10, "bold"))
        view_lbl.pack(anchor="w", pady=(0, 5))
        
        self.txt_adj_list = tk.Text(self.tab_view, bg="#1e1e2e", fg="#cdd6f4", font=("Consolas", 9), wrap="none", height=12, highlightthickness=1, highlightbackground="#45475a")
        self.txt_adj_list.pack(fill="both", expand=True, pady=(0, 15))
        
        matrix_lbl = tk.Label(self.tab_view, text="Adjacency Matrix:", bg=self.panel_bg, fg="#f5c2e7", font=("Arial", 10, "bold"))
        matrix_lbl.pack(anchor="w", pady=(0, 5))
        
        self.txt_adj_matrix = tk.Text(self.tab_view, bg="#1e1e2e", fg="#cdd6f4", font=("Consolas", 9), wrap="none", height=12, highlightthickness=1, highlightbackground="#45475a")
        self.txt_adj_matrix.pack(fill="both", expand=True)
        
        # ── 4. Tab Log ────────────────────────────────────────
        lbl_log = tk.Label(self.tab_log, text="Log Penjelasan Langkah:", bg=self.panel_bg, fg=self.text_color, font=("Arial", 10, "bold"))
        lbl_log.pack(anchor="w", pady=(0, 5))
        
        log_container = tk.Frame(self.tab_log, bg=self.panel_bg)
        log_container.pack(fill="both", expand=True)
        
        self.log_text = tk.Text(log_container, bg="#1e1e2e", fg="#cdd6f4", font=("Consolas", 9), wrap="word", state="disabled", highlightthickness=1, highlightbackground="#45475a")
        self.log_text.pack(side="left", fill="both", expand=True)
        
        scrollbar = tk.Scrollbar(log_container, command=self.log_text.yview)
        scrollbar.pack(side="right", fill="y")
        self.log_text.config(yscrollcommand=scrollbar.set)

    def toggle_sidebar(self):
        """Menampilkan atau menyembunyikan panel kontrol (sidebar)."""
        if self.sidebar_visible:
            self.sidebar.pack_forget()
            self.sidebar_visible = False
            self.btn_hamburger.config(text="☰", bg="#313244", fg=self.text_color)
        else:
            self.sidebar.pack(side=tk.RIGHT, fill=tk.Y)
            self.sidebar.pack_propagate(False)
            self.sidebar_visible = True
            self.btn_hamburger.config(text="✕", bg="#f38ba8", fg="#11111b")

    def draw_graph(self):
        """Menggambar visualisasi graf (edge dan vertex) pada canvas."""
        self.canvas.delete("all")
        vertices = self.graph.get_vertices()
        drawn_edges = set()
        
        active_edges = self.get_active_edges_at_step()
        
        # 1. Gambar Edge
        for u in vertices:
            if u not in self.node_coords:
                continue
            x1, y1 = self.node_coords[u]
            for v in self.graph.adj[u]:
                if v not in self.node_coords:
                    continue
                x2, y2 = self.node_coords[v]
                
                edge_key = tuple(sorted([u, v])) if not self.graph.directed else (u, v)
                if not self.graph.directed and edge_key in drawn_edges:
                    continue
                drawn_edges.add(edge_key)
                
                # Menghitung titik potong garis di pinggir lingkaran node
                dx = x2 - x1
                dy = y2 - y1
                dist = math.sqrt(dx**2 + dy**2)
                if dist > 0:
                    ux = dx / dist
                    uy = dy / dist
                    sx = x1 + self.node_radius * ux
                    sy = y1 + self.node_radius * uy
                    ex = x2 - self.node_radius * ux
                    ey = y2 - self.node_radius * uy
                else:
                    sx, sy, ex, ey = x1, y1, x2, y2
                
                # Penentuan warna edge
                is_active = (u, v) in active_edges or (not self.graph.directed and (v, u) in active_edges)
                color = self.edge_active if is_active else self.edge_default
                width = 3 if is_active else 2
                
                if self.graph.directed:
                    self.canvas.create_line(sx, sy, ex, ey, fill=color, width=width, arrow=tk.LAST, arrowshape=(12, 14, 5))
                else:
                    self.canvas.create_line(sx, sy, ex, ey, fill=color, width=width)
        
        # 2. Gambar Node (Vertex)
        for v in vertices:
            if v not in self.node_coords:
                continue
            x, y = self.node_coords[v]
            
            color = self.get_node_color_at_step(v)
            
            # Efek pulse (membesar) untuk node yang sedang aktif dikunjungi
            r = self.node_radius
            if self.animation_running and self.is_node_active_at_step(v):
                r = self.node_radius + 4
                
            self.canvas.create_oval(x - r, y - r, x + r, y + r, fill=color, outline="#45475a", width=2)
            self.canvas.create_text(x, y, text=v, fill="#cdd6f4", font=("Arial", 11, "bold"))

    # ── Traversal Steps Recorder ──────────────────────────────

    def record_dfs_steps(self, start):
        """Mensimulasikan DFS dan mencatat state pada setiap langkah."""
        steps = []
        sim_seen = set()
        sim_stack = [start]
        sim_visited = []
        sim_active_edges = set()
        sim_parent = {}
        
        # Frame Awal
        steps.append({
            "current_node": None,
            "visited": list(sim_visited),
            "queued": list(sim_stack),
            "active_edges": set(sim_active_edges),
            "log": f"Memulai DFS dari vertex '{start}'. Memasukkan '{start}' ke Stack.",
            "structure": list(sim_stack)
        })
        
        while sim_stack:
            curr = sim_stack.pop()
            
            if curr not in sim_seen:
                sim_seen.add(curr)
                sim_visited.append(curr)
                
                if curr in sim_parent:
                    sim_active_edges.add((sim_parent[curr], curr))
                
                steps.append({
                    "current_node": curr,
                    "visited": list(sim_visited),
                    "queued": list(sim_stack),
                    "active_edges": set(sim_active_edges),
                    "log": f"Pop '{curr}' dari Stack. Kunjungi '{curr}' (node berubah hijau).",
                    "structure": list(sim_stack) + [curr]
                })
                
                # Tetangga dimasukkan terbalik (seperti kode python.py)
                neighbors = list(reversed(sorted(self.graph.adj[curr])))
                added = []
                for neighbor in neighbors:
                    if neighbor not in sim_seen:
                        sim_stack.append(neighbor)
                        sim_parent[neighbor] = curr
                        added.append(neighbor)
                
                if added:
                    steps.append({
                        "current_node": curr,
                        "visited": list(sim_visited),
                        "queued": list(sim_stack),
                        "active_edges": set(sim_active_edges),
                        "log": f"Masukkan tetangga '{', '.join(reversed(added))}' ke Stack (node berubah kuning).",
                        "structure": list(sim_stack)
                    })
                    
        steps.append({
            "current_node": None,
            "visited": list(sim_visited),
            "queued": [],
            "active_edges": set(sim_active_edges),
            "log": "DFS Selesai! Semua vertex yang terjangkau telah dikunjungi.",
            "structure": []
        })
        return steps

    def record_bfs_steps(self, start):
        """Mensimulasikan BFS dan mencatat state pada setiap langkah."""
        steps = []
        sim_queue = deque([start])
        sim_seen = {start}
        sim_visited = []
        sim_active_edges = set()
        sim_parent = {}
        
        steps.append({
            "current_node": None,
            "visited": list(sim_visited),
            "queued": list(sim_queue),
            "active_edges": set(sim_active_edges),
            "log": f"Memulai BFS dari vertex '{start}'. Memasukkan '{start}' ke Queue.",
            "structure": list(sim_queue)
        })
        
        while sim_queue:
            curr = sim_queue.popleft()
            sim_visited.append(curr)
            
            if curr in sim_parent:
                sim_active_edges.add((sim_parent[curr], curr))
                
            steps.append({
                "current_node": curr,
                "visited": list(sim_visited),
                "queued": list(sim_queue),
                "active_edges": set(sim_active_edges),
                "log": f"De-queue '{curr}' dari Queue. Kunjungi '{curr}' (node berubah hijau).",
                "structure": [curr] + list(sim_queue)
            })
            
            neighbors = sorted(self.graph.adj[curr])
            added = []
            for neighbor in neighbors:
                if neighbor not in sim_seen:
                    sim_seen.add(neighbor)
                    sim_queue.append(neighbor)
                    sim_parent[neighbor] = curr
                    added.append(neighbor)
            
            if added:
                steps.append({
                    "current_node": curr,
                    "visited": list(sim_visited),
                    "queued": list(sim_queue),
                    "active_edges": set(sim_active_edges),
                    "log": f"En-queue tetangga '{', '.join(added)}' ke Queue (node berubah kuning).",
                    "structure": list(sim_queue)
                })
                
        steps.append({
            "current_node": None,
            "visited": list(sim_visited),
            "queued": [],
            "active_edges": set(sim_active_edges),
            "log": "BFS Selesai! Semua vertex yang terjangkau telah dikunjungi.",
            "structure": []
        })
        return steps

    # ── Particle Animation Micro-simulation ───────────────────

    def animate_particle(self, start_node, end_node, callback):
        """Menganimasikan sebuah partikel neon yang mengalir di sepanjang edge."""
        if start_node not in self.node_coords or end_node not in self.node_coords:
            callback()
            return
            
        x1, y1 = self.node_coords[start_node]
        x2, y2 = self.node_coords[end_node]
        
        frames = 10
        delay = 20 # ms per frame (total 200ms)
        
        def animate_frame(i):
            if i > frames:
                self.canvas.delete("particle")
                callback()
                return
            
            # Hitung posisi interpolasi linear
            t = i / frames
            px = x1 + t * (x2 - x1)
            py = y1 + t * (y2 - y1)
            
            self.canvas.delete("particle")
            # Gambar titik bersinar neon
            self.canvas.create_oval(px - 6, py - 6, px + 6, py + 6, fill="#89dceb", outline="#f38ba8", width=1, tags="particle")
            self.root.after(delay, lambda: animate_frame(i + 1))
            
        animate_frame(0)

    # ── Animation Controls ────────────────────────────────────

    def play_next_step(self):
        """Mengeksekusi dan menggambarkan langkah animasi berikutnya."""
        if not self.animation_running or self.animation_paused:
            return
            
        if self.current_step_idx >= len(self.animation_steps):
            self.animation_running = False
            self.update_controls_state()
            return
            
        step_data = self.animation_steps[self.current_step_idx]
        curr = step_data["current_node"]
        parent = None
        
        if curr and self.current_step_idx > 0:
            for u, v in step_data["active_edges"]:
                if v == curr:
                    parent = u
                    break
                    
        def complete():
            self.draw_graph()
            self.log_text.config(state="normal")
            self.log_text.insert(tk.END, f"\n• {step_data['log']}")
            self.log_text.see(tk.END)
            self.log_text.config(state="disabled")
            
            struct_name = "Stack" if self.current_algo == "DFS" else "Queue"
            self.struct_label.config(text=f"Isi {struct_name}:")
            content_str = " -> ".join(step_data["structure"]) if step_data["structure"] else "(kosong)"
            self.struct_content.config(text=content_str)
            
            self.current_step_idx += 1
            
            if self.animation_running and not self.animation_paused:
                self.animation_timer = self.root.after(self.animation_speed, self.play_next_step)
            
        if parent and curr:
            self.animate_particle(parent, curr, complete)
        else:
            complete()

    def play_single_step(self):
        """Mengeksekusi tepat satu langkah saja (Step Forward)."""
        if self.current_step_idx >= len(self.animation_steps):
            self.animation_running = False
            self.update_controls_state()
            return
            
        step_data = self.animation_steps[self.current_step_idx]
        curr = step_data["current_node"]
        parent = None
        
        if curr and self.current_step_idx > 0:
            for u, v in step_data["active_edges"]:
                if v == curr:
                    parent = u
                    break
                    
        def complete():
            self.draw_graph()
            self.log_text.config(state="normal")
            self.log_text.insert(tk.END, f"\n• {step_data['log']}")
            self.log_text.see(tk.END)
            self.log_text.config(state="disabled")
            
            struct_name = "Stack" if self.current_algo == "DFS" else "Queue"
            self.struct_label.config(text=f"Isi {struct_name}:")
            content_str = " -> ".join(step_data["structure"]) if step_data["structure"] else "(kosong)"
            self.struct_content.config(text=content_str)
            
            self.current_step_idx += 1
            self.update_controls_state()
            
        if parent and curr:
            self.animate_particle(parent, curr, complete)
        else:
            complete()

    def start_dfs(self):
        start = self.cb_start.get()
        if not start:
            messagebox.showerror("Error", "Graf kosong atau node awal tidak terpilih.")
            return
        self.reset_animation()
        self.current_algo = "DFS"
        self.animation_steps = self.record_dfs_steps(start)
        self.animation_running = True
        self.animation_paused = False
        
        self.log_text.config(state="normal")
        self.log_text.delete("1.0", tk.END)
        self.log_text.insert(tk.END, "=== MEMULAI TRAVERSAL DFS ===\n")
        self.log_text.config(state="disabled")
        
        self.update_controls_state()
        self.play_next_step()

    def start_bfs(self):
        start = self.cb_start.get()
        if not start:
            messagebox.showerror("Error", "Graf kosong atau node awal tidak terpilih.")
            return
        self.reset_animation()
        self.current_algo = "BFS"
        self.animation_steps = self.record_bfs_steps(start)
        self.animation_running = True
        self.animation_paused = False
        
        self.log_text.config(state="normal")
        self.log_text.delete("1.0", tk.END)
        self.log_text.insert(tk.END, "=== MEMULAI TRAVERSAL BFS ===\n")
        self.log_text.config(state="disabled")
        
        self.update_controls_state()
        self.play_next_step()

    def toggle_pause(self):
        if not self.animation_running:
            return
        self.animation_paused = not self.animation_paused
        if self.animation_paused:
            self.btn_play.config(text="Resume", bg="#a6e3a1")
            if self.animation_timer:
                self.root.after_cancel(self.animation_timer)
                self.animation_timer = None
        else:
            self.btn_play.config(text="Pause", bg="#cba6f7")
            self.play_next_step()
        self.update_controls_state()

    def step_forward(self):
        if self.animation_running and self.animation_paused:
            self.play_single_step()

    def reset_animation(self):
        """Mengembalikan visualisasi ke bentuk graf awal."""
        self.animation_running = False
        self.animation_paused = False
        if self.animation_timer:
            self.root.after_cancel(self.animation_timer)
            self.animation_timer = None
            
        self.canvas.delete("particle")
        self.animation_steps = []
        self.current_step_idx = 0
        self.current_algo = ""
        
        self.log_text.config(state="normal")
        self.log_text.delete("1.0", tk.END)
        self.log_text.insert(tk.END, "Animasi di-reset. Graf kembali ke keadaan semula.")
        self.log_text.config(state="disabled")
        
        self.struct_content.config(text="(kosong)")
        self.struct_label.config(text="Isi Struktur Data:")
        
        self.update_controls_state()
        self.draw_graph()

    def on_speed_change(self, val):
        self.animation_speed = int(val)

    def update_controls_state(self):
        """Menyesuaikan keaktifan tombol kontrol berdasarkan status berjalan."""
        if self.animation_running:
            self.btn_dfs.config(state="disabled")
            self.btn_bfs.config(state="disabled")
            self.cb_start.config(state="disabled")
            self.btn_play.config(state="normal")
            if self.animation_paused:
                self.btn_step.config(state="normal")
                self.btn_play.config(text="Resume", bg="#a6e3a1")
            else:
                self.btn_step.config(state="disabled")
                self.btn_play.config(text="Pause", bg="#cba6f7")
        else:
            self.btn_dfs.config(state="normal")
            self.btn_bfs.config(state="normal")
            self.cb_start.config(state="readonly")
            self.btn_play.config(state="disabled", text="Pause", bg="#cba6f7")
            self.btn_step.config(state="disabled")

    # ── Helpers Pewarnaan & State ─────────────────────────────

    def get_node_color_at_step(self, v):
        if not self.animation_running or len(self.animation_steps) == 0:
            return self.node_default
        idx = min(self.current_step_idx, len(self.animation_steps) - 1)
        step_data = self.animation_steps[idx]
        if v == step_data["current_node"]:
            return self.node_active
        elif v in step_data["visited"]:
            return self.node_visited
        elif v in step_data["queued"]:
            return self.node_queued
        return self.node_default

    def is_node_active_at_step(self, v):
        if not self.animation_running or len(self.animation_steps) == 0:
            return False
        idx = min(self.current_step_idx, len(self.animation_steps) - 1)
        return v == self.animation_steps[idx]["current_node"]

    def get_active_edges_at_step(self):
        if not self.animation_running or len(self.animation_steps) == 0:
            return set()
        idx = min(self.current_step_idx, len(self.animation_steps) - 1)
        return self.animation_steps[idx]["active_edges"]

    # ── CRUD & Mode Toggles via GUI ───────────────────────────

    def toggle_mode(self):
        self.graph.directed = not self.graph.directed
        mode_text = "Mode: DIRECTED" if self.graph.directed else "Mode: UNDIRECTED"
        self.btn_mode.config(text=mode_text, bg="#f5c2e7" if self.graph.directed else "#313244", fg="#11111b" if self.graph.directed else self.text_color)
        self.update_stats()
        self.update_view_tab()
        self.draw_graph()

    def add_vertex_gui(self):
        v = self.ent_add_v.get().strip().upper()
        if not v:
            messagebox.showerror("Error", "Nama vertex tidak boleh kosong!")
            return
        if v in self.graph.get_vertices():
            messagebox.showerror("Error", f"Vertex '{v}' sudah ada!")
            return
        self.graph.add_vertex(v)
        # Tentukan posisi acak untuk vertex baru di dekat tengah canvas
        self.node_coords[v] = (random.randint(150, 550), random.randint(150, 450))
        self.ent_add_v.delete(0, tk.END)
        self.update_stats()
        self.refresh_dropdowns()
        self.update_view_tab()
        self.draw_graph()

    def del_vertex_gui(self):
        v = self.cb_del_v.get()
        if not v:
            messagebox.showerror("Error", "Silakan pilih vertex yang ingin dihapus!")
            return
        if self.graph.remove_vertex(v):
            if v in self.node_coords:
                del self.node_coords[v]
            self.update_stats()
            self.refresh_dropdowns()
            self.update_view_tab()
            self.draw_graph()
        else:
            messagebox.showerror("Error", f"Gagal menghapus vertex '{v}'!")

    def add_edge_gui(self):
        u = self.cb_edge_u1.get()
        v = self.cb_edge_v1.get()
        if not u or not v:
            messagebox.showerror("Error", "Silakan pilih kedua vertex!")
            return
        if u == v:
            messagebox.showerror("Error", "Tidak bisa membuat self-loop (edge ke diri sendiri)!")
            return
        if self.graph.has_edge(u, v):
            messagebox.showerror("Error", f"Edge '{u}' - '{v}' sudah ada!")
            return
        self.graph.add_edge(u, v)
        self.update_stats()
        self.refresh_dropdowns()
        self.update_view_tab()
        self.draw_graph()

    def del_edge_gui(self):
        u = self.cb_edge_u2.get()
        v = self.cb_edge_v2.get()
        if not u or not v:
            messagebox.showerror("Error", "Silakan pilih kedua vertex!")
            return
        if self.graph.remove_edge(u, v):
            self.update_stats()
            self.refresh_dropdowns()
            self.update_view_tab()
            self.draw_graph()
        else:
            messagebox.showerror("Error", f"Edge '{u}' - '{v}' tidak ditemukan!")

    def update_stats(self):
        v_count = self.graph.vertex_count()
        e_count = self.graph.edge_count()
        mode_str = "DIRECTED" if self.graph.directed else "UNDIRECTED"
        self.lbl_stats.config(text=f"Mode: {mode_str}  |  Verteks: {v_count}  |  Edge: {e_count}")

    def refresh_dropdowns(self):
        vertices = sorted(self.graph.get_vertices())
        prev_del = self.cb_del_v.get()
        prev_u1 = self.cb_edge_u1.get()
        prev_v1 = self.cb_edge_v1.get()
        prev_u2 = self.cb_edge_u2.get()
        prev_v2 = self.cb_edge_v2.get()
        prev_start = self.cb_start.get()
        
        for cb in [self.cb_del_v, self.cb_edge_u1, self.cb_edge_v1, self.cb_edge_u2, self.cb_edge_v2, self.cb_start]:
            cb.config(values=vertices)
            
        if prev_del in vertices: self.cb_del_v.set(prev_del)
        else: self.cb_del_v.set("")
            
        if prev_u1 in vertices: self.cb_edge_u1.set(prev_u1)
        else: self.cb_edge_u1.set("")
            
        if prev_v1 in vertices: self.cb_edge_v1.set(prev_v1)
        else: self.cb_edge_v1.set("")
            
        if prev_u2 in vertices: self.cb_edge_u2.set(prev_u2)
        else: self.cb_edge_u2.set("")
            
        if prev_v2 in vertices: self.cb_edge_v2.set(prev_v2)
        else: self.cb_edge_v2.set("")
            
        if prev_start in vertices: self.cb_start.set(prev_start)
        elif vertices: self.cb_start.set(vertices[0])
        else: self.cb_start.set("")

    def update_view_tab(self):
        """Memperbarui teks Adjacency List dan Adjacency Matrix di Tab View."""
        # Adjacency List
        self.txt_adj_list.config(state="normal")
        self.txt_adj_list.delete("1.0", tk.END)
        
        vertices = self.graph.get_vertices()
        if not vertices:
            self.txt_adj_list.insert(tk.END, "(graf kosong)")
        else:
            kind = "DIRECTED" if self.graph.directed else "UNDIRECTED"
            self.txt_adj_list.insert(tk.END, f"=== ADJACENCY LIST ({kind}) ===\n\n")
            for v in sorted(vertices):
                neighbors = sorted(self.graph.adj[v])
                arrow = "→" if self.graph.directed else "─"
                if neighbors:
                    chain = f" {arrow} ".join(neighbors)
                    self.txt_adj_list.insert(tk.END, f"  {v} : {chain}\n")
                else:
                    self.txt_adj_list.insert(tk.END, f"  {v} : (tidak ada tetangga)\n")
        self.txt_adj_list.config(state="disabled")
        
        # Adjacency Matrix
        self.txt_adj_matrix.config(state="normal")
        self.txt_adj_matrix.delete("1.0", tk.END)
        
        if not vertices:
            self.txt_adj_matrix.insert(tk.END, "(graf kosong)")
        else:
            self.txt_adj_matrix.insert(tk.END, "=== ADJACENCY MATRIX ===\n\n")
            sorted_v = sorted(vertices)
            n = len(sorted_v)
            
            # Header
            header = "    " + " ".join(f"{v:>3}" for v in sorted_v)
            self.txt_adj_matrix.insert(tk.END, header + "\n")
            self.txt_adj_matrix.insert(tk.END, "   " + "─" * (4 * n + 1) + "\n")
            
            for u in sorted_v:
                row = f" {u} │"
                for v in sorted_v:
                    if u == v:
                        row += f"   -"
                    elif self.graph.has_edge(u, v):
                        row += f"   1"
                    else:
                        row += f"   0"
                self.txt_adj_matrix.insert(tk.END, row + "\n")
        self.txt_adj_matrix.config(state="disabled")

    # ── Canvas Mouse Interaction Handlers ──────────────────────

    def on_canvas_click(self, event):
        """Menangani klik pada canvas: Drag-and-drop node atau klik kosong untuk buat node."""
        clicked_node = None
        for v, (x, y) in self.node_coords.items():
            dist = math.sqrt((event.x - x)**2 + (event.y - y)**2)
            if dist <= self.node_radius:
                clicked_node = v
                break
                
        if clicked_node:
            self.dragged_node = clicked_node
        else:
            # Klik di area kosong: buat vertex baru via popup dialog
            v_name = simpledialog.askstring("Tambah Verteks", "Nama Verteks Baru (1-3 karakter):", parent=self.root)
            if v_name:
                v_name = v_name.strip().upper()
                if not v_name:
                    return
                if v_name in self.graph.get_vertices():
                    messagebox.showerror("Error", f"Vertex '{v_name}' sudah ada!")
                    return
                self.graph.add_vertex(v_name)
                self.node_coords[v_name] = (event.x, event.y)
                self.update_stats()
                self.refresh_dropdowns()
                self.update_view_tab()
                self.draw_graph()

    def on_canvas_drag(self, event):
        """Menangani penyeretan vertex di canvas."""
        if self.dragged_node:
            x = max(25, min(event.x, self.canvas.winfo_width() - 25))
            y = max(25, min(event.y, self.canvas.winfo_height() - 25))
            self.node_coords[self.dragged_node] = (x, y)
            self.draw_graph()

    def on_canvas_release(self, event):
        """Menangani pelepasan tombol klik setelah drag."""
        self.dragged_node = None

    def on_close(self):
        self.reset_animation()
        self.root.destroy()

    def run(self):
        self.root.mainloop()


def main():
    """Fungsi utama — Membuka visualisasi GUI langsung."""
    graph = Graph(directed=False)

    # Pre-load contoh data awal
    for v in ["A", "B", "C", "D", "E"]:
        graph.add_vertex(v)
    for u, v in [("A", "B"), ("A", "C"), ("B", "D"), ("C", "D"), ("D", "E")]:
        graph.add_edge(u, v)

    if not TK_AVAILABLE:
        print("Error: Tkinter tidak tersedia di sistem Python Anda!")
        return

    visualizer = GraphVisualizer(graph)
    visualizer.run()


if __name__ == "__main__":
    main()
