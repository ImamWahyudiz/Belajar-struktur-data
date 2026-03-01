package stack;

import java.util.Scanner;

/**
 * Class Stack - Implementasi Stack menggunakan Array
 * Stack adalah struktur data LIFO (Last In First Out)
 */
class Stack {
    private String[] items;
    private int top;
    private int capacity;

    /**
     * Constructor untuk membuat stack dengan kapasitas tertentu
     */
    public Stack(int capacity) {
        this.capacity = capacity;
        this.items = new String[capacity];
        this.top = -1;
    }

    /**
     * Push item ke stack
     */
    public void push(String item) {
        if (isFull()) {
            throw new RuntimeException("Stack Overflow!");
        }
        items[++top] = item;
    }

    /**
     * Pop item dari stack
     */
    public String pop() {
        if (isEmpty()) {
            throw new RuntimeException("Stack Underflow!");
        }
        return items[top--];
    }

    /**
     * Peek item teratas tanpa menghapus
     */
    public String peek() {
        if (isEmpty()) {
            throw new RuntimeException("Stack is empty!");
        }
        return items[top];
    }

    /**
     * Cek apakah stack kosong
     */
    public boolean isEmpty() {
        return top == -1;
    }

    /**
     * Cek apakah stack penuh
     */
    public boolean isFull() {
        return top == capacity - 1;
    }

    /**
     * Mengembalikan isi stack sebagai string
     */
    public String getContent() {
        if (isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i <= top; i++) {
            sb.append(items[i]);
            if (i < top) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

/**
 * Class ExpressionEvaluator - Mengevaluasi ekspresi aritmatika
 * Mengkonversi infix ke postfix dan mengevaluasi hasilnya
 */
class ExpressionEvaluator {
    
    /**
     * Menentukan prioritas operator
     */
    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    /**
     * Cek apakah karakter adalah operator
     */
    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }

    /**
     * Konversi infix ke postfix dengan step by step
     */
    public String infixToPostfix(String infix) {
        StringBuilder result = new StringBuilder();
        Stack stack = new Stack(100);
        int stepNumber = 1;

        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          KONVERSI INFIX KE POSTFIX (Step by Step)                   ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Infix Expression: %-48s║%n", infix);
        System.out.println("╠═══════╦════════╦═══════════════════╦═══════════════════════════════╣");
        System.out.println("║ Step  ║ Simbol ║ Stack             ║ Postfix                       ║");
        System.out.println("╠═══════╬════════╬═══════════════════╬═══════════════════════════════╣");

        for (int i = 0; i < infix.length(); i++) {
            char ch = infix.charAt(i);

            // Skip whitespace
            if (ch == ' ') {
                continue;
            }

            // Jika operand (angka atau huruf), tambahkan ke result
            if (Character.isLetterOrDigit(ch)) {
                // Handle multi-digit numbers
                StringBuilder num = new StringBuilder();
                while (i < infix.length() && (Character.isLetterOrDigit(infix.charAt(i)) || infix.charAt(i) == '.')) {
                    num.append(infix.charAt(i));
                    i++;
                }
                i--; // Mundur satu karena loop akan increment
                
                result.append(num).append(" ");
                System.out.printf("║ %-5d ║ %-6s ║ %-17s ║ %-29s ║%n", 
                    stepNumber++, num, stack.getContent(), result.toString().trim());
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
            // Jika '(', push ke stack
            else if (ch == '(') {
                stack.push(String.valueOf(ch));
                System.out.printf("║ %-5d ║ %-6s ║ %-17s ║ %-29s ║%n", 
                    stepNumber++, ch, stack.getContent(), result.toString().trim());
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
            // Jika ')', pop sampai ketemu '('
            else if (ch == ')') {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.append(stack.pop()).append(" ");
                    System.out.printf("║ %-5d ║ %-6s ║ %-17s ║ %-29s ║%n", 
                        stepNumber++, "pop", stack.getContent(), result.toString().trim());
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop(); // Buang '('
                    System.out.printf("║ %-5d ║ %-6s ║ %-17s ║ %-29s ║%n", 
                        stepNumber++, ch, stack.getContent(), result.toString().trim());
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                }
            }
            // Jika operator
            else if (isOperator(ch)) {
                while (!stack.isEmpty() && !stack.peek().equals("(") && 
                       precedence(ch) <= precedence(stack.peek().charAt(0))) {
                    result.append(stack.pop()).append(" ");
                    System.out.printf("║ %-5d ║ %-6s ║ %-17s ║ %-29s ║%n", 
                        stepNumber++, "pop", stack.getContent(), result.toString().trim());
                    try { Thread.sleep(500); } catch (InterruptedException e) {}
                }
                stack.push(String.valueOf(ch));
                System.out.printf("║ %-5d ║ %-6s ║ %-17s ║ %-29s ║%n", 
                    stepNumber++, ch, stack.getContent(), result.toString().trim());
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
        }

        // Pop semua operator yang tersisa
        while (!stack.isEmpty()) {
            result.append(stack.pop()).append(" ");
            System.out.printf("║ %-5d ║ %-6s ║ %-17s ║ %-29s ║%n", 
                stepNumber++, "pop", stack.getContent(), result.toString().trim());
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        System.out.println("╚═══════╩════════╩═══════════════════╩═══════════════════════════════╝");
        
        return result.toString().trim();
    }

    /**
     * Evaluasi postfix expression dengan step by step
     */
    public double evaluatePostfix(String postfix) {
        Stack stack = new Stack(100);
        String[] tokens = postfix.split("\\s+");
        int stepNumber = 1;

        System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
        System.out.println("║          EVALUASI POSTFIX (Step by Step)                            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════════════╣");
        System.out.printf("║  Postfix Expression: %-45s║%n", postfix);
        System.out.println("╠═══════╦════════╦═══════════════════════════╦═══════════════════════╣");
        System.out.println("║ Step  ║ Token  ║ Operasi                   ║ Stack                 ║");
        System.out.println("╠═══════╬════════╬═══════════════════════════╬═══════════════════════╣");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            // Jika operand, push ke stack
            if (isNumeric(token)) {
                stack.push(token);
                System.out.printf("║ %-5d ║ %-6s ║ Push %-20s ║ %-21s ║%n", 
                    stepNumber++, token, token, stack.getContent());
                try { Thread.sleep(500); } catch (InterruptedException e) {}
            }
            // Jika operator, pop 2 operand, hitung, push hasil
            else if (token.length() == 1 && isOperator(token.charAt(0))) {
                if (stack.isEmpty()) {
                    throw new RuntimeException("Invalid expression!");
                }
                double operand2 = Double.parseDouble(stack.pop());
                
                if (stack.isEmpty()) {
                    throw new RuntimeException("Invalid expression!");
                }
                double operand1 = Double.parseDouble(stack.pop());
                
                double result = applyOperator(operand1, operand2, token.charAt(0));
                
                String operation = String.format("%.2f %s %.2f = %.2f", operand1, token, operand2, result);
                System.out.printf("║ %-5d ║ %-6s ║ %-25s ║ %-21s ║%n", 
                    stepNumber++, token, operation, "[" + result + "]");
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                
                stack.push(String.valueOf(result));
            }
        }

        System.out.println("╚═══════╩════════╩═══════════════════════════╩═══════════════════════╝");

        if (stack.isEmpty()) {
            throw new RuntimeException("Invalid expression!");
        }

        return Double.parseDouble(stack.pop());
    }

    /**
     * Cek apakah string adalah angka
     */
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Aplikasikan operator pada dua operand
     */
    private double applyOperator(double a, double b, char operator) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero!");
                }
                return a / b;
            case '^':
                return Math.pow(a, b);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}

/**
 * Class utama - Program Evaluasi Ekspresi Aritmatika
 */
public class javaX {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        
        while (true) {
            System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
            System.out.println("║          PROGRAM EVALUASI EKSPRESI ARITMATIKA (STACK)               ║");
            System.out.println("╠═══════════════════════════════════════════════════════════════════════╣");
            System.out.println("║  Operator yang didukung: +, -, *, /, ^, (, )                        ║");
            System.out.println("║  Contoh: 3 + 5 * 2  atau  (3 + 5) * 2                               ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
            
            System.out.print("\nMasukkan ekspresi infix (atau 'exit' untuk keluar): ");
            String infix = scanner.nextLine().trim();
            
            if (infix.equalsIgnoreCase("exit")) {
                System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
                System.out.println("║  Terima kasih telah menggunakan program ini!                        ║");
                System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
                break;
            }
            
            if (infix.isEmpty()) {
                System.out.println("✗ Error: Ekspresi tidak boleh kosong!");
                continue;
            }
            
            try {
                // Konversi infix ke postfix
                String postfix = evaluator.infixToPostfix(infix);
                
                System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                        HASIL KONVERSI                               ║");
                System.out.println("╠═══════════════════════════════════════════════════════════════════════╣");
                System.out.printf("║  Infix    : %-54s║%n", infix);
                System.out.printf("║  Postfix  : %-54s║%n", postfix);
                System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
                
                // Evaluasi postfix
                double result = evaluator.evaluatePostfix(postfix);
                
                System.out.println("\n╔═══════════════════════════════════════════════════════════════════════╗");
                System.out.println("║                        HASIL AKHIR                                  ║");
                System.out.println("╠═══════════════════════════════════════════════════════════════════════╣");
                System.out.printf("║  %s = %.2f%61s║%n", infix, result, "");
                System.out.println("╚═══════════════════════════════════════════════════════════════════════╝");
                
            } catch (Exception e) {
                System.out.println("\n✗ Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}
