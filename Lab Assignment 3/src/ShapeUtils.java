public class ShapeUtils {
    public void displayShapes(Shape[] shapes) {
        for (Shape shape : shapes) {
            if (shape != null) {
                System.out.println("Shape Name: " + shape.getName());
                if (shape instanceof Shape2D) {
                    System.out.println("Area: " + ((Shape2D) shape).area());
                    System.out.println("Is Filled: " + ((Shape2D) shape).isFilled());
                } else if (shape instanceof Shape3D) {
                    System.out.println("Volume: " + ((Shape3D) shape).volume());
                }
                System.out.println();
            }
        }
    }

    public void updatelenght(Shape shape, double length) {
        if (shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) shape;
            rectangle.setLength(length);
            System.out.println("Rectangle length updated to: " + length);
            System.out.println("New area: " + rectangle.area());
            System.out.println("New perimeter: " + rectangle.perimeter());
        } else {
            System.out.println("This shape is not a Rectangle");
        }
    }

    public static void drawDrawable(Drawable[] drawable) {
        for (Drawable d : drawable) {
            if (d != null) {
                d.draw();
            }
        }
    }
}
