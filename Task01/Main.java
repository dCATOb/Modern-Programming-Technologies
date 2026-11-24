public class Main {
    public static void main(String[] args) {
        BankAccount account = new BankAccount("Алексей", 100.00);
        account.deposit(25.50);
        System.out.println("Владелец: " + account.getOwner() + ", баланс: " + account.getBalance() + " руб.");
    }
}

// Инкапсуляция: состояние закрыто и может изменяться только через методы с проверкой данных.
class BankAccount {
    private final String owner;
    private double balance;

    BankAccount(String owner, double openingBalance) {
        if (openingBalance < 0) throw new IllegalArgumentException("Начальный баланс не может быть отрицательным");
        this.owner = owner;
        this.balance = openingBalance;
    }

    String getOwner() { return owner; }
    double getBalance() { return balance; }

    void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Сумма пополнения должна быть положительной");
        balance += amount;
    }
}
