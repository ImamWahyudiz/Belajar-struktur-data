package matrix;

import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.*;

class AnimationStep {
    int[][] matrixState;
    int rows;
    int cols;
    List<Point> activeCells;
    List<Point> visitedCells;
    String logMessage;

    public AnimationStep(int[][] state, int r, int c, List<Point> active, List<Point> visited, String msg) {
        this.rows = r;
        this.cols = c;
        this.matrixState = new int[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(state[i], 0, this.matrixState[i], 0, c);
        }
        this.activeCells = new ArrayList<>(active);
        this.visitedCells = new ArrayList<>(visited);
        this.logMessage = msg;
    }
}

class MatrixOperations {
    
    private int[][] cloneMatrix(int[][] matrix, int r, int c) {
        int[][] clone = new int[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(matrix[i], 0, clone[i], 0, c);
        }
        return clone;
    }

    public List<AnimationStep> sortRowWise(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Memulai Sortir per Baris."));
        
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c - 1; j++) {
                for (int k = 0; k < c - j - 1; k++) {
                    List<Point> active = Arrays.asList(new Point(i, k), new Point(i, k+1));
                    steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), String.format("Membandingkan elemen (%d,%d) dan (%d,%d)", i, k, i, k+1)));
                    
                    if (state[i][k] > state[i][k + 1]) {
                        int temp = state[i][k];
                        state[i][k] = state[i][k + 1];
                        state[i][k + 1] = temp;
                        steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menukar elemen karena elemen kiri lebih besar."));
                    }
                }
            }
            steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Baris ke-" + i + " selesai disortir."));
        }
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Sortir per baris selesai."));
        return steps;
    }

    public List<AnimationStep> sortColWise(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Memulai Sortir per Kolom."));
        
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r - 1; i++) {
                for (int k = 0; k < r - i - 1; k++) {
                    List<Point> active = Arrays.asList(new Point(k, j), new Point(k+1, j));
                    steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), String.format("Membandingkan elemen (%d,%d) dan (%d,%d)", k, j, k+1, j)));
                    
                    if (state[k][j] > state[k + 1][j]) {
                        int temp = state[k][j];
                        state[k][j] = state[k + 1][j];
                        state[k + 1][j] = temp;
                        steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menukar elemen karena elemen atas lebih besar."));
                    }
                }
            }
            steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Kolom ke-" + j + " selesai disortir."));
        }
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Sortir per kolom selesai."));
        return steps;
    }

    public List<AnimationStep> rotateClockwise1(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Memulai Rotasi Searah Jarum Jam sebanyak 1 elemen."));
        
        int rowStart = 0, rowEnd = r - 1;
        int colStart = 0, colEnd = c - 1;
        
        while (rowStart < rowEnd && colStart < colEnd) {
            int prev = state[rowStart + 1][colStart];
            
            for (int i = colStart; i <= colEnd; i++) {
                List<Point> active = Arrays.asList(new Point(rowStart, i));
                steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menggeser batas atas cincin ke kanan."));
                int curr = state[rowStart][i];
                state[rowStart][i] = prev;
                prev = curr;
            }
            rowStart++;
            
            for (int i = rowStart; i <= rowEnd; i++) {
                List<Point> active = Arrays.asList(new Point(i, colEnd));
                steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menggeser batas kanan cincin ke bawah."));
                int curr = state[i][colEnd];
                state[i][colEnd] = prev;
                prev = curr;
            }
            colEnd--;
            
            if (rowStart <= rowEnd) {
                for (int i = colEnd; i >= colStart; i--) {
                    List<Point> active = Arrays.asList(new Point(rowEnd, i));
                    steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menggeser batas bawah cincin ke kiri."));
                    int curr = state[rowEnd][i];
                    state[rowEnd][i] = prev;
                    prev = curr;
                }
                rowEnd--;
            }
            
            if (colStart <= colEnd) {
                for (int i = rowEnd; i >= rowStart; i--) {
                    List<Point> active = Arrays.asList(new Point(i, colStart));
                    steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menggeser batas kiri cincin ke atas."));
                    int curr = state[i][colStart];
                    state[i][colStart] = prev;
                    prev = curr;
                }
                colStart++;
            }
        }
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Rotasi searah jarum jam sebanyak 1 elemen selesai."));
        return steps;
    }

    public List<AnimationStep> rotateCounterClockwise1(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Memulai Rotasi Berlawanan Arah Jarum Jam sebanyak 1 elemen."));
        
        int rowStart = 0, rowEnd = r - 1;
        int colStart = 0, colEnd = c - 1;
        
        while (rowStart < rowEnd && colStart < colEnd) {
            int prev = state[rowStart][colStart + 1];
            
            for (int i = rowStart; i <= rowEnd; i++) {
                List<Point> active = Arrays.asList(new Point(i, colStart));
                steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menggeser batas kiri cincin ke bawah."));
                int curr = state[i][colStart];
                state[i][colStart] = prev;
                prev = curr;
            }
            colStart++;
            
            for (int i = colStart; i <= colEnd; i++) {
                List<Point> active = Arrays.asList(new Point(rowEnd, i));
                steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menggeser batas bawah cincin ke kanan."));
                int curr = state[rowEnd][i];
                state[rowEnd][i] = prev;
                prev = curr;
            }
            rowEnd--;
            
            if (colStart <= colEnd) {
                for (int i = rowEnd; i >= rowStart; i--) {
                    List<Point> active = Arrays.asList(new Point(i, colEnd));
                    steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menggeser batas kanan cincin ke atas."));
                    int curr = state[i][colEnd];
                    state[i][colEnd] = prev;
                    prev = curr;
                }
                colEnd--;
            }
            
            if (rowStart <= rowEnd) {
                for (int i = colEnd; i >= colStart; i--) {
                    List<Point> active = Arrays.asList(new Point(rowStart, i));
                    steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), "Menggeser batas atas cincin ke kiri."));
                    int curr = state[rowStart][i];
                    state[rowStart][i] = prev;
                    prev = curr;
                }
                rowStart++;
            }
        }
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Rotasi berlawanan arah jarum jam sebanyak 1 elemen selesai."));
        return steps;
    }

    public List<AnimationStep> rotate90(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Memulai Rotasi 90 Derajat (Searah Jarum Jam). Pertama, Transpose matriks."));
        
        int[][] newState = new int[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                newState[j][i] = state[i][j];
                List<Point> active = Arrays.asList(new Point(j, i));
                steps.add(new AnimationStep(newState, c, r, active, new ArrayList<>(), String.format("Memindahkan elemen (%d,%d) ke (%d,%d)", i, j, j, i)));
            }
        }
        
        steps.add(new AnimationStep(newState, c, r, new ArrayList<>(), new ArrayList<>(), "Transpose selesai. Selanjutnya, balik urutan setiap baris."));
        
        for (int i = 0; i < c; i++) {
            int left = 0, right = r - 1;
            while (left < right) {
                List<Point> active = Arrays.asList(new Point(i, left), new Point(i, right));
                steps.add(new AnimationStep(newState, c, r, active, new ArrayList<>(), String.format("Menukar elemen kiri dan kanan pada baris %d", i)));
                int temp = newState[i][left];
                newState[i][left] = newState[i][right];
                newState[i][right] = temp;
                left++;
                right--;
            }
        }
        
        steps.add(new AnimationStep(newState, c, r, new ArrayList<>(), new ArrayList<>(), "Rotasi 90 derajat selesai."));
        return steps;
    }

    public List<AnimationStep> rotate180(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Memulai Rotasi 180 Derajat. Pertama, balik urutan baris."));
        
        for (int i = 0; i < r / 2; i++) {
            int targetRow = r - 1 - i;
            for (int j = 0; j < c; j++) {
                List<Point> active = Arrays.asList(new Point(i, j), new Point(targetRow, j));
                steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), String.format("Menukar elemen baris %d dan baris %d", i, targetRow)));
                int temp = state[i][j];
                state[i][j] = state[targetRow][j];
                state[targetRow][j] = temp;
            }
        }
        
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Pembalikan baris selesai. Selanjutnya, balik elemen di setiap baris."));
        
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c / 2; j++) {
                int targetCol = c - 1 - j;
                List<Point> active = Arrays.asList(new Point(i, j), new Point(i, targetCol));
                steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(), String.format("Menukar elemen kiri dan kanan pada baris %d", i)));
                int temp = state[i][j];
                state[i][j] = state[i][targetCol];
                state[i][targetCol] = temp;
            }
        }
        
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Rotasi 180 derajat selesai."));
        return steps;
    }

    public List<AnimationStep> traverseRowWise(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        List<Point> visited = new ArrayList<>();
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), visited, "Memulai Traversal per Baris."));
        
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                Point p = new Point(i, j);
                List<Point> active = Collections.singletonList(p);
                visited.add(p);
                steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(visited), String.format("Mengunjungi elemen di baris %d, kolom %d", i, j)));
            }
        }
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), visited, "Traversal per baris selesai."));
        return steps;
    }

    public List<AnimationStep> traverseColWise(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        List<Point> visited = new ArrayList<>();
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), visited, "Memulai Traversal per Kolom."));
        
        for (int j = 0; j < c; j++) {
            for (int i = 0; i < r; i++) {
                Point p = new Point(i, j);
                List<Point> active = Collections.singletonList(p);
                visited.add(p);
                steps.add(new AnimationStep(state, r, c, active, new ArrayList<>(visited), String.format("Mengunjungi elemen di kolom %d, baris %d", j, i)));
            }
        }
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), visited, "Traversal per kolom selesai."));
        return steps;
    }

    public List<AnimationStep> traverseSpiral(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        List<Point> visited = new ArrayList<>();
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), visited, "Memulai Traversal Spiral."));
        
        int rowStart = 0, rowEnd = r - 1;
        int colStart = 0, colEnd = c - 1;
        
        while (rowStart <= rowEnd && colStart <= colEnd) {
            for (int i = colStart; i <= colEnd; i++) {
                Point p = new Point(rowStart, i);
                visited.add(p);
                steps.add(new AnimationStep(state, r, c, Collections.singletonList(p), new ArrayList<>(visited), "Maju ke kanan sepanjang batas atas."));
            }
            rowStart++;
            
            for (int i = rowStart; i <= rowEnd; i++) {
                Point p = new Point(i, colEnd);
                visited.add(p);
                steps.add(new AnimationStep(state, r, c, Collections.singletonList(p), new ArrayList<>(visited), "Turun ke bawah sepanjang batas kanan."));
            }
            colEnd--;
            
            if (rowStart <= rowEnd) {
                for (int i = colEnd; i >= colStart; i--) {
                    Point p = new Point(rowEnd, i);
                    visited.add(p);
                    steps.add(new AnimationStep(state, r, c, Collections.singletonList(p), new ArrayList<>(visited), "Mundur ke kiri sepanjang batas bawah."));
                }
                rowEnd--;
            }
            
            if (colStart <= colEnd) {
                for (int i = rowEnd; i >= rowStart; i--) {
                    Point p = new Point(i, colStart);
                    visited.add(p);
                    steps.add(new AnimationStep(state, r, c, Collections.singletonList(p), new ArrayList<>(visited), "Naik ke atas sepanjang batas kiri."));
                }
                colStart++;
            }
        }
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), visited, "Traversal spiral selesai."));
        return steps;
    }

    public List<AnimationStep> transpose(int[][] matrix, int r, int c) {
        List<AnimationStep> steps = new ArrayList<>();
        int[][] state = cloneMatrix(matrix, r, c);
        steps.add(new AnimationStep(state, r, c, new ArrayList<>(), new ArrayList<>(), "Memulai Transpose Matriks."));
        
        int[][] newState = new int[c][r];
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                newState[j][i] = state[i][j];
                List<Point> active = Arrays.asList(new Point(j, i));
                steps.add(new AnimationStep(newState, c, r, active, new ArrayList<>(), String.format("Memindahkan elemen (%d,%d) ke (%d,%d)", i, j, j, i)));
            }
        }
        steps.add(new AnimationStep(newState, c, r, new ArrayList<>(), new ArrayList<>(), "Transpose matriks selesai. Baris menjadi kolom dan kolom menjadi baris."));
        return steps;
    }
}

class MatrixPanel extends JPanel {
    private int[][] matrix;
    private int rows;
    private int cols;
    private int[][] originalMatrix;
    private int origRows;
    private int origCols;
    private List<Point> activeCells = new ArrayList<>();
    private List<Point> visitedCells = new ArrayList<>();
    
    // Tema warna modern (Catppuccin Mocha Palette)
    private final Color bgColor = new Color(0x1E, 0x1E, 0x2E);
    private final Color cellDefault = new Color(0x31, 0x32, 0x44);
    private final Color cellActive = new Color(0xF3, 0x8B, 0xA8);      // red/pink
    private final Color cellVisited = new Color(0xA6, 0xE3, 0xA1);     // green
    private final Color textColor = new Color(0xCD, 0xD6, 0xF4);
    private final Color cellBorder = new Color(0x45, 0x47, 0x5A);

    public MatrixPanel() {
        setBackground(bgColor);
    }

    public void updateState(int[][] m, int r, int c, List<Point> active, List<Point> visited, int[][] origM, int origR, int origC) {
        this.rows = r;
        this.cols = c;
        this.matrix = new int[r][c];
        for (int i = 0; i < r; i++) {
            System.arraycopy(m[i], 0, this.matrix[i], 0, c);
        }
        
        if (origM != null) {
            this.origRows = origR;
            this.origCols = origC;
            this.originalMatrix = new int[origR][origC];
            for (int i = 0; i < origR; i++) {
                System.arraycopy(origM[i], 0, this.originalMatrix[i], 0, origC);
            }
        } else {
            this.originalMatrix = null;
        }
        
        this.activeCells = new ArrayList<>(active);
        this.visitedCells = new ArrayList<>(visited);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (matrix == null || rows == 0 || cols == 0) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int cellSize = Math.min((getWidth() - 40) / cols, (getHeight() - 40) / rows);
        cellSize = Math.min(cellSize, 80); // max size
        if (cellSize < 10) cellSize = 10;
        
        int startX = (getWidth() - (cols * cellSize)) / 2;
        int startY = (getHeight() - (rows * cellSize)) / 2;
        
        g2.setFont(new Font("Arial", Font.BOLD, cellSize / 3));
        FontMetrics fm = g2.getFontMetrics();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * cellSize;
                int y = startY + i * cellSize;
                
                Point p = new Point(i, j);
                Color fill = cellDefault;
                if (visitedCells.contains(p)) {
                    fill = cellVisited;
                }
                if (activeCells.contains(p)) {
                    fill = cellActive;
                }
                
                g2.setColor(fill);
                g2.fillRect(x, y, cellSize, cellSize);
                
                g2.setColor(cellBorder);
                g2.drawRect(x, y, cellSize, cellSize);
                
                g2.setColor(textColor);
                String val = String.valueOf(matrix[i][j]);
                int txtX = x + (cellSize - fm.stringWidth(val)) / 2;
                int txtY = y + ((cellSize - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(val, txtX, txtY);
            }
        }
        
        // Draw Original Matrix at Top-Left if it exists
        if (originalMatrix != null && origRows > 0 && origCols > 0) {
            int miniCellSize = Math.max(10, cellSize / 2);
            if (miniCellSize > 25) miniCellSize = 25;
            
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.setColor(new Color(0xF5, 0xC2, 0xE7)); // Pinkish text for label
            g2.drawString("Versi Asli:", 15, 20);
            
            int miniStartX = 15;
            int miniStartY = 30;
            
            g2.setFont(new Font("Arial", Font.PLAIN, miniCellSize / 2 + 2));
            FontMetrics miniFm = g2.getFontMetrics();
            
            for (int i = 0; i < origRows; i++) {
                for (int j = 0; j < origCols; j++) {
                    int x = miniStartX + j * miniCellSize;
                    int y = miniStartY + i * miniCellSize;
                    
                    g2.setColor(cellDefault);
                    g2.fillRect(x, y, miniCellSize, miniCellSize);
                    
                    g2.setColor(cellBorder);
                    g2.drawRect(x, y, miniCellSize, miniCellSize);
                    
                    g2.setColor(new Color(0x6C, 0x70, 0x86)); // Dimmer text for mini matrix
                    String val = String.valueOf(originalMatrix[i][j]);
                    int txtX = x + (miniCellSize - miniFm.stringWidth(val)) / 2;
                    int txtY = y + ((miniCellSize - miniFm.getHeight()) / 2) + miniFm.getAscent();
                    g2.drawString(val, txtX, txtY);
                }
            }
        }
    }
}

public class javaX extends JFrame {
    
    private MatrixPanel canvasPanel;
    private int[][] currentMatrix;
    private int[][] animationOriginalMatrix;
    private int animationOriginalRows;
    private int animationOriginalCols;
    private int currentRows = 4;
    private int currentCols = 4;
    private MatrixOperations ops = new MatrixOperations();
    
    // Animation state
    private List<AnimationStep> currentSteps = new ArrayList<>();
    private int stepIdx = 0;
    private javax.swing.Timer timer;
    private int animationDelay = 800; // ms
    private boolean isPaused = false;
    
    // UI Components
    private JComboBox<String> cbAlgorithm;
    private JButton btnStart, btnPause, btnStep, btnReset;
    private JTextArea txtLog;
    private JSlider sliderSpeed;
    private JTextField tfRows, tfCols;
    private JButton btnGenerate;
    private JPanel sidebar;
    
    // Colors
    private final Color bgColor = new Color(0x1E, 0x1E, 0x2E);
    private final Color panelBg = new Color(0x18, 0x18, 0x25);
    private final Color textColor = new Color(0xCD, 0xD6, 0xF4);
    private final Color btnColor = new Color(0x31, 0x32, 0x44);

    public javaX() {
        setTitle("Visualisasi Operasi Matriks (Java Swing)");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(bgColor);
        setLayout(new BorderLayout());
        
        canvasPanel = new MatrixPanel();
        add(canvasPanel, BorderLayout.CENTER);
        
        setupSidebar();
        
        generateRandomMatrix(currentRows, currentCols);
        
        timer = new javax.swing.Timer(animationDelay, e -> nextStep());
    }
    
    private void setupSidebar() {
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(panelBg);
        sidebar.setPreferredSize(new Dimension(320, getHeight()));
        sidebar.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // --- 1. Matrix Initialization ---
        JLabel lblInit = new JLabel("Inisialisasi Matriks:");
        lblInit.setForeground(new Color(0xF5, 0xC2, 0xE7));
        lblInit.setFont(new Font("Arial", Font.BOLD, 12));
        sidebar.add(lblInit);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel pnlDim = new JPanel(new GridLayout(1, 4, 5, 0));
        pnlDim.setBackground(panelBg);
        pnlDim.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel lblR = new JLabel("Baris:");
        lblR.setForeground(textColor);
        tfRows = new JTextField("4");
        tfRows.setBackground(btnColor); tfRows.setForeground(textColor); tfRows.setCaretColor(textColor);
        
        JLabel lblC = new JLabel("Kolom:");
        lblC.setForeground(textColor);
        tfCols = new JTextField("4");
        tfCols.setBackground(btnColor); tfCols.setForeground(textColor); tfCols.setCaretColor(textColor);
        
        pnlDim.add(lblR); pnlDim.add(tfRows);
        pnlDim.add(lblC); pnlDim.add(tfCols);
        sidebar.add(pnlDim);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        btnGenerate = new JButton("Generate Matriks Acak");
        styleButton(btnGenerate, new Color(0x89, 0xB4, 0xFA), new Color(0x11, 0x11, 0x1B));
        btnGenerate.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnGenerate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnGenerate.addActionListener(e -> {
            try {
                int r = Integer.parseInt(tfRows.getText());
                int c = Integer.parseInt(tfCols.getText());
                if(r <= 0 || c <= 0 || r > 20 || c > 20) {
                    JOptionPane.showMessageDialog(this, "Dimensi matriks harus 1-20.");
                    return;
                }
                currentRows = r;
                currentCols = c;
                generateRandomMatrix(r, c);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid!");
            }
        });
        sidebar.add(btnGenerate);
        sidebar.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // --- 2. Algorithms ---
        JLabel lblAlg = new JLabel("Menu Operasi:");
        lblAlg.setForeground(new Color(0xF5, 0xC2, 0xE7));
        lblAlg.setFont(new Font("Arial", Font.BOLD, 12));
        sidebar.add(lblAlg);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        
        String[] algos = {
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
        };
        cbAlgorithm = new JComboBox<>(algos);
        cbAlgorithm.setBackground(btnColor); cbAlgorithm.setForeground(textColor);
        cbAlgorithm.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        sidebar.add(cbAlgorithm);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // --- 3. Controls ---
        JPanel pnlControls = new JPanel(new GridLayout(2, 2, 8, 8));
        pnlControls.setBackground(panelBg);
        pnlControls.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        btnStart = new JButton("Start");
        btnPause = new JButton("Pause");
        btnStep = new JButton("Step");
        btnReset = new JButton("Reset");
        
        styleButton(btnStart, new Color(0xA6, 0xE3, 0xA1), new Color(0x11, 0x11, 0x1B));
        styleButton(btnPause, new Color(0xCB, 0xA6, 0xF7), new Color(0x11, 0x11, 0x1B));
        styleButton(btnStep, new Color(0xF9, 0xE2, 0xAF), new Color(0x11, 0x11, 0x1B));
        styleButton(btnReset, new Color(0xF3, 0x8B, 0xA8), new Color(0x11, 0x11, 0x1B));
        
        btnStart.addActionListener(e -> startAnimation());
        btnPause.addActionListener(e -> togglePause());
        btnStep.addActionListener(e -> nextStep());
        btnReset.addActionListener(e -> stopAnimation(true));
        
        pnlControls.add(btnStart);
        pnlControls.add(btnPause);
        pnlControls.add(btnStep);
        pnlControls.add(btnReset);
        sidebar.add(pnlControls);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // --- 4. Speed ---
        JLabel lblSpeed = new JLabel("Kecepatan Animasi:");
        lblSpeed.setForeground(textColor);
        sidebar.add(lblSpeed);
        
        sliderSpeed = new JSlider(100, 2000, 800);
        sliderSpeed.setBackground(panelBg);
        sliderSpeed.setForeground(textColor);
        sliderSpeed.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        sliderSpeed.addChangeListener(e -> {
            animationDelay = sliderSpeed.getValue();
            timer.setDelay(animationDelay);
        });
        sidebar.add(sliderSpeed);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // --- 5. Log ---
        JLabel lblLog = new JLabel("Log Eksekusi:");
        lblLog.setForeground(textColor);
        sidebar.add(lblLog);
        
        txtLog = new JTextArea();
        txtLog.setBackground(bgColor);
        txtLog.setForeground(textColor);
        txtLog.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);
        
        JScrollPane scrollLog = new JScrollPane(txtLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(new Color(0x45, 0x47, 0x5A)));
        sidebar.add(scrollLog);
        
        add(sidebar, BorderLayout.EAST);
    }
    
    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }
    
    private void generateRandomMatrix(int r, int c) {
        stopAnimation(false);
        currentMatrix = new int[r][c];
        Random rand = new Random();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                currentMatrix[i][j] = rand.nextInt(90) + 10; // 10-99
            }
        }
        animationOriginalMatrix = null;
        canvasPanel.updateState(currentMatrix, r, c, new ArrayList<>(), new ArrayList<>(), null, 0, 0);
        txtLog.setText("Matriks acak " + r + "x" + c + " berhasil di-generate.\n");
    }
    
    private void logMessage(String msg) {
        txtLog.append(msg + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    private void startAnimation() {
        int index = cbAlgorithm.getSelectedIndex();
        if (index == 10) { // Quit
            System.exit(0);
        }
        
        stopAnimation(false);
        
        // Simpan state asli untuk ditampilkan di pojok kiri atas
        animationOriginalRows = currentRows;
        animationOriginalCols = currentCols;
        animationOriginalMatrix = new int[currentRows][currentCols];
        for(int i = 0; i < currentRows; i++){
            System.arraycopy(currentMatrix[i], 0, animationOriginalMatrix[i], 0, currentCols);
        }
        
        switch (index) {
            case 0: currentSteps = ops.sortRowWise(currentMatrix, currentRows, currentCols); break;
            case 1: currentSteps = ops.sortColWise(currentMatrix, currentRows, currentCols); break;
            case 2: currentSteps = ops.rotateClockwise1(currentMatrix, currentRows, currentCols); break;
            case 3: currentSteps = ops.rotateCounterClockwise1(currentMatrix, currentRows, currentCols); break;
            case 4: currentSteps = ops.rotate90(currentMatrix, currentRows, currentCols); break;
            case 5: currentSteps = ops.rotate180(currentMatrix, currentRows, currentCols); break;
            case 6: currentSteps = ops.traverseRowWise(currentMatrix, currentRows, currentCols); break;
            case 7: currentSteps = ops.traverseColWise(currentMatrix, currentRows, currentCols); break;
            case 8: currentSteps = ops.traverseSpiral(currentMatrix, currentRows, currentCols); break;
            case 9: currentSteps = ops.transpose(currentMatrix, currentRows, currentCols); break;
        }
        
        if (currentSteps != null && !currentSteps.isEmpty()) {
            stepIdx = 0;
            isPaused = false;
            btnPause.setText("Pause");
            timer.start();
            logMessage("---- Memulai Animasi ----");
        }
    }
    
    private void togglePause() {
        if (timer.isRunning()) {
            timer.stop();
            isPaused = true;
            btnPause.setText("Resume");
            logMessage("[Animasi di-pause]");
        } else if (isPaused && currentSteps != null && stepIdx < currentSteps.size()) {
            timer.start();
            isPaused = false;
            btnPause.setText("Pause");
            logMessage("[Animasi dilanjutkan]");
        }
    }
    
    private void stopAnimation(boolean restoreInitial) {
        timer.stop();
        isPaused = false;
        btnPause.setText("Pause");
        currentSteps = new ArrayList<>();
        stepIdx = 0;
        
        if (restoreInitial) {
            animationOriginalMatrix = null;
            canvasPanel.updateState(currentMatrix, currentRows, currentCols, new ArrayList<>(), new ArrayList<>(), null, 0, 0);
            txtLog.setText("Animasi direset.\n");
        }
    }
    
    private void nextStep() {
        if (currentSteps == null || currentSteps.isEmpty()) return;
        
        if (stepIdx < currentSteps.size()) {
            AnimationStep step = currentSteps.get(stepIdx);
            canvasPanel.updateState(step.matrixState, step.rows, step.cols, step.activeCells, step.visitedCells, animationOriginalMatrix, animationOriginalRows, animationOriginalCols);
            logMessage("Step " + (stepIdx + 1) + ": " + step.logMessage);
            
            // If this is the last step, update the base matrix to be the final state so further ops work on it
            if (stepIdx == currentSteps.size() - 1) {
                currentMatrix = step.matrixState;
                currentRows = step.rows;
                currentCols = step.cols;
                timer.stop();
                logMessage("---- Animasi Selesai ----");
            }
            stepIdx++;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {}
            new javaX().setVisible(true);
        });
    }
}
