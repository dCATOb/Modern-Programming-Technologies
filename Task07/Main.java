import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Сложные проценты: 1) будущая сумма  2) начальный капитал");
            int mode = input.nextInt();
            if (mode == 1) {
                System.out.print("Начальный капитал, годовая ставка (%), начислений в год, срок в годах: ");
                double principal = input.nextDouble();
                double rate = input.nextDouble() / 100.0;
                int frequency = input.nextInt();
                double years = input.nextDouble();
                System.out.printf("Будущая сумма: %.2f%n", CompoundInterest.futureValue(principal, rate, frequency, years));
            } else if (mode == 2) {
                System.out.print("Целевая сумма, годовая ставка (%), начислений в год, срок в годах: ");
                double target = input.nextDouble();
                double rate = input.nextDouble() / 100.0;
                int frequency = input.nextInt();
                double years = input.nextDouble();
                System.out.printf("Необходимый начальный капитал: %.2f%n", CompoundInterest.presentValue(target, rate, frequency, years));
            } else {
                throw new IllegalArgumentException("Выберите 1 или 2");
            }
        } catch (IllegalArgumentException ex) {
            System.err.println("Ошибка: " + ex.getMessage());
            System.exit(1);
        }
    }
}

class CompoundInterest {
    // A = P(1 + r/m)^(m*t), где r — годовая ставка в десятичном виде.
    static double futureValue(double principal, double annualRate, int compoundsPerYear, double years) {
        validate(principal, annualRate, compoundsPerYear, years);
        return principal * Math.pow(1 + annualRate / compoundsPerYear, compoundsPerYear * years);
    }

    // Преобразование той же формулы для вычисления P.
    static double presentValue(double target, double annualRate, int compoundsPerYear, double years) {
        validate(target, annualRate, compoundsPerYear, years);
        return target / Math.pow(1 + annualRate / compoundsPerYear, compoundsPerYear * years);
    }

    private static void validate(double amount, double rate, int frequency, double years) {
        if (amount < 0 || rate < 0 || frequency <= 0 || years < 0)
            throw new IllegalArgumentException("Сумма, ставка и срок должны быть неотрицательными; число начислений должно быть положительным");
    }
}
