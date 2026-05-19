# Graph - Manajemen Struktur Data Graf

Implementasi struktur data **Graf (Graph)** dengan **Adjacency List**. Mendukung operasi Tambah/Hapus Vertex, Tambah/Hapus Edge, tampilan dalam tiga format, serta Traversal DFS dan BFS.

---

## Deskripsi Program

Graf adalah struktur data non-linear yang terdiri dari kumpulan **vertex** (simpul) dan **edge** (sisi penghubung). Program ini menyediakan dua implementasi: **terminal interaktif** (Java & Python) dan **web app visual** (`graph.html`).

### Representasi Internal

```
Adjacency List (TreeMap / dict of sorted list)

Vertex A  →  [B, C]
Vertex B  →  [A, D]
Vertex C  →  [A, D]
Vertex D  →  [B, C, E]
Vertex E  →  [D]
```

---

## Jenis Graf

| Jenis | Keterangan |
|-------|-----------|
| **Undirected** | Edge tidak berarah — A—B sama dengan B—A |
| **Directed** | Edge berarah — A→B tidak berarti B→A |

Mode dapat di-toggle (menu 8) tanpa menghapus data.

---

## Kompleksitas Waktu

| Operasi | Kompleksitas |
|---------|-------------|
| Tambah Vertex | O(1) |
| Hapus Vertex | O(V + E) |
| Tambah Edge | O(degree) |
| Hapus Edge | O(degree) |
| DFS | O(V + E) |
| BFS | O(V + E) |
| Tampilkan Matrix | O(V²) |

---

## Fitur Program

| No | Fitur | Deskripsi |
|----|-------|-----------|
| 1 | **Tambah Vertex** | Tambahkan simpul baru ke dalam graf |
| 2 | **Hapus Vertex** | Hapus simpul beserta semua edge yang terhubung |
| 3 | **Tambah Edge** | Hubungkan dua vertex dengan sebuah edge |
| 4 | **Hapus Edge** | Hapus edge antara dua vertex |
| 5 | **Tampilkan Graph** | Lihat sebagai Adjacency List, Matrix, atau ASCII |
| 6 | **Traversal DFS** | Depth-First Search (menggunakan Stack) |
| 7 | **Traversal BFS** | Breadth-First Search (menggunakan Queue) |
| 8 | **Toggle Mode** | Ganti antara Directed dan Undirected |
| 0 | **Quit** | Keluar dari program |

---

## Struktur Kode

### Java (`javaX.java`)

```
package graph;

class Graph
    + addVertex(v)          → Tambah vertex, return bool
    + removeVertex(v)       → Hapus vertex + semua edge-nya
    + addEdge(u, v)         → Tambah edge, return kode status
    + removeEdge(u, v)      → Hapus edge
    + dfs(start)            → DFS iteratif dengan Stack
    + bfs(start)            → BFS iteratif dengan Queue
    + displayAdjacencyList()
    + displayAdjacencyMatrix()
    + displayAsciiGraph()
    + displayTraversal(order, name, start)

class javaX (Main)
    + main()                → Loop menu interaktif
```

### Python (`python.py`)

```
class Graph
    add_vertex(v)           → Tambah vertex
    remove_vertex(v)        → Hapus vertex + edge
    add_edge(u, v)          → Tambah edge
    remove_edge(u, v)       → Hapus edge
    dfs(start)              → DFS iteratif
    bfs(start)              → BFS iteratif
    display_adjacency_list()
    display_adjacency_matrix()
    display_ascii_graph()
    display_traversal(order, name, start)

main()                      → Loop menu interaktif
```

### Web App (`graph.html`)

Aplikasi berbasis browser dengan canvas interaktif:
- Drag vertex untuk mengatur posisi
- Klik tombol untuk tambah/hapus vertex & edge
- Animasi traversal DFS/BFS step-by-step
- Tampilan Adjacency List & Matrix secara real-time

---

## Cara Menjalankan

### Java

```bash
# Dari folder root project (Belajar-struktur-data/)
javac -encoding UTF-8 graph/javaX.java -d bin
java -cp bin graph.javaX
```

### Python

```bash
# Dari folder root project
python graph/python.py
# atau dari dalam folder graph/
cd graph
python python.py
```

### Web App (Visual Interaktif)

```bash
# Cukup buka file di browser
# Double-click graph.html
# atau buka dengan: start graph/graph.html
```

---

## Contoh Output Terminal

```
╔══════════════════════════════════════════════════════════════╗
║         PROGRAM MANAJEMEN GRAF — STRUKTUR DATA               ║
╚══════════════════════════════════════════════════════════════╝

  Vertex: 5   |   Edge: 5   |   Jenis: UNDIRECTED

  ADJACENCY LIST
     A  :  B → C
     B  :  A → D
     C  :  A → D
     D  :  B → C → E
     E  :  D

  TRAVERSAL DFS dari A:
  [1]A → [2]B → [3]D → [4]C → [5]E
```

---

## Bahasa yang Digunakan

- **Java** — OpenJDK 17+
- **Python** — Python 3.9+
- **Web** — HTML5 / CSS3 / JavaScript (Canvas API)
