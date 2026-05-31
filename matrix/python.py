#!/usr/bin/env python3

import tkinter as tk
from tkinter import ttk, messagebox
import random

class AnimationStep:
    def __init__(self, state, r, c, active, visited, msg):
        self.rows = r
        self.cols = c
        self.state = [row[:] for row in state]
        self.active = list(active)
        self.visited = list(visited)
        self.msg = msg

class MatrixOperations:
    @staticmethod
    def clone_matrix(matrix):
        return [row[:] for row in matrix]

    def sort_row_wise(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        steps.append(AnimationStep(state, r, c, [], [], "Memulai Sortir per Baris."))
        
        for i in range(r):
            for j in range(c - 1):
                for k in range(c - j - 1):
                    active = [(i, k), (i, k+1)]
                    steps.append(AnimationStep(state, r, c, active, [], f"Membandingkan elemen ({i},{k}) dan ({i},{k+1})"))
                    if state[i][k] > state[i][k+1]:
                        state[i][k], state[i][k+1] = state[i][k+1], state[i][k]
                        steps.append(AnimationStep(state, r, c, active, [], "Menukar elemen karena elemen kiri lebih besar."))
            steps.append(AnimationStep(state, r, c, [], [], f"Baris ke-{i} selesai disortir."))
        steps.append(AnimationStep(state, r, c, [], [], "Sortir per baris selesai."))
        return steps

    def sort_col_wise(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        steps.append(AnimationStep(state, r, c, [], [], "Memulai Sortir per Kolom."))
        
        for j in range(c):
            for i in range(r - 1):
                for k in range(r - i - 1):
                    active = [(k, j), (k+1, j)]
                    steps.append(AnimationStep(state, r, c, active, [], f"Membandingkan elemen ({k},{j}) dan ({k+1},{j})"))
                    if state[k][j] > state[k+1][j]:
                        state[k][j], state[k+1][j] = state[k+1][j], state[k][j]
                        steps.append(AnimationStep(state, r, c, active, [], "Menukar elemen karena elemen atas lebih besar."))
            steps.append(AnimationStep(state, r, c, [], [], f"Kolom ke-{j} selesai disortir."))
        steps.append(AnimationStep(state, r, c, [], [], "Sortir per kolom selesai."))
        return steps

    def rotate_clockwise_1(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        steps.append(AnimationStep(state, r, c, [], [], "Memulai Rotasi Searah Jarum Jam sebanyak 1 elemen."))
        
        row_start, row_end = 0, r - 1
        col_start, col_end = 0, c - 1
        
        while row_start < row_end and col_start < col_end:
            prev = state[row_start + 1][col_start]
            
            for i in range(col_start, col_end + 1):
                active = [(row_start, i)]
                steps.append(AnimationStep(state, r, c, active, [], "Menggeser batas atas cincin ke kanan."))
                curr = state[row_start][i]
                state[row_start][i] = prev
                prev = curr
            row_start += 1
            
            for i in range(row_start, row_end + 1):
                active = [(i, col_end)]
                steps.append(AnimationStep(state, r, c, active, [], "Menggeser batas kanan cincin ke bawah."))
                curr = state[i][col_end]
                state[i][col_end] = prev
                prev = curr
            col_end -= 1
            
            if row_start <= row_end:
                for i in range(col_end, col_start - 1, -1):
                    active = [(row_end, i)]
                    steps.append(AnimationStep(state, r, c, active, [], "Menggeser batas bawah cincin ke kiri."))
                    curr = state[row_end][i]
                    state[row_end][i] = prev
                    prev = curr
                row_end -= 1
                
            if col_start <= col_end:
                for i in range(row_end, row_start - 1, -1):
                    active = [(i, col_start)]
                    steps.append(AnimationStep(state, r, c, active, [], "Menggeser batas kiri cincin ke atas."))
                    curr = state[i][col_start]
                    state[i][col_start] = prev
                    prev = curr
                col_start += 1
                
        steps.append(AnimationStep(state, r, c, [], [], "Rotasi searah jarum jam sebanyak 1 elemen selesai."))
        return steps

    def rotate_counter_clockwise_1(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        steps.append(AnimationStep(state, r, c, [], [], "Memulai Rotasi Berlawanan Arah Jarum Jam sebanyak 1 elemen."))
        
        row_start, row_end = 0, r - 1
        col_start, col_end = 0, c - 1
        
        while row_start < row_end and col_start < col_end:
            prev = state[row_start][col_start + 1]
            
            for i in range(row_start, row_end + 1):
                active = [(i, col_start)]
                steps.append(AnimationStep(state, r, c, active, [], "Menggeser batas kiri cincin ke bawah."))
                curr = state[i][col_start]
                state[i][col_start] = prev
                prev = curr
            col_start += 1
            
            for i in range(col_start, col_end + 1):
                active = [(row_end, i)]
                steps.append(AnimationStep(state, r, c, active, [], "Menggeser batas bawah cincin ke kanan."))
                curr = state[row_end][i]
                state[row_end][i] = prev
                prev = curr
            row_end -= 1
            
            if col_start <= col_end:
                for i in range(row_end, row_start - 1, -1):
                    active = [(i, col_end)]
                    steps.append(AnimationStep(state, r, c, active, [], "Menggeser batas kanan cincin ke atas."))
                    curr = state[i][col_end]
                    state[i][col_end] = prev
                    prev = curr
                col_end -= 1
                
            if row_start <= row_end:
                for i in range(col_end, col_start - 1, -1):
                    active = [(row_start, i)]
                    steps.append(AnimationStep(state, r, c, active, [], "Menggeser batas atas cincin ke kiri."))
                    curr = state[row_start][i]
                    state[row_start][i] = prev
                    prev = curr
                row_start += 1
                
        steps.append(AnimationStep(state, r, c, [], [], "Rotasi berlawanan arah jarum jam sebanyak 1 elemen selesai."))
        return steps

    def rotate_90(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        steps.append(AnimationStep(state, r, c, [], [], "Memulai Rotasi 90 Derajat (Searah Jarum Jam). Pertama, Transpose matriks."))
        
        new_state = [[0]*r for _ in range(c)]
        for i in range(r):
            for j in range(c):
                new_state[j][i] = state[i][j]
                active = [(j, i)]
                steps.append(AnimationStep(new_state, c, r, active, [], f"Memindahkan elemen ({i},{j}) ke ({j},{i})"))
                
        steps.append(AnimationStep(new_state, c, r, [], [], "Transpose selesai. Selanjutnya, balik urutan setiap baris."))
        
        for i in range(c):
            left, right = 0, r - 1
            while left < right:
                active = [(i, left), (i, right)]
                steps.append(AnimationStep(new_state, c, r, active, [], f"Menukar elemen kiri dan kanan pada baris {i}"))
                new_state[i][left], new_state[i][right] = new_state[i][right], new_state[i][left]
                left += 1
                right -= 1
                
        steps.append(AnimationStep(new_state, c, r, [], [], "Rotasi 90 derajat selesai."))
        return steps

    def rotate_180(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        steps.append(AnimationStep(state, r, c, [], [], "Memulai Rotasi 180 Derajat. Pertama, balik urutan baris."))
        
        for i in range(r // 2):
            target_row = r - 1 - i
            for j in range(c):
                active = [(i, j), (target_row, j)]
                steps.append(AnimationStep(state, r, c, active, [], f"Menukar elemen baris {i} dan baris {target_row}"))
                state[i][j], state[target_row][j] = state[target_row][j], state[i][j]
                
        steps.append(AnimationStep(state, r, c, [], [], "Pembalikan baris selesai. Selanjutnya, balik elemen di setiap baris."))
        
        for i in range(r):
            for j in range(c // 2):
                target_col = c - 1 - j
                active = [(i, j), (i, target_col)]
                steps.append(AnimationStep(state, r, c, active, [], f"Menukar elemen kiri dan kanan pada baris {i}"))
                state[i][j], state[i][target_col] = state[i][target_col], state[i][j]
                
        steps.append(AnimationStep(state, r, c, [], [], "Rotasi 180 derajat selesai."))
        return steps

    def traverse_row_wise(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        visited = []
        steps.append(AnimationStep(state, r, c, [], visited, "Memulai Traversal per Baris."))
        
        for i in range(r):
            for j in range(c):
                p = (i, j)
                visited.append(p)
                steps.append(AnimationStep(state, r, c, [p], list(visited), f"Mengunjungi elemen di baris {i}, kolom {j}"))
                
        steps.append(AnimationStep(state, r, c, [], visited, "Traversal per baris selesai."))
        return steps

    def traverse_col_wise(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        visited = []
        steps.append(AnimationStep(state, r, c, [], visited, "Memulai Traversal per Kolom."))
        
        for j in range(c):
            for i in range(r):
                p = (i, j)
                visited.append(p)
                steps.append(AnimationStep(state, r, c, [p], list(visited), f"Mengunjungi elemen di kolom {j}, baris {i}"))
                
        steps.append(AnimationStep(state, r, c, [], visited, "Traversal per kolom selesai."))
        return steps

    def traverse_spiral(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        visited = []
        steps.append(AnimationStep(state, r, c, [], visited, "Memulai Traversal Spiral."))
        
        row_start, row_end = 0, r - 1
        col_start, col_end = 0, c - 1
        
        while row_start <= row_end and col_start <= col_end:
            for i in range(col_start, col_end + 1):
                p = (row_start, i)
                visited.append(p)
                steps.append(AnimationStep(state, r, c, [p], list(visited), "Maju ke kanan sepanjang batas atas."))
            row_start += 1
            
            for i in range(row_start, row_end + 1):
                p = (i, col_end)
                visited.append(p)
                steps.append(AnimationStep(state, r, c, [p], list(visited), "Turun ke bawah sepanjang batas kanan."))
            col_end -= 1
            
            if row_start <= row_end:
                for i in range(col_end, col_start - 1, -1):
                    p = (row_end, i)
                    visited.append(p)
                    steps.append(AnimationStep(state, r, c, [p], list(visited), "Mundur ke kiri sepanjang batas bawah."))
                row_end -= 1
                
            if col_start <= col_end:
                for i in range(row_end, row_start - 1, -1):
                    p = (i, col_start)
                    visited.append(p)
                    steps.append(AnimationStep(state, r, c, [p], list(visited), "Naik ke atas sepanjang batas kiri."))
                col_start += 1
                
        steps.append(AnimationStep(state, r, c, [], visited, "Traversal spiral selesai."))
        return steps

    def transpose(self, matrix, r, c):
        steps = []
        state = self.clone_matrix(matrix)
        steps.append(AnimationStep(state, r, c, [], [], "Memulai Transpose Matriks."))
        
        new_state = [[0]*r for _ in range(c)]
        for i in range(r):
            for j in range(c):
                new_state[j][i] = state[i][j]
                active = [(j, i)]
                steps.append(AnimationStep(new_state, c, r, active, [], f"Memindahkan elemen ({i},{j}) ke ({j},{i})"))
                
        steps.append(AnimationStep(new_state, c, r, [], [], "Transpose matriks selesai. Baris menjadi kolom dan kolom menjadi baris."))
        return steps

class MatrixVisualizer:
    def __init__(self, root):
        self.root = root
        self.root.title("Visualisasi Operasi Matriks (Python Tkinter)")
        self.root.geometry("1000x650")
        self.root.minsize(800, 500)
        
        # Colors (Catppuccin Mocha Palette)
        self.bg_color = "#1E1E2E"
        self.panel_bg = "#181825"
        self.cell_default = "#313244"
        self.cell_active = "#F38BA8"
        self.cell_visited = "#A6E3A1"
        self.text_color = "#CDD6F4"
        self.cell_border = "#45475A"
        
        self.root.configure(bg=self.bg_color)
        
        self.ops = MatrixOperations()
        self.current_matrix = []
        self.current_rows = 4
        self.current_cols = 4
        
        self.original_matrix = None
        self.orig_rows = 0
        self.orig_cols = 0
        
        self.active_cells = []
        self.visited_cells = []
        
        self.current_steps = []
        self.step_idx = 0
        self.animation_delay = 800
        self.is_paused = False
        self.animation_id = None
        
        self.setup_ui()
        self.generate_random_matrix()

    def setup_ui(self):
        self.main_frame = tk.Frame(self.root, bg=self.bg_color)
        self.main_frame.pack(fill=tk.BOTH, expand=True)
        
        # Canvas
        self.canvas_frame = tk.Frame(self.main_frame, bg=self.bg_color)
        self.canvas_frame.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        
        self.canvas = tk.Canvas(self.canvas_frame, bg=self.bg_color, highlightthickness=0)
        self.canvas.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)
        self.canvas.bind("<Configure>", lambda e: self.draw_matrix())
        
        # Sidebar
        self.sidebar = tk.Frame(self.main_frame, bg=self.panel_bg, width=320, padx=15, pady=15)
        self.sidebar.pack(side=tk.RIGHT, fill=tk.Y)
        self.sidebar.pack_propagate(False)
        
        # 1. Init Matrix
        tk.Label(self.sidebar, text="Inisialisasi Matriks:", bg=self.panel_bg, fg="#F5C2E7", font=("Arial", 11, "bold")).pack(anchor="w", pady=(0,5))
        
        dim_frame = tk.Frame(self.sidebar, bg=self.panel_bg)
        dim_frame.pack(fill="x", pady=(0,10))
        
        tk.Label(dim_frame, text="Baris:", bg=self.panel_bg, fg=self.text_color).pack(side=tk.LEFT)
        self.ent_rows = tk.Entry(dim_frame, bg=self.cell_default, fg=self.text_color, insertbackground=self.text_color, width=5)
        self.ent_rows.insert(0, "4")
        self.ent_rows.pack(side=tk.LEFT, padx=(5,15))
        
        tk.Label(dim_frame, text="Kolom:", bg=self.panel_bg, fg=self.text_color).pack(side=tk.LEFT)
        self.ent_cols = tk.Entry(dim_frame, bg=self.cell_default, fg=self.text_color, insertbackground=self.text_color, width=5)
        self.ent_cols.insert(0, "4")
        self.ent_cols.pack(side=tk.LEFT, padx=(5,0))
        
        btn_gen = tk.Button(self.sidebar, text="Generate Matriks Acak", bg="#89B4FA", fg="#11111B", font=("Arial", 9, "bold"), relief="flat", command=self.on_generate_click, cursor="hand2")
        btn_gen.pack(fill="x", pady=(0,20))
        
        # 2. Algorithm Menu
        tk.Label(self.sidebar, text="Menu Operasi:", bg=self.panel_bg, fg="#F5C2E7", font=("Arial", 11, "bold")).pack(anchor="w", pady=(0,5))
        
        algos = [
            "1-a. Sort matrix row-wise",
            "1-b. Sort matrix column-wise",
            "2-a. Rotate Matrix Clockwise by 1",
            "2-b. Rotate Matrix Counter-Clockwise by 1",
            "2-c. Rotate matrix by 90",
            "2-d. Rotate matrix by 180",
            "3-a. Row-wise traversal",
            "3-b. Column-wise traversal",
            "4. Print matrix in spiral form",
            "5. Transpose matrix",
            "6. Quit"
        ]
        self.cb_algo = ttk.Combobox(self.sidebar, values=algos, state="readonly")
        self.cb_algo.current(0)
        self.cb_algo.pack(fill="x", pady=(0,15))
        
        # 3. Controls
        ctrl_frame = tk.Frame(self.sidebar, bg=self.panel_bg)
        ctrl_frame.pack(fill="x", pady=(0,15))
        ctrl_frame.columnconfigure(0, weight=1)
        ctrl_frame.columnconfigure(1, weight=1)
        
        self.btn_start = tk.Button(ctrl_frame, text="Start", bg="#A6E3A1", fg="#11111B", font=("Arial", 9, "bold"), relief="flat", command=self.start_animation, cursor="hand2")
        self.btn_start.grid(row=0, column=0, padx=(0,4), pady=(0,4), sticky="ew")
        
        self.btn_pause = tk.Button(ctrl_frame, text="Pause", bg="#CBA6F7", fg="#11111B", font=("Arial", 9, "bold"), relief="flat", command=self.toggle_pause, cursor="hand2")
        self.btn_pause.grid(row=0, column=1, padx=(4,0), pady=(0,4), sticky="ew")
        
        self.btn_step = tk.Button(ctrl_frame, text="Step", bg="#F9E2AF", fg="#11111B", font=("Arial", 9, "bold"), relief="flat", command=self.next_step, cursor="hand2")
        self.btn_step.grid(row=1, column=0, padx=(0,4), pady=(4,0), sticky="ew")
        
        self.btn_reset = tk.Button(ctrl_frame, text="Reset", bg="#F38BA8", fg="#11111B", font=("Arial", 9, "bold"), relief="flat", command=lambda: self.stop_animation(True), cursor="hand2")
        self.btn_reset.grid(row=1, column=1, padx=(4,0), pady=(4,0), sticky="ew")
        
        # 4. Speed
        tk.Label(self.sidebar, text="Kecepatan Animasi:", bg=self.panel_bg, fg=self.text_color).pack(anchor="w")
        self.scale_speed = tk.Scale(self.sidebar, from_=100, to=2000, orient="horizontal", bg=self.panel_bg, fg=self.text_color, highlightbackground=self.panel_bg, command=self.on_speed_change)
        self.scale_speed.set(800)
        self.scale_speed.pack(fill="x", pady=(0,15))
        
        # 5. Log
        tk.Label(self.sidebar, text="Log Eksekusi:", bg=self.panel_bg, fg=self.text_color).pack(anchor="w")
        
        log_frame = tk.Frame(self.sidebar, bg=self.panel_bg)
        log_frame.pack(fill="both", expand=True)
        
        self.txt_log = tk.Text(log_frame, bg=self.bg_color, fg=self.text_color, font=("Consolas", 9), wrap="word", highlightthickness=1, highlightbackground=self.cell_border)
        self.txt_log.pack(side=tk.LEFT, fill="both", expand=True)
        
        scroll = tk.Scrollbar(log_frame, command=self.txt_log.yview)
        scroll.pack(side=tk.RIGHT, fill="y")
        self.txt_log.config(yscrollcommand=scroll.set)

    def on_generate_click(self):
        try:
            r = int(self.ent_rows.get())
            c = int(self.ent_cols.get())
            if r <= 0 or c <= 0 or r > 20 or c > 20:
                messagebox.showerror("Error", "Dimensi matriks harus 1-20.")
                return
            self.current_rows = r
            self.current_cols = c
            self.generate_random_matrix()
        except ValueError:
            messagebox.showerror("Error", "Input tidak valid!")

    def generate_random_matrix(self):
        self.stop_animation(False)
        self.current_matrix = [[random.randint(10, 99) for _ in range(self.current_cols)] for _ in range(self.current_rows)]
        self.active_cells = []
        self.visited_cells = []
        self.original_matrix = None
        self.log_message(f"Matriks acak {self.current_rows}x{self.current_cols} berhasil di-generate.")
        self.draw_matrix()

    def log_message(self, msg):
        self.txt_log.insert(tk.END, msg + "\n")
        self.txt_log.see(tk.END)

    def draw_matrix(self):
        self.canvas.delete("all")
        if not self.current_matrix or self.current_rows == 0 or self.current_cols == 0:
            return
            
        w = self.canvas.winfo_width()
        h = self.canvas.winfo_height()
        if w <= 1 or h <= 1:
            w, h = 600, 500
            
        cell_size = min((w - 40) // self.current_cols, (h - 40) // self.current_rows)
        cell_size = min(max(cell_size, 10), 80)
        
        start_x = (w - (self.current_cols * cell_size)) // 2
        start_y = (h - (self.current_rows * cell_size)) // 2
        
        for i in range(self.current_rows):
            for j in range(self.current_cols):
                x1 = start_x + j * cell_size
                y1 = start_y + i * cell_size
                x2 = x1 + cell_size
                y2 = y1 + cell_size
                
                p = (i, j)
                fill_color = self.cell_default
                if p in self.visited_cells:
                    fill_color = self.cell_visited
                if p in self.active_cells:
                    fill_color = self.cell_active
                    
                self.canvas.create_rectangle(x1, y1, x2, y2, fill=fill_color, outline=self.cell_border, width=1)
                
                val = str(self.current_matrix[i][j])
                font_size = max(cell_size // 3, 8)
                self.canvas.create_text((x1 + x2) // 2, (y1 + y2) // 2, text=val, fill=self.text_color, font=("Arial", font_size, "bold"))
                
        # Draw Original Matrix at Top-Left if it exists
        if self.original_matrix is not None and self.orig_rows > 0 and self.orig_cols > 0:
            mini_cell_size = max(10, cell_size // 2)
            mini_cell_size = min(mini_cell_size, 25)
            
            self.canvas.create_text(15, 20, text="Versi Asli:", fill="#F5C2E7", font=("Arial", 10, "bold"), anchor="w")
            
            mini_start_x = 15
            mini_start_y = 30
            
            for i in range(self.orig_rows):
                for j in range(self.orig_cols):
                    x1 = mini_start_x + j * mini_cell_size
                    y1 = mini_start_y + i * mini_cell_size
                    x2 = x1 + mini_cell_size
                    y2 = y1 + mini_cell_size
                    
                    self.canvas.create_rectangle(x1, y1, x2, y2, fill=self.cell_default, outline=self.cell_border, width=1)
                    
                    val = str(self.original_matrix[i][j])
                    mini_font_size = max(mini_cell_size // 2, 6)
                    self.canvas.create_text((x1 + x2) // 2, (y1 + y2) // 2, text=val, fill="#6C7086", font=("Arial", mini_font_size))

    def on_speed_change(self, val):
        self.animation_delay = int(val)

    def start_animation(self):
        idx = self.cb_algo.current()
        if idx == 10: # Quit
            self.root.quit()
            return
            
        self.stop_animation(False)
        
        self.original_matrix = self.ops.clone_matrix(self.current_matrix)
        self.orig_rows = self.current_rows
        self.orig_cols = self.current_cols
        
        if idx == 0: self.current_steps = self.ops.sort_row_wise(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 1: self.current_steps = self.ops.sort_col_wise(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 2: self.current_steps = self.ops.rotate_clockwise_1(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 3: self.current_steps = self.ops.rotate_counter_clockwise_1(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 4: self.current_steps = self.ops.rotate_90(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 5: self.current_steps = self.ops.rotate_180(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 6: self.current_steps = self.ops.traverse_row_wise(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 7: self.current_steps = self.ops.traverse_col_wise(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 8: self.current_steps = self.ops.traverse_spiral(self.current_matrix, self.current_rows, self.current_cols)
        elif idx == 9: self.current_steps = self.ops.transpose(self.current_matrix, self.current_rows, self.current_cols)

        if self.current_steps:
            self.step_idx = 0
            self.is_paused = False
            self.btn_pause.config(text="Pause")
            self.log_message("---- Memulai Animasi ----")
            self.schedule_next_step()

    def schedule_next_step(self):
        if not self.is_paused:
            self.next_step()
            if self.step_idx < len(self.current_steps):
                self.animation_id = self.root.after(self.animation_delay, self.schedule_next_step)

    def toggle_pause(self):
        if not self.is_paused and self.animation_id:
            self.root.after_cancel(self.animation_id)
            self.animation_id = None
            self.is_paused = True
            self.btn_pause.config(text="Resume")
            self.log_message("[Animasi di-pause]")
        elif self.is_paused and self.current_steps and self.step_idx < len(self.current_steps):
            self.is_paused = False
            self.btn_pause.config(text="Pause")
            self.log_message("[Animasi dilanjutkan]")
            self.schedule_next_step()

    def stop_animation(self, restore_initial):
        if self.animation_id:
            self.root.after_cancel(self.animation_id)
            self.animation_id = None
        self.is_paused = False
        self.btn_pause.config(text="Pause")
        self.current_steps = []
        self.step_idx = 0
        
        if restore_initial:
            self.active_cells = []
            self.visited_cells = []
            self.original_matrix = None
            self.draw_matrix()
            self.log_message("Animasi direset.")

    def next_step(self):
        if not self.current_steps:
            return
            
        if self.step_idx < len(self.current_steps):
            step = self.current_steps[self.step_idx]
            self.current_matrix = step.state
            self.current_rows = step.rows
            self.current_cols = step.cols
            self.active_cells = step.active
            self.visited_cells = step.visited
            
            self.log_message(f"Step {self.step_idx + 1}: {step.msg}")
            self.draw_matrix()
            
            if self.step_idx == len(self.current_steps) - 1:
                self.log_message("---- Animasi Selesai ----")
                if self.animation_id:
                    self.root.after_cancel(self.animation_id)
                    self.animation_id = None
            
            self.step_idx += 1

if __name__ == "__main__":
    root = tk.Tk()
    app = MatrixVisualizer(root)
    root.mainloop()
