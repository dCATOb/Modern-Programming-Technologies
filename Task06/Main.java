import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Введите выражение (например: 2 * 6.5):");
            String expression = input.nextLine();
            try {
                System.out.println("Результат: " + Calculator.evaluate(expression));
            } catch (IllegalArgumentException ex) {
                System.err.println("Ошибка: " + ex.getMessage());
                System.exit(1);
            }
        }
    }
}

class Calculator {
    static double evaluate(String expression) {
        String[] parts = expression.trim().split("\\s+");
        if (parts.length != 3) throw new IllegalArgumentException("Формат: число оператор число");
        try {
            double left = Double.parseDouble(parts[0]);
            double right = Double.parseDouble(parts[2]);
            switch (parts[1]) {
                case "+": return left + right;
                case "-": return left - right;
                case "*": return left * right;
                case "/":
                    if (right == 0.0) throw new IllegalArgumentException("Делить на ноль нельзя");
                    return left / right;
                default: throw new IllegalArgumentException("Допустимые операторы: +, -, * или /");
            }
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Операндами должны быть числа", ex);
        }
    }
}
