#!/usr/bin/env python3
"""
Simulasi Antrian Bank dengan GUI
Menggunakan collections.deque sebagai struktur data Queue
"""

import tkinter as tk
from tkinter import ttk, messagebox
from collections import deque
import threading
import platform
import subprocess
import os

# Untuk text-to-speech
try:
    import pyttsx3
    TTS_AVAILABLE = True
except ImportError:
    TTS_AVAILABLE = False
    print("Warning: pyttsx3 tidak tersedia. Install dengan: pip install pyttsx3")


class Customer:
    """Class untuk menyimpan data customer"""
    def __init__(self, number, name):
        self.number = number
        self.name = name
    
    def __str__(self):
        return f"No. {self.number} - {self.name}"


class QueueSimulation:
    """Aplikasi Simulasi Antrian Bank"""
    
    def __init__(self, root):
        self.root = root
        self.root.title("Simulasi Antrian Bank")
        self.root.geometry("800x600")
        self.root.resizable(True, True)
        
        # Set background color untuk root window
        self.root.config(bg='#a5d6a7')
        
        # Inisialisasi queue dan nomor
        self.queue = deque()
        self.current_number = 0
        
        # Inisialisasi TTS
        if TTS_AVAILABLE:
            try:
                self.tts_engine = pyttsx3.init()
                # Set properties
                self.tts_engine.setProperty('rate', 150)  # Kecepatan bicara
                self.tts_engine.setProperty('volume', 1.0)  # Volume
                
                # Set bahasa Indonesia dan install jika perlu
                self.setup_indonesian_voice()
            except:
                self.tts_engine = None
                print("Warning: TTS engine gagal diinisialisasi")
        else:
            self.tts_engine = None
        
        # Setup GUI
        self.setup_gui()
    
    def setup_indonesian_voice(self):
        """Setup dan install voice Indonesia jika diperlukan"""
        try:
            # Untuk Linux, langsung gunakan espeak-ng, skip pyttsx3 
            # karena lebih reliable untuk bahasa Indonesia
            if platform.system() == 'Linux':
                print("🔧 Menggunakan espeak-ng untuk voice Indonesia")
                # Disable pyttsx3, gunakan espeak-ng langsung
                self.tts_engine = None
                return
            
            voices = self.tts_engine.getProperty('voices')
            indonesian_voice_found = False
            
            # Cari voice Indonesia
            for voice in voices:
                if 'indonesia' in voice.name.lower() or 'id' in voice.id.lower():
                    self.tts_engine.setProperty('voice', voice.id)
                    indonesian_voice_found = True
                    print(f"✅ Voice Indonesia ditemukan: {voice.name}")
                    break
            
            # Jika tidak ditemukan
            if not indonesian_voice_found:
                print("⚠️  Voice Indonesia tidak ditemukan di pyttsx3")
                system = platform.system()
                
                if system == 'Windows':
                    print("📥 Untuk Windows, install Indonesian Language Pack dari Settings > Time & Language > Language")
                elif system == 'Darwin':
                    print("📥 Untuk macOS, voice Indonesia mungkin sudah tersedia sebagai 'Damayanti'")
                    # Coba cari Damayanti
                    for voice in voices:
                        if 'damayanti' in voice.name.lower():
                            self.tts_engine.setProperty('voice', voice.id)
                            print(f"✅ Menggunakan voice: {voice.name}")
                            break
        except Exception as e:
            print(f"Error setup voice: {e}")
    
    def install_indonesian_voice_linux(self):
        """Install voice Indonesia untuk Linux"""
        try:
            # Check jika espeak-ng sudah terinstall
            result = subprocess.run(['which', 'espeak-ng'], capture_output=True)
            
            if result.returncode != 0:
                print("📦 Menginstall espeak-ng...")
                print("Jalankan: sudo apt install espeak-ng")
                # Untuk distro berbasis Debian/Ubuntu
                try:
                    subprocess.run(['pkexec', 'apt', 'install', '-y', 'espeak-ng'], check=False)
                except:
                    pass
            
            # Check voice data Indonesia
            print("✅ Untuk voice Indonesia yang lebih baik, install:")
            print("   sudo apt install espeak-ng-data")
            print("   sudo apt install speech-dispatcher-espeak-ng")
            
        except Exception as e:
            print(f"Error installing voice: {e}")
            print("Install manual dengan: sudo apt install espeak-ng espeak-ng-data")
    
    def setup_gui(self):
        """Setup semua komponen GUI"""
        
        # Frame Atas - Ambil Antrian
        top_frame = tk.LabelFrame(
            self.root, 
            text="Ambil Nomor Antrian", 
            bg='#a5d6a7',
            fg='white',
            font=('Arial', 10, 'bold'),
            padx=10,
            pady=10
        )
        top_frame.pack(fill=tk.X, padx=10, pady=5)
        
        tk.Label(top_frame, text="Nama:", bg='#a5d6a7', fg='white', font=('Arial', 10)).pack(side=tk.LEFT, padx=5)
        
        self.name_entry = tk.Entry(top_frame, width=30, font=('Arial', 12))
        self.name_entry.pack(side=tk.LEFT, padx=5)
        self.name_entry.bind('<Return>', lambda e: self.take_number())
        
        self.take_button = tk.Button(
            top_frame, 
            text="Ambil Antrian", 
            command=self.take_number,
            bg='#ffc107',
            fg='#333333',
            font=('Arial', 12, 'bold'),
            padx=20,
            pady=5,
            cursor='hand2'
        )
        self.take_button.pack(side=tk.LEFT, padx=10)
        
        # Frame Tengah - Tampilan Antrian
        center_frame = tk.LabelFrame(
            self.root, 
            text="Data Antrian",
            bg='#a5d6a7',
            fg='white',
            font=('Arial', 10, 'bold'),
            padx=10,
            pady=10
        )
        center_frame.pack(fill=tk.BOTH, expand=True, padx=10, pady=5)
        
        # Configure style untuk Treeview
        style = ttk.Style()
        style.theme_use('clam')
        style.configure('Treeview',
                       background='#fff9c4',
                       foreground='#333333',
                       fieldbackground='#fff9c4',
                       rowheight=25)
        style.configure('Treeview.Heading',
                       background='#ffc107',
                       foreground='#333333',
                       font=('Arial', 10, 'bold'),
                       relief='flat')
        # Disable hover effect pada heading dan set selected row color
        style.map('Treeview.Heading',
                 background=[('active', '#ffc107'), ('pressed', '#ffc107')],
                 foreground=[('active', '#333333'), ('pressed', '#333333')])
        style.map('Treeview',
                 background=[('selected', '#999999')])
        
        # Treeview untuk tampilan tabel
        columns = ('number', 'name')
        self.tree = ttk.Treeview(center_frame, columns=columns, show='headings', height=15)
        
        self.tree.heading('number', text='Nomor Antrian')
        self.tree.heading('name', text='Nama')
        
        self.tree.column('number', width=150, anchor=tk.CENTER)
        self.tree.column('name', width=400, anchor=tk.W)
        
        # Scrollbar
        scrollbar = ttk.Scrollbar(center_frame, orient=tk.VERTICAL, command=self.tree.yview)
        self.tree.configure(yscroll=scrollbar.set)
        
        self.tree.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        
        # Frame Bawah - Kontrol
        bottom_frame = tk.Frame(self.root, bg='#a5d6a7')
        bottom_frame.pack(fill=tk.X, padx=10, pady=5)
        
        # Button Panel
        button_panel = tk.Frame(bottom_frame, bg='#a5d6a7')
        button_panel.pack(pady=10)
        
        self.display_button = tk.Button(
            button_panel,
            text="Tampilkan Antrian",
            command=self.display_queue,
            bg='#ffc107',
            fg='#333333',
            font=('Arial', 12, 'bold'),
            padx=20,
            pady=8,
            cursor='hand2'
        )
        self.display_button.pack(side=tk.LEFT, padx=5)
        
        self.call_button = tk.Button(
            button_panel,
            text="Panggil Antrian",
            command=self.call_queue,
            bg='#ffc107',
            fg='#333333',
            font=('Arial', 12, 'bold'),
            padx=20,
            pady=8,
            cursor='hand2'
        )
        self.call_button.pack(side=tk.LEFT, padx=5)
        
        # Status Label
        self.status_label = tk.Label(
            bottom_frame,
            text="Status: Siap",
            font=('Arial', 14, 'bold'),
            bg='#a5d6a7',
            fg='white',
            pady=10
        )
        self.status_label.pack()
    
    def take_number(self):
        """Fitur 3: Ambil Antrian"""
        name = self.name_entry.get().strip()
        
        if not name:
            messagebox.showerror("Error", "Nama tidak boleh kosong!")
            return
        
        self.current_number += 1
        customer = Customer(self.current_number, name)
        self.queue.append(customer)
        
        # Tampilkan konfirmasi dengan dialog custom
        self.show_success_dialog(customer)
        
        self.name_entry.delete(0, tk.END)
        self.status_label.config(
            text=f"Status: Antrian No.{self.current_number} ditambahkan"
        )
        
        # Refresh table
        self.display_queue()
    
    def show_success_dialog(self, customer):
        """Tampilkan dialog sukses dengan background kuning"""
        dialog = tk.Toplevel(self.root)
        dialog.title("Antrian Berhasil")
        dialog.geometry("400x250")
        dialog.transient(self.root)
        dialog.grab_set()
        
        # Background color hijau teal
        dialog.config(bg='#a5d6a7')
        
        # Content Frame
        content_frame = tk.Frame(dialog, bg='#a5d6a7')
        content_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)
        
        tk.Label(
            content_frame,
            text="✓ ANTRIAN BERHASIL",
            font=('Arial', 18, 'bold'),
            bg='#a5d6a7',
            fg='white'
        ).pack(pady=10)
        
        tk.Label(
            content_frame,
            text=f"Nomor Antrian Anda:",
            font=('Arial', 12),
            bg='#a5d6a7',
            fg='white'
        ).pack(pady=5)
        
        tk.Label(
            content_frame,
            text=customer.number,
            font=('Arial', 48, 'bold'),
            bg='#a5d6a7',
            fg='#ffc107'
        ).pack(pady=5)
        
        tk.Label(
            content_frame,
            text=f"Nama: {customer.name}",
            font=('Arial', 14),
            bg='#a5d6a7',
            fg='white'
        ).pack(pady=5)
        
        tk.Label(
            content_frame,
            text="Silakan menunggu panggilan",
            font=('Arial', 10, 'italic'),
            bg='#a5d6a7',
            fg='white'
        ).pack(pady=10)
        
        # OK Button
        ok_button = tk.Button(
            dialog,
            text="OK",
            command=dialog.destroy,
            font=('Arial', 12, 'bold'),
            bg='#ffc107',
            fg='#333333',
            padx=30,
            pady=8,
            cursor='hand2'
        )
        ok_button.pack(pady=10)
        
        # Center dialog
        dialog.update_idletasks()
        x = (dialog.winfo_screenwidth() // 2) - (dialog.winfo_width() // 2)
        y = (dialog.winfo_screenheight() // 2) - (dialog.winfo_height() // 2)
        dialog.geometry(f"+{x}+{y}")
        
        # Wait for dialog to close
        dialog.wait_window()
    
    def display_queue(self):
        """Fitur 4: Tampilkan Data Antrian"""
        # Clear tree
        for item in self.tree.get_children():
            self.tree.delete(item)
        
        if not self.queue:
            self.status_label.config(text="Status: Antrian kosong")
            return
        
        # Tampilkan semua data di treeview
        for customer in self.queue:
            self.tree.insert('', tk.END, values=(customer.number, customer.name))
        
        self.status_label.config(
            text=f"Status: Menampilkan {len(self.queue)} antrian"
        )
    
    def call_queue(self):
        """Fitur 5: Panggil Antrian"""
        if not self.queue:
            messagebox.showinfo("Informasi", "Antrian kosong!")
            self.status_label.config(text="Status: Antrian kosong")
            return
        
        customer = self.queue.popleft()
        
        # Tampilkan dialog dulu, audio akan diputar bersamaan
        announcement = f"Nomor antrian {customer.number} atas nama {customer.name}"
        
        # Panggil suara di thread agar tidak blocking dialog
        self.speak(announcement)
        
        # Buat dialog untuk panggilan (muncul langsung)
        result = self.show_call_dialog(customer)
        
        # Jika di-skip, masukkan ke posisi ke-2
        if result == 'skip':
            if len(self.queue) >= 1:
                # Insert di index 1 (posisi ke-2)
                self.queue.insert(1, customer)
                self.status_label.config(
                    text=f"Status: No.{customer.number} di-skip, dipindah ke posisi 2"
                )
            else:
                # Jika queue kosong atau cuma 1, masukkan di akhir
                self.queue.append(customer)
                self.status_label.config(
                    text=f"Status: No.{customer.number} di-skip, dikembalikan ke antrian"
                )
        else:
            self.status_label.config(
                text=f"Status: No.{customer.number} - {customer.name} selesai dilayani"
            )
        
        # Refresh table
        self.display_queue()
    
    def show_call_dialog(self, customer):
        """Tampilkan dialog panggilan antrian"""
        dialog = tk.Toplevel(self.root)
        dialog.title("Panggilan Antrian")
        dialog.geometry("450x280")
        dialog.transient(self.root)
        dialog.grab_set()
        
        # Variable untuk menyimpan hasil
        result = {'action': 'ok'}
        
        # Background color
        dialog.config(bg='#a5d6a7')
        
        # Content Frame
        content_frame = tk.Frame(dialog, bg='#a5d6a7')
        content_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=20)
        
        tk.Label(
            content_frame,
            text="MEMANGGIL",
            font=('Arial', 20, 'bold'),
            bg='#a5d6a7',
            fg='white'
        ).pack(pady=10)
        
        tk.Label(
            content_frame,
            text=f"Nomor: {customer.number}",
            font=('Arial', 36, 'bold'),
            bg='#a5d6a7',
            fg='#ffc107'
        ).pack(pady=5)
        
        tk.Label(
            content_frame,
            text=f"Nama: {customer.name}",
            font=('Arial', 24),
            bg='#a5d6a7',
            fg='white'
        ).pack(pady=5)
        
        # Button Frame
        button_frame = tk.Frame(dialog, bg='#a5d6a7')
        button_frame.pack(fill=tk.X, padx=20, pady=15)
        
        def on_ok():
            result['action'] = 'ok'
            dialog.destroy()
        
        def on_skip():
            result['action'] = 'skip'
            dialog.destroy()
        
        # OK Button
        ok_button = tk.Button(
            button_frame,
            text="OK",
            command=on_ok,
            font=('Arial', 12, 'bold'),
            bg='#ffc107',
            fg='#333333',
            padx=25,
            pady=8,
            cursor='hand2',
            width=10
        )
        ok_button.pack(side=tk.LEFT, padx=10, expand=True)
        
        # Skip Button
        skip_button = tk.Button(
            button_frame,
            text="SKIP",
            command=on_skip,
            font=('Arial', 12, 'bold'),
            bg='#ffb300',
            fg='#333333',
            padx=25,
            pady=8,
            cursor='hand2',
            width=10
        )
        skip_button.pack(side=tk.LEFT, padx=10, expand=True)
        
        # Center dialog
        dialog.update_idletasks()
        x = (dialog.winfo_screenwidth() // 2) - (dialog.winfo_width() // 2)
        y = (dialog.winfo_screenheight() // 2) - (dialog.winfo_height() // 2)
        dialog.geometry(f"+{x}+{y}")
        
        # Wait for dialog to close
        dialog.wait_window()
        
        return result['action']
    
    def speak(self, text):
        """Text-to-Speech function - runs in background thread"""
        def speak_thread():
            try:
                print(f"🔊 speak() called with text: {text}")
                print(f"   System: {platform.system()}")
                
                # Untuk Linux, gunakan gTTS dengan mpv player
                if platform.system() == 'Linux':
                    print("   Using gTTS (Google) + mpv...")
                    from gtts import gTTS
                    import tempfile
                    
                    # Generate speech dengan gTTS
                    tts = gTTS(text=text, lang='id', slow=False)
                    
                    # Save ke temporary file
                    with tempfile.NamedTemporaryFile(delete=False, suffix='.mp3') as fp:
                        temp_file = fp.name
                        tts.save(temp_file)
                    
                    print(f"   Audio saved to: {temp_file}")
                    print("   Playing with mpv...")
                    
                    # Play dengan mpv (lebih reliable untuk audio di Linux)
                    result = subprocess.run(
                        ['mpv', '--really-quiet', '--no-video', '--volume=100', temp_file],
                        stderr=subprocess.DEVNULL,
                        stdout=subprocess.DEVNULL
                    )
                    print(f"   mpv exit code: {result.returncode}")
                    
                    # Cleanup
                    try:
                        os.remove(temp_file)
                        print("   Temp file removed")
                    except Exception as e:
                        print(f"   Cleanup error: {e}")
                    
                    if result.returncode == 0:
                        print("   ✅ Audio played successfully!")
                    else:
                        print(f"   ❌ mpv failed with code {result.returncode}")
                        
                else:
                    # Untuk OS lain (macOS/Windows)
                    if self.tts_engine:
                        self.tts_engine.say(text)
                        self.tts_engine.runAndWait()
                    elif platform.system() == 'Darwin':  # macOS
                        subprocess.run(['say', '-v', 'Damayanti', text], check=False)
                    elif platform.system() == 'Windows':
                        ps_command = f"Add-Type -AssemblyName System.Speech; (New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('{text}')"
                        subprocess.run(['powershell', '-Command', ps_command], check=False)
                        
            except Exception as e:
                print(f"❌ TTS Error: {e}")
                print(f"   Text: {text}")
                import traceback
                traceback.print_exc()
        
        # Jalankan di thread terpisah agar tidak blocking GUI
        thread = threading.Thread(target=speak_thread, daemon=True)
        thread.start()
    
    def run(self):
        """Jalankan aplikasi"""
        self.root.mainloop()


def main():
    root = tk.Tk()
    app = QueueSimulation(root)
    app.run()


if __name__ == "__main__":
    main()
