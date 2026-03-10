package queue;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Simulasi Antrian Bank dengan GUI
 * Menggunakan LinkedList sebagai struktur data Queue
 */
public class QueueSimulation extends JFrame {
    // Queue untuk menyimpan data antrian
    private LinkedList<Customer> queue;
    private int currentNumber;
    
    // Komponen GUI
    private JTextField nameField;
    private final JTable queueTable;
    private final DefaultTableModel tableModel;
    private final JLabel statusLabel;
    private JButton takeNumberButton;
    private final JButton callQueueButton;
    private final JButton displayQueueButton;
    
    // Inner class untuk data Customer
    class Customer {
        int number;
        String name;
        
        Customer(int number, String name) {
            this.number = number;
            this.name = name;
        }
        
        @Override
        public String toString() {
            return "No. " + number + " - " + name;
        }
    }
    
    public QueueSimulation() {
        queue = new LinkedList<>();
        currentNumber = 0;
        
        // Setup Frame
        setTitle("Simulasi Antrian Bank");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Panel Atas - Ambil Antrian
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("Ambil Nomor Antrian"));
        topPanel.setLayout(new FlowLayout());
        
        JLabel nameLabel = new JLabel("Nama:");
        nameField = new JTextField(20);
        takeNumberButton = new JButton("Ambil Antrian");
        takeNumberButton.setBackground(new Color(46, 204, 113));
        takeNumberButton.setForeground(Color.WHITE);
        takeNumberButton.setFocusPainted(false);
        
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(takeNumberButton);
        
        // Panel Tengah - Tampilan Antrian
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Data Antrian"));
        
        // Tabel untuk menampilkan antrian
        String[] columnNames = {"Nomor Antrian", "Nama"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        queueTable = new JTable(tableModel);
        queueTable.setFont(new Font("Arial", Font.PLAIN, 14));
        queueTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        queueTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(queueTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel Bawah - Kontrol
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        displayQueueButton = new JButton("Tampilkan Antrian");
        displayQueueButton.setBackground(new Color(52, 152, 219));
        displayQueueButton.setForeground(Color.WHITE);
        displayQueueButton.setFocusPainted(false);
        
        callQueueButton = new JButton("Panggil Antrian");
        callQueueButton.setBackground(new Color(231, 76, 60));
        callQueueButton.setForeground(Color.WHITE);
        callQueueButton.setFocusPainted(false);
        
        buttonPanel.add(displayQueueButton);
        buttonPanel.add(callQueueButton);
        
        statusLabel = new JLabel("Status: Siap", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        
        // Tambahkan semua panel ke frame
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Event Listeners
        takeNumberButton.addActionListener(e -> takeNumber());
        displayQueueButton.addActionListener(e -> displayQueue());
        callQueueButton.addActionListener(e -> callQueue());
        
        // Enter key di name field
        nameField.addActionListener(e -> takeNumber());
        
        setLocationRelativeTo(null);
    }
    
    // Fitur 3: Ambil Antrian
    private void takeNumber() {
        String name = nameField.getText().trim();
        
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Nama tidak boleh kosong!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        currentNumber++;
        Customer customer = new Customer(currentNumber, name);
        queue.offer(customer);
        
        // Tampilkan konfirmasi
        JOptionPane.showMessageDialog(this,
            "Nomor Antrian Anda: " + currentNumber + "\n" +
            "Nama: " + name + "\n\n" +
            "Silakan menunggu panggilan.",
            "Antrian Berhasil",
            JOptionPane.INFORMATION_MESSAGE);
        
        nameField.setText("");
        statusLabel.setText("Status: Antrian No." + currentNumber + " ditambahkan");
        
        // Refresh table
        displayQueue();
    }
    
    // Fitur 4: Tampilkan Data Antrian
    private void displayQueue() {
        // Clear table
        tableModel.setRowCount(0);
        
        if (queue.isEmpty()) {
            statusLabel.setText("Status: Antrian kosong");
            return;
        }
        
        // Tampilkan semua data di tabel
        for (Customer customer : queue) {
            Object[] row = {customer.number, customer.name};
            tableModel.addRow(row);
        }
        
        statusLabel.setText("Status: Menampilkan " + queue.size() + " antrian");
    }
    
    // Fitur 5: Panggil Antrian
    private void callQueue() {
        if (queue.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Antrian kosong!",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE);
            statusLabel.setText("Status: Antrian kosong");
            return;
        }
        
        Customer customer = queue.poll();
        
        // Panggilan suara - tepat saat tombol ditekan
        String announcement = "Nomor antrian " + customer.number + 
                            " atas nama " + customer.name;
        speak(announcement);
        
        // Tampilkan dialog dengan info customer dan dapatkan hasil
        String result = showCallDialog(customer);
        
        // Jika di-skip, masukkan ke posisi ke-2
        if ("skip".equals(result)) {
            if (queue.size() >= 1) {
                // Insert di index 1 (posisi ke-2)
                queue.add(1, customer);
                statusLabel.setText("Status: No." + customer.number + " di-skip, dipindah ke posisi 2");
            } else {
                // Jika queue kosong atau cuma 1, masukkan di akhir
                queue.offer(customer);
                statusLabel.setText("Status: No." + customer.number + " di-skip, dikembalikan ke antrian");
            }
        } else {
            statusLabel.setText("Status: No." + customer.number + " - " + customer.name + " selesai dilayani");
        }
        
        // Refresh table
        displayQueue();
    }
    
    // Tampilkan dialog panggilan dengan tombol OK dan Skip
    private String showCallDialog(Customer customer) {
        final String[] result = {"ok"};
        
        final JDialog dialog = new JDialog(this, "Panggilan Antrian", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(450, 280);
        
        // Content Panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        contentPanel.setBackground(new Color(52, 152, 219));
        
        JLabel callLabel = new JLabel("MEMANGGIL");
        callLabel.setFont(new Font("Arial", Font.BOLD, 20));
        callLabel.setForeground(Color.WHITE);
        callLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel numberLabel = new JLabel("Nomor: " + customer.number);
        numberLabel.setFont(new Font("Arial", Font.BOLD, 36));
        numberLabel.setForeground(Color.YELLOW);
        numberLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLabel = new JLabel("Nama: " + customer.name);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPanel.add(callLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(numberLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createVerticalGlue());
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(52, 152, 219));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 15, 20));
        
        // OK Button
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.setPreferredSize(new Dimension(120, 40));
        okButton.setBackground(new Color(39, 174, 96));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> {
            result[0] = "ok";
            dialog.dispose();
        });
        
        // Skip Button
        JButton skipButton = new JButton("SKIP");
        skipButton.setFont(new Font("Arial", Font.BOLD, 12));
        skipButton.setPreferredSize(new Dimension(120, 40));
        skipButton.setBackground(new Color(243, 156, 18));
        skipButton.setForeground(Color.WHITE);
        skipButton.setFocusPainted(false);
        skipButton.addActionListener(e -> {
            result[0] = "skip";
            dialog.dispose();
        });
        
        buttonPanel.add(okButton);
        buttonPanel.add(skipButton);
        
        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        
        return result[0];
    }
    
    // Fungsi Text-to-Speech sederhana
    private void speak(String text) {
        // Jalankan di thread terpisah agar tidak blocking GUI
        new Thread(() -> {
            try {
                // Opsi 1: Menggunakan system TTS (Linux)
                if (System.getProperty("os.name").toLowerCase().contains("linux")) {
                    Runtime.getRuntime().exec(new String[]{"spd-say", text});
                }
                // Opsi 2: Menggunakan system TTS (Windows)
                else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    Runtime.getRuntime().exec(new String[]{
                        "powershell", 
                        "-Command", 
                        "Add-Type -AssemblyName System.Speech; " +
                        "(New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('" + text + "')"
                    });
                }
                // Opsi 3: Menggunakan system TTS (Mac)
                else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    Runtime.getRuntime().exec(new String[]{"say", text});
                }
            } catch (IOException e) {
                System.out.println("TTS Error: " + e.getMessage());
                System.out.println("Text: " + text);
            }
        }).start();
    }
    
    public static void main(String[] args) {
        // Gunakan Look and Feel sistem
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Error setting Look and Feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            QueueSimulation frame = new QueueSimulation();
            frame.setVisible(true);
        });
    }
}
