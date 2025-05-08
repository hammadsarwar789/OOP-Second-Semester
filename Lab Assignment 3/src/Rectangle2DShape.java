public class Rectangle2DShape extends Shape2D {
    private double length;
    private double width;
    private double height;

    public Rectangle2DShape(String name, boolean isFilled, double length, double width, double height) {
        super(name, isFilled);
        this.length = length;
        this.width = width;
        this.height = height;
    }

    public double area() {
        return 2 * (length * width + width * height + height * length);
    }

    public double perimeter() {
        return 4 * (length + width + height);
    }

    public void draw() {
        System.out.println("Drawing a 3D rectangle");
    }
}
