package zoo;

public class Parrot extends Bird {
    public Parrot(String name) { super(name); }
    @Override public String sound() { return "чирикает"; }
}
