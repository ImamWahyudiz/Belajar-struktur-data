#!/usr/bin/env python3
"""
Program Evaluasi Ekspresi Aritmatika menggunakan Stack
Mengkonversi infix ke postfix dan mengevaluasi hasilnya dengan step by step
"""


class Stack:
    """
    Class Stack - Implementasi Stack menggunakan List
    Stack adalah struktur data LIFO (Last In First Out)
    """
    
    def __init__(self, capacity=100):
        """
        Constructor untuk membuat stack dengan kapasitas tertentu
        """
        self.capacity = capacity
        self.items = []
    
    def push(self, item):
        """
        Push item ke stack
        """
        if self.is_full():
            raise OverflowError("Stack Overflow!")
        self.items.append(item)
    
    def pop(self):
        """
        Pop item dari stack
        """
        if self.is_empty():
            raise IndexError("Stack Underflow!")
        return self.items.pop()
    
    def peek(self):
        """
        Peek item teratas tanpa menghapus
        """
        if self.is_empty():
            raise IndexError("Stack is empty!")
        return self.items[-1]
    
    def is_empty(self):
        """
        Cek apakah stack kosong
        """
        return len(self.items) == 0
    
    def is_full(self):
        """
        Cek apakah stack penuh
        """
        return len(self.items) >= self.capacity
    
    def get_content(self):
        """
        Mengembalikan isi stack sebagai string
        """
        if self.is_empty():
            return "[]"
        return str(self.items)


class ExpressionEvaluator:
    """
    Class ExpressionEvaluator - Mengevaluasi ekspresi aritmatika
    Mengkonversi infix ke postfix dan mengevaluasi hasilnya
    """
    
    def precedence(self, operator):
        """
        Menentukan prioritas operator
        """
        if operator in ('+', '-'):
            return 1
        elif operator in ('*', '/'):
            return 2
        elif operator == '^':
            return 3
        else:
            return -1
    
    def is_operator(self, ch):
        """
        Cek apakah karakter adalah operator
        """
        return ch in ['+', '-', '*', '/', '^']
    
    def infix_to_postfix(self, infix):
        """
        Konversi infix ke postfix dengan step by step
        """
        result = []
        stack = Stack()
        step_number = 1
        
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║          KONVERSI INFIX KE POSTFIX (Step by Step)         ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print(f"  Infix Expression: {infix}")
        print("────────────────────────────────────────────────────────────")
        print("  Step | Simbol | Stack           | Postfix")
        print("────────────────────────────────────────────────────────────")
        
        i = 0
        while i < len(infix):
            ch = infix[i]
            
            # Skip whitespace
            if ch == ' ':
                i += 1
                continue
            
            # Jika operand (angka atau huruf), tambahkan ke result
            if ch.isalnum() or ch == '.':
                # Handle multi-digit numbers
                num = []
                while i < len(infix) and (infix[i].isalnum() or infix[i] == '.'):
                    num.append(infix[i])
                    i += 1
                i -= 1  # Mundur satu karena loop akan increment
                
                num_str = ''.join(num)
                result.append(num_str)
                print(f"  {step_number:<4} | {num_str:<6} | {stack.get_content():<15} | {' '.join(result)}")
                step_number += 1
            
            # Jika '(', push ke stack
            elif ch == '(':
                stack.push(ch)
                print(f"  {step_number:<4} | {ch:<6} | {stack.get_content():<15} | {' '.join(result)}")
                step_number += 1
            
            # Jika ')', pop sampai ketemu '('
            elif ch == ')':
                while not stack.is_empty() and stack.peek() != '(':
                    result.append(stack.pop())
                    print(f"  {step_number:<4} | {'pop':<6} | {stack.get_content():<15} | {' '.join(result)}")
                    step_number += 1
                if not stack.is_empty() and stack.peek() == '(':
                    stack.pop()  # Buang '('
                    print(f"  {step_number:<4} | {ch:<6} | {stack.get_content():<15} | {' '.join(result)}")
                    step_number += 1
            
            # Jika operator
            elif self.is_operator(ch):
                while (not stack.is_empty() and 
                       stack.peek() != '(' and 
                       self.precedence(ch) <= self.precedence(stack.peek())):
                    result.append(stack.pop())
                    print(f"  {step_number:<4} | {'pop':<6} | {stack.get_content():<15} | {' '.join(result)}")
                    step_number += 1
                stack.push(ch)
                print(f"  {step_number:<4} | {ch:<6} | {stack.get_content():<15} | {' '.join(result)}")
                step_number += 1
            
            i += 1
        
        # Pop semua operator yang tersisa
        while not stack.is_empty():
            result.append(stack.pop())
            print(f"  {step_number:<4} | {'pop':<6} | {stack.get_content():<15} | {' '.join(result)}")
            step_number += 1
        
        print("╚════════════════════════════════════════════════════════════╝")
        
        return ' '.join(result)
    
    def evaluate_postfix(self, postfix):
        """
        Evaluasi postfix expression dengan step by step
        """
        stack = Stack()
        tokens = postfix.split()
        step_number = 1
        
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║          EVALUASI POSTFIX (Step by Step)                  ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print(f"  Postfix Expression: {postfix}")
        print("────────────────────────────────────────────────────────────")
        print("  Step | Token  | Operasi           | Stack")
        print("────────────────────────────────────────────────────────────")
        
        for token in tokens:
            if not token:
                continue
            
            # Jika operand, push ke stack
            if self.is_numeric(token):
                stack.push(token)
                print(f"  {step_number:<4} | {token:<6} | Push {token:<13} | {stack.get_content()}")
                step_number += 1
            
            # Jika operator, pop 2 operand, hitung, push hasil
            elif len(token) == 1 and self.is_operator(token):
                if stack.is_empty():
                    raise ValueError("Invalid expression!")
                operand2 = float(stack.pop())
                
                if stack.is_empty():
                    raise ValueError("Invalid expression!")
                operand1 = float(stack.pop())
                
                result = self.apply_operator(operand1, operand2, token)
                
                print(f"  {step_number:<4} | {token:<6} | {operand1:.2f} {token} {operand2:.2f} = {result:.2f} | [{result}]")
                step_number += 1
                
                stack.push(str(result))
        
        print("╚════════════════════════════════════════════════════════════╝")
        
        if stack.is_empty():
            raise ValueError("Invalid expression!")
        
        return float(stack.pop())
    
    def is_numeric(self, s):
        """
        Cek apakah string adalah angka
        """
        try:
            float(s)
            return True
        except ValueError:
            return False
    
    def apply_operator(self, a, b, operator):
        """
        Aplikasikan operator pada dua operand
        """
        if operator == '+':
            return a + b
        elif operator == '-':
            return a - b
        elif operator == '*':
            return a * b
        elif operator == '/':
            if b == 0:
                raise ZeroDivisionError("Division by zero!")
            return a / b
        elif operator == '^':
            return a ** b
        else:
            raise ValueError(f"Invalid operator: {operator}")


def main():
    """
    Function main - entry point program
    """
    evaluator = ExpressionEvaluator()
    
    while True:
        print("\n╔════════════════════════════════════════════════════════════╗")
        print("║      PROGRAM EVALUASI EKSPRESI ARITMATIKA (STACK)         ║")
        print("╠════════════════════════════════════════════════════════════╣")
        print("║  Operator yang didukung: +, -, *, /, ^, (, )              ║")
        print("║  Contoh: 3 + 5 * 2  atau  (3 + 5) * 2                     ║")
        print("╚════════════════════════════════════════════════════════════╝")
        
        infix = input("\nMasukkan ekspresi infix (atau 'exit' untuk keluar): ").strip()
        
        if infix.lower() == 'exit':
            print("\n╔════════════════════════════════════════════════════════════╗")
            print("║  Terima kasih telah menggunakan program ini!               ║")
            print("╚════════════════════════════════════════════════════════════╝")
            break
        
        if not infix:
            print("✗ Error: Ekspresi tidak boleh kosong!")
            continue
        
        try:
            # Konversi infix ke postfix
            postfix = evaluator.infix_to_postfix(infix)
            
            print("\n╔════════════════════════════════════════════════════════════╗")
            print("║                    HASIL KONVERSI                          ║")
            print("╠════════════════════════════════════════════════════════════╣")
            print(f"  Infix    : {infix}")
            print(f"  Postfix  : {postfix}")
            print("╚════════════════════════════════════════════════════════════╝")
            
            # Evaluasi postfix
            result = evaluator.evaluate_postfix(postfix)
            
            print("\n╔════════════════════════════════════════════════════════════╗")
            print("║                    HASIL AKHIR                             ║")
            print("╠════════════════════════════════════════════════════════════╣")
            print(f"  {infix} = {result:.2f}")
            print("╚════════════════════════════════════════════════════════════╝")
            
        except Exception as e:
            print(f"\n✗ Error: {e}")


if __name__ == "__main__":
    main()
