import java.util.Scanner;

public class LeapYear {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Введите год: ");
            if (!scanner.hasNextInt()) throw new IllegalArgumentException("Год должен быть целым числом.");
            int year = scanner.nextInt();
            if (year <= 0) throw new IllegalArgumentException("Год должен быть положительным числом.");
            boolean leap = year % 400 == 0 || (year % 4 == 0 && year % 100 != 0);
            System.out.println(year + (leap ? " — високосный год." : " — невисокосный год."));
        } catch (IllegalArgumentException ex) {
            System.err.println("Неверный ввод: " + ex.getMessage());
        }
    }
}
