public class Sphere extends Shape3D {
    private double radius;
    private boolean isFilled;

    public Sphere(String name, boolean isFilled, double radius) {
        super(name);
        this.isFilled = isFilled;
        this.radius = radius;
    }

    public double volume() {
        return (4.0 / 3.0) * Math.PI * radius * radius * radius;
    }

    public double area() {
        return 4 * Math.PI * radius * radius;
    }

    public boolean isFilled() {
        return isFilled;
    }
    public void draw() {
        System.out.println("Drawing a sphere");
    }
}

