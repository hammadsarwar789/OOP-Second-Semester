public class Rectangle extends Shape2D{
    private double length;
    private double width;
    Point topLeftCorner= new Point(0, 0);
    public Rectangle(String name, boolean isFilled, double length, double width) {
        super(name, isFilled);
        this.length = length;
        this.width = width;
    }


    public double area() {
        return length * width;
    }
    public double perimeter() {
        return 2 * (length + width);
    }
    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }


    public void draw() {
        System.out.println("Drawing Rectangle");
    }

    public boolean intersects(Rectangle other) {
        // Get the coordinates of both rectangles
        double x1 = this.topLeftCorner.getX();
        double y1 = this.topLeftCorner.getY();
        double x2 = x1 + this.width;
        double y2 = y1 + this.length;
        
        double ox1 = other.topLeftCorner.getX();
        double oy1 = other.topLeftCorner.getY();
        double ox2 = ox1 + other.width;
        double oy2 = oy1 + other.length;
        

        if (x2 < ox1 || ox2 < x1) {
            return false;
        }
        

        if (y2 < oy1 || oy2 < y1) {
            return false;
        }
        
        return true;
    }
}
