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
#  Menu & Program Utama
# ─────────────────────────────────────────────────────────────

def clear_screen():
    """Bersihkan layar terminal."""
    os.system('cls' if os.name == 'nt' else 'clear')


def display_header():
    """Tampilkan header program."""
    print()
    print(c("╔══════════════════════════════════════════════════════════════╗", CYAN))
    print(c("║         PROGRAM MANAJEMEN GRAF — STRUKTUR DATA               ║", CYAN))
    print(c("║       Representasi: Adjacency List  |  Python 3              ║", CYAN))
    print(c("╚══════════════════════════════════════════════════════════════╝", CYAN))


def display_menu(graph):
    """Tampilkan menu utama."""
    kind = "DIRECTED" if graph.directed else "UNDIRECTED"
    print(c("┌──────────────────────────────────────────────────────────────┐", CYAN))
    print(f"│  Graf: {c(kind, YELLOW)}  |  Vertex: {c(str(graph.vertex_count()), GREEN)}  "
          f"|  Edge: {c(str(graph.edge_count()), GREEN)}")
    print(c("├──────────────────────────────────────────────────────────────┤", CYAN))
    print(c("│  MENU UTAMA                                                  │", BOLD))
    print(c("├──────────────────────────────────────────────────────────────┤", CYAN))
    options = [
        ("1", "Tambah Vertex"),
        ("2", "Hapus Vertex"),
        ("3", "Tambah Edge"),
        ("4", "Hapus Edge"),
        ("5", "Tampilkan Graph (List / Matrix / ASCII)"),
        ("6", "Traversal DFS"),
        ("7", "Traversal BFS"),
        ("8", "Toggle Directed / Undirected"),
        ("0", "Quit"),
    ]
    for key, label in options:
        print(f"  {c(key, YELLOW + BOLD)}.  {label}")
    print(c("└──────────────────────────────────────────────────────────────┘", CYAN))


def prompt(text):
    """Input prompt dengan warna."""
    return input(c(f"  ► {text}: ", YELLOW)).strip()


def success(msg):
    print(c(f"  ✓ {msg}", GREEN))


def error(msg):
    print(c(f"  ✗ {msg}", RED))


def info(msg):
    print(c(f"  ℹ {msg}", CYAN))


def wait():
    input(c("\n  [Tekan Enter untuk kembali ke menu...] ", DIM))


def main():
    """Fungsi utama — loop program interaktif."""
    graph = Graph(directed=False)

    # Pre-load contoh data
    for v in ["A", "B", "C", "D", "E"]:
        graph.add_vertex(v)
    for u, v in [("A", "B"), ("A", "C"), ("B", "D"), ("C", "D"), ("D", "E")]:
        graph.add_edge(u, v)

    while True:
        clear_screen()
        display_header()
        display_menu(graph)

        choice = prompt("Pilih menu (0–8)")

        # ── 1. Tambah Vertex ──────────────────────────────────
        if choice == "1":
            print()
            name = prompt("Nama vertex baru")
            if not name:
                error("Nama vertex tidak boleh kosong!")
            elif graph.add_vertex(name):
                success(f"Vertex '{name.upper()}' berhasil ditambahkan!")
                info(f"Total vertex sekarang: {graph.vertex_count()}")
            else:
                error(f"Vertex '{name.upper()}' sudah ada dalam graf!")
            wait()

        # ── 2. Hapus Vertex ──────────────────────────────────
        elif choice == "2":
            print()
            verts = graph.get_vertices()
            if not verts:
                error("Graf kosong, tidak ada vertex yang bisa dihapus.")
            else:
                info(f"Vertex yang ada: {', '.join(verts)}")
                name = prompt("Nama vertex yang akan dihapus")
                if graph.remove_vertex(name):
                    success(f"Vertex '{name.upper()}' beserta semua edge-nya berhasil dihapus!")
                    info(f"Total vertex sekarang: {graph.vertex_count()}")
                else:
                    error(f"Vertex '{name.upper()}' tidak ditemukan dalam graf!")
            wait()

        # ── 3. Tambah Edge ────────────────────────────────────
        elif choice == "3":
            print()
            verts = graph.get_vertices()
            if len(verts) < 2:
                error("Graf butuh minimal 2 vertex untuk membuat edge.")
            else:
                info(f"Vertex yang ada: {', '.join(verts)}")
                u = prompt("Vertex asal")
                v = prompt("Vertex tujuan")
                if graph.add_edge(u, v):
                    arrow = "→" if graph.directed else "—"
                    success(f"Edge '{u.upper()}' {arrow} '{v.upper()}' berhasil ditambahkan!")
                    info(f"Total edge sekarang: {graph.edge_count()}")
                else:
                    u_up = str(u).strip().upper()
                    v_up = str(v).strip().upper()
                    if u_up not in graph.adj or v_up not in graph.adj:
                        error(f"Salah satu vertex tidak ditemukan! Pastikan vertex '{u_up}' dan '{v_up}' ada.")
                    elif u_up == v_up:
                        error("Self-loop tidak diizinkan (vertex asal = vertex tujuan)!")
                    else:
                        error(f"Edge '{u_up}' — '{v_up}' sudah ada dalam graf!")
            wait()

        # ── 4. Hapus Edge ─────────────────────────────────────
        elif choice == "4":
            print()
            if graph.edge_count() == 0:
                error("Tidak ada edge dalam graf.")
            else:
                verts = graph.get_vertices()
                info(f"Vertex yang ada: {', '.join(verts)}")
                u = prompt("Vertex asal edge")
                v = prompt("Vertex tujuan edge")
                if graph.remove_edge(u, v):
                    success(f"Edge '{str(u).upper()}' — '{str(v).upper()}' berhasil dihapus!")
                    info(f"Total edge sekarang: {graph.edge_count()}")
                else:
                    error(f"Edge '{str(u).upper()}' — '{str(v).upper()}' tidak ditemukan!")
            wait()

        # ── 5. Tampilkan Graph ────────────────────────────────
        elif choice == "5":
            print()
            print(c("  Pilih tampilan:", BOLD))
            print(f"  {c('a', YELLOW)}. Adjacency List")
            print(f"  {c('b', YELLOW)}. Adjacency Matrix")
            print(f"  {c('c', YELLOW)}. ASCII Visual")
            print(f"  {c('d', YELLOW)}. Semua Tampilan")
            sub = prompt("Pilih (a/b/c/d)")

            if sub == "a":
                graph.display_adjacency_list()
            elif sub == "b":
                graph.display_adjacency_matrix()
            elif sub == "c":
                graph.display_ascii_graph()
            elif sub == "d":
                graph.display_adjacency_list()
                graph.display_adjacency_matrix()
                graph.display_ascii_graph()
            else:
                error("Pilihan tidak valid!")
            wait()

        # ── 6. DFS ────────────────────────────────────────────
        elif choice == "6":
            print()
            verts = graph.get_vertices()
            if not verts:
                error("Graf kosong!")
            else:
                info(f"Vertex yang ada: {', '.join(verts)}")
                start = prompt("Vertex awal DFS")
                start_up = str(start).strip().upper()
                result = graph.dfs(start_up)
                if result is None:
                    error(f"Vertex '{start_up}' tidak ditemukan dalam graf!")
                else:
                    graph.display_traversal(result, "DFS (Depth-First Search)", start_up)
                    info(f"DFS menggunakan STACK — mengunjungi sedalam mungkin sebelum backtrack.")
            wait()

        # ── 7. BFS ────────────────────────────────────────────
        elif choice == "7":
            print()
            verts = graph.get_vertices()
            if not verts:
                error("Graf kosong!")
            else:
                info(f"Vertex yang ada: {', '.join(verts)}")
                start = prompt("Vertex awal BFS")
                start_up = str(start).strip().upper()
                result = graph.bfs(start_up)
                if result is None:
                    error(f"Vertex '{start_up}' tidak ditemukan dalam graf!")
                else:
                    graph.display_traversal(result, "BFS (Breadth-First Search)", start_up)
                    info(f"BFS menggunakan QUEUE — mengunjungi level demi level.")
            wait()

        # ── 8. Toggle Directed ────────────────────────────────
        elif choice == "8":
            print()
            graph.directed = not graph.directed
            mode = "DIRECTED (berarah)" if graph.directed else "UNDIRECTED (tidak berarah)"
            success(f"Mode graf diganti ke: {mode}")
            info("Perhatian: Edge yang sudah ada tidak diubah secara otomatis.")
            wait()

        # ── 0. Quit ───────────────────────────────────────────
        elif choice == "0":
            clear_screen()
            print()
            print(c("╔══════════════════════════════════════════════════════════════╗", CYAN))
            print(c("║       Terima kasih telah menggunakan program ini!            ║", CYAN))
            print(c("║              PROGRAM MANAJEMEN GRAF — Python                 ║", CYAN))
            print(c("╚══════════════════════════════════════════════════════════════╝", CYAN))
            print()
            break

        else:
            error("Pilihan tidak valid! Masukkan angka 0–8.")
            wait()


if __name__ == "__main__":
    main()
