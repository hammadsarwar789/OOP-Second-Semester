public abstract class Shape2D extends Shape {
    private boolean isFilled;

    public Shape2D(String name, boolean isFilled) {
        super(name);
        this.isFilled = isFilled; // Fixed assignment
    }

    public boolean isFilled() {
        return isFilled;
    }

    public abstract double area(); // Added abstract method
}
