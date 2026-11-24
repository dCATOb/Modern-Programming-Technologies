public class Main {
    public static void main(String[] args) {
        Shape[] shapes = { new Circle(2), new Rectangle(3, 4) };
        for (Shape shape : shapes) {
            System.out.println("Площадь фигуры " + (shape instanceof Circle ? "Круг" : "Прямоугольник") + ": " + shape.area());
        }
    }
}

// Полиморфизм: каждый подкласс предоставляет собственную реализацию area().
abstract class Shape {
    abstract double area();
}

class Circle extends Shape {
    private final double radius;
    Circle(double radius) { this.radius = radius; }
    @Override double area() { return Math.PI * radius * radius; }
}

class Rectangle extends Shape {
    private final double width;
    private final double height;
    Rectangle(double width, double height) { this.width = width; this.height = height; }
    @Override double area() { return width * height; }
}
