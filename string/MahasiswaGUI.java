package string;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

class MahasiswaData {
    String nim;
    String nama;

    public MahasiswaData(String nim, String nama) {
        this.nim = nim;
        this.nama = nama;
    }
}

public class MahasiswaGUI extends JFrame {
    private static final int CAPACITY = 10;
    private final MahasiswaData[] data;
    private int count;

    // UI Components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nimField, namaField, positionField;
    private JLabel statusLabel, countLabel;
    private JPanel mainPanel;

    // Colors
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color BG_COLOR = new Color(236, 240, 241);
    private final Color CARD_COLOR = Color.WHITE;

    public MahasiswaGUI() {
        data = new MahasiswaData[CAPACITY];
        count = 0;
        
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Sistem Manajemen Data Mahasiswa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Main Panel
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Center - Table
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);
        
        // Bottom - Operations
        mainPanel.add(createOperationsPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
        
        updateTable();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("📚 MANAJEMEN DATA MAHASISWA");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        countLabel = new JLabel("Total Data: 0/" + CAPACITY);
        countLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        countLabel.setForeground(Color.WHITE);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(countLabel, BorderLayout.SOUTH);
        
        return headerPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(CARD_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Table Header
        JLabel tableTitle = new JLabel("📋 Daftar Mahasiswa");
        tableTitle.setFont(new Font("Arial", Font.BOLD, 18));
        tableTitle.setForeground(new Color(44, 62, 80));
        
        // Table
        String[] columns = {"No", "NIM", "Nama"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(52, 152, 219, 100));
        table.setGridColor(new Color(189, 195, 199));
        
        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(52, 73, 94));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199), 1));
        
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }

    private JPanel createOperationsPanel() {
        JPanel operationsPanel = new JPanel(new GridBagLayout());
        operationsPanel.setBackground(CARD_COLOR);
        operationsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Input Section
        JPanel inputPanel = createInputPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        operationsPanel.add(inputPanel, gbc);
        
        // Insert Operations
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.33;
        operationsPanel.add(createStyledButton("⬆️ Insert at Beginning", SUCCESS_COLOR, e -> insertAtBeginning()), gbc);
        
        gbc.gridx = 1;
        operationsPanel.add(createStyledButton("📍 Insert at Position", SUCCESS_COLOR, e -> insertAtPosition()), gbc);
        
        gbc.gridx = 2;
        operationsPanel.add(createStyledButton("⬇️ Insert at End", SUCCESS_COLOR, e -> insertAtEnd()), gbc);
        
        // Delete Operations
        gbc.gridx = 0;
        gbc.gridy = 2;
        operationsPanel.add(createStyledButton("❌ Delete from Beginning", DANGER_COLOR, e -> deleteFromBeginning()), gbc);
        
        gbc.gridx = 1;
        operationsPanel.add(createStyledButton("🗑️ Delete at Position", DANGER_COLOR, e -> deleteFromPosition()), gbc);
        
        gbc.gridx = 2;
        operationsPanel.add(createStyledButton("⛔ Delete from End", DANGER_COLOR, e -> deleteFromEnd()), gbc);
        
        // Other Operations
        gbc.gridx = 0;
        gbc.gridy = 3;
        operationsPanel.add(createStyledButton("🔍 Delete by NIM", DANGER_COLOR, e -> deleteFirstOccurrence()), gbc);
        
        gbc.gridx = 1;
        operationsPanel.add(createStyledButton("🔄 Refresh Table", PRIMARY_COLOR, e -> updateTable()), gbc);
        
        gbc.gridx = 2;
        operationsPanel.add(createStyledButton("🚪 Exit", new Color(149, 165, 166), e -> System.exit(0)), gbc);
        
        // Status Label
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        statusLabel.setForeground(new Color(127, 140, 141));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        operationsPanel.add(statusLabel, gbc);
        
        return operationsPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(250, 250, 250));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        Font labelFont = new Font("Arial", Font.BOLD, 13);
        
        // NIM
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.2;
        JLabel nimLabel = new JLabel("NIM:");
        nimLabel.setFont(labelFont);
        inputPanel.add(nimLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        nimField = new JTextField(15);
        nimField.setFont(new Font("Arial", Font.PLAIN, 14));
        nimField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(nimField, gbc);
        
        // Nama
        gbc.gridx = 2;
        gbc.weightx = 0.2;
        JLabel namaLabel = new JLabel("Nama:");
        namaLabel.setFont(labelFont);
        inputPanel.add(namaLabel, gbc);
        
        gbc.gridx = 3;
        gbc.weightx = 0.8;
        namaField = new JTextField(15);
        namaField.setFont(new Font("Arial", Font.PLAIN, 14));
        namaField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(namaField, gbc);
        
        // Position
        gbc.gridx = 4;
        gbc.weightx = 0.2;
        JLabel posLabel = new JLabel("Position:");
        posLabel.setFont(labelFont);
        inputPanel.add(posLabel, gbc);
        
        gbc.gridx = 5;
        gbc.weightx = 0.3;
        positionField = new JTextField(5);
        positionField.setFont(new Font("Arial", Font.PLAIN, 14));
        positionField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        inputPanel.add(positionField, gbc);
        
        return inputPanel;
    }

    private JButton createStyledButton(String text, Color bgColor, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 45));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        button.addActionListener(listener);
        return button;
    }

    private void insertAtBeginning() {
        String nim = nimField.getText().trim();
        String nama = namaField.getText().trim();
        
        if (nim.isEmpty() || nama.isEmpty()) {
            showError("NIM dan Nama harus diisi!");
            return;
        }
        
        if (count >= CAPACITY) {
            showError("Array penuh! Tidak bisa menambah data.");
            return;
        }
        
        for (int i = count; i > 0; i--) {
            data[i] = data[i - 1];
        }
        data[0] = new MahasiswaData(nim, nama);
        count++;
        
        clearFields();
        updateTable();
        showSuccess("Data berhasil ditambahkan di awal!");
    }

    private void insertAtPosition() {
        String nim = nimField.getText().trim();
        String nama = namaField.getText().trim();
        String posStr = positionField.getText().trim();
        
        if (nim.isEmpty() || nama.isEmpty() || posStr.isEmpty()) {
            showError("NIM, Nama, dan Position harus diisi!");
            return;
        }
        
        try {
            int position = Integer.parseInt(posStr);
            
            if (position < 0 || position > count) {
                showError("Posisi tidak valid! (0 - " + count + ")");
                return;
            }
            
            if (count >= CAPACITY) {
                showError("Array penuh! Tidak bisa menambah data.");
                return;
            }
            
            for (int i = count; i > position; i--) {
                data[i] = data[i - 1];
            }
            data[position] = new MahasiswaData(nim, nama);
            count++;
            
            clearFields();
            updateTable();
            showSuccess("Data berhasil ditambahkan di posisi " + position + "!");
        } catch (NumberFormatException e) {
            showError("Position harus berupa angka!");
        }
    }

    private void insertAtEnd() {
        String nim = nimField.getText().trim();
        String nama = namaField.getText().trim();
        
        if (nim.isEmpty() || nama.isEmpty()) {
            showError("NIM dan Nama harus diisi!");
            return;
        }
        
        if (count >= CAPACITY) {
            showError("Array penuh! Tidak bisa menambah data.");
            return;
        }
        
        data[count] = new MahasiswaData(nim, nama);
        count++;
        
        clearFields();
        updateTable();
        showSuccess("Data berhasil ditambahkan di akhir!");
    }

    private void deleteFromBeginning() {
        if (count == 0) {
            showError("Array kosong! Tidak ada data yang dihapus.");
            return;
        }
        
        for (int i = 0; i < count - 1; i++) {
            data[i] = data[i + 1];
        }
        data[count - 1] = null;
        count--;
        
        updateTable();
        showSuccess("Data berhasil dihapus dari awal!");
    }

    private void deleteFromPosition() {
        String posStr = positionField.getText().trim();
        
        if (posStr.isEmpty()) {
            showError("Position harus diisi!");
            return;
        }
        
        try {
            int position = Integer.parseInt(posStr);
            
            if (position < 0 || position >= count) {
                showError("Posisi tidak valid! (0 - " + (count - 1) + ")");
                return;
            }
            
            for (int i = position; i < count - 1; i++) {
                data[i] = data[i + 1];
            }
            data[count - 1] = null;
            count--;
            
            clearFields();
            updateTable();
            showSuccess("Data berhasil dihapus dari posisi " + position + "!");
        } catch (NumberFormatException e) {
            showError("Position harus berupa angka!");
        }
    }

    private void deleteFromEnd() {
        if (count == 0) {
            showError("Array kosong! Tidak ada data yang dihapus.");
            return;
        }
        
        data[count - 1] = null;
        count--;
        
        updateTable();
        showSuccess("Data berhasil dihapus dari akhir!");
    }

    private void deleteFirstOccurrence() {
        String nim = nimField.getText().trim();
        
        if (nim.isEmpty()) {
            showError("NIM harus diisi!");
            return;
        }
        
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (data[i].nim.equals(nim)) {
                for (int j = i; j < count - 1; j++) {
                    data[j] = data[j + 1];
                }
                data[count - 1] = null;
                count--;
                found = true;
                break;
            }
        }
        
        if (found) {
            clearFields();
            updateTable();
            showSuccess("Data dengan NIM " + nim + " berhasil dihapus!");
        } else {
            showError("Data dengan NIM " + nim + " tidak ditemukan!");
        }
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        
        for (int i = 0; i < count; i++) {
            tableModel.addRow(new Object[]{
                i + 1,
                data[i].nim,
                data[i].nama
            });
        }
        
        countLabel.setText("Total Data: " + count + "/" + CAPACITY);
    }

    private void clearFields() {
        nimField.setText("");
        namaField.setText("");
        positionField.setText("");
    }

    private void showSuccess(String message) {
        statusLabel.setText("✅ " + message);
        statusLabel.setForeground(SUCCESS_COLOR);
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        statusLabel.setText("❌ " + message);
        statusLabel.setForeground(DANGER_COLOR);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new MahasiswaGUI());
    }
}
