public class Circle extends Shape2D {
    private double radius;
    Point center= new Point(0, 0);
    public Circle(String name, boolean isFilled, double radius) {
        super(name, isFilled);
        this.radius = radius;
    }


    public double area() {
        return Math.PI * radius * radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
    public void draw() {
        System.out.println("Drawing a circle ");
    }
}

