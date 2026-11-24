public class Main {
    public static void main(String[] args) {
        Dog dog = new Dog("Мухтар");
        dog.eat();
        dog.speak();
    }
}

// Наследование: класс Dog — специализированный вариант общего класса Animal.
class Animal {
    private final String name;
    Animal(String name) { this.name = name; }
    void eat() { System.out.println(name + " ест."); }
}

class Dog extends Animal {
    Dog(String name) { super(name); }
    void speak() { System.out.println("Гав!"); }
}
