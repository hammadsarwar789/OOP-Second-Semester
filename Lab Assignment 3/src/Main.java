import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Shape shape;
        Shape2D shape2D;
        Shape3D shape3D;
        Shape[] shapes = new Shape[20];
        
        Shape2D circle1 = new Circle("Circle", true, 5.0);
        System.out.println("Circle Area: " + circle1.area());
        
        shape2D = new Rectangle("Rectangle", true, 5.0, 5.0);
        System.out.println("Rectangle Area: " + shape2D.area());
        System.out.println("Rectangle Perimeter: " + ((Rectangle) shape2D).perimeter());
        
        Shape3D sphere1 = new Sphere("Sphere", true, 5.0);
        System.out.println("Sphere Volume: " + sphere1.volume());
        System.out.println("Sphere Area: " + ((Sphere) sphere1).area());
        
        Shape3D cube1 = new Cube("Cube", 3.0);
        System.out.println("Cube Volume: " + cube1.volume());
        System.out.println("Cube Area: " + ((Cube) cube1).area());
        
        // array
        shapes[0] = cube1;
        shapes[1] = circle1;
        shapes[2] = shape2D;
        shapes[3] = sphere1;
        shapes[4] = cube1;
        shapes[5] = circle1;
        shapes[6] = shape2D;
        shapes[7] = cube1;
        shapes[8] = sphere1;
        shapes[9] = cube1;
        shapes[10] = circle1;
        shapes[11] = sphere1;
        shapes[12] = shape2D;
        shapes[13] = sphere1;
        shapes[14] = cube1;
        shapes[15] = cube1;
        shapes[16] = circle1;
        shapes[17] = shape2D;
        shapes[18] = sphere1;
        shapes[19] = cube1;

        // After the shapes array
        ShapeUtils utils = new ShapeUtils();
        System.out.println("\nDisplaying all shapes:");
        utils.displayShapes(shapes);


        System.out.println("\nUpdating rectangle length:");
        utils.updatelenght(shape2D, 10.0);  // Updates length to 10.0
        System.out.println("\nDisplaying shapes after update:");
        utils.displayShapes(shapes);

        System.out.println("\nDrawing all shapes:");
        ShapeUtils.drawDrawable(shapes);  // shapes array can be used as Drawable array since Shape implements Drawable

        // intersection test
        System.out.println("\nTesting rectangle intersection:");
        Rectangle rect1 = new Rectangle("Rectangle 1", true, 5.0, 3.0);
        Rectangle rect2 = new Rectangle("Rectangle 2", true, 4.0, 2.0);
        

        rect2.topLeftCorner.setX(2);
        rect2.topLeftCorner.setY(2);
        
        System.out.println("Do rectangles intersect? " + rect1.intersects(rect2));
    }
}
