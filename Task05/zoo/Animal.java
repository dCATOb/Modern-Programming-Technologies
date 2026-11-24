package zoo;

public abstract class Animal {
    private final String name;
    protected Animal(String name) { this.name = name; }
    public String getName() { return name; }
    public abstract String sound();
    public void describe() { System.out.println(getName() + " " + sound()); }
}
