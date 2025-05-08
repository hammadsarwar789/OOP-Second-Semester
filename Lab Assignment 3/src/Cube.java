public class Cube extends Shape3D {
    private double side;


    public Cube(String name, double side) {
        super(name);
        this.side = side;
    }

    public double volume() {
        return side * side * side;
    }

    public double area() {
        return 6 * side * side;
    }


    public void draw() {
        System.out.println("Drawing a cube");
    }
}
