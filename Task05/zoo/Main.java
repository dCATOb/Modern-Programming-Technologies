package zoo;

public class Main {
    public static void main(String[] args) {
        Animal[] residents = { new Lion("Лев"), new Parrot("Кеша") };
        for (Animal animal : residents) animal.describe();
    }
}
