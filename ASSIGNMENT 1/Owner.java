// Owner.java
public class Owner extends Person {
    private static int counter = 1;
    String Id;

    Owner(String name) {
        super(name);
        this.Id = String.format("O%03d", counter++);
    }

    @Override
    public String toString() {
        return "Owner : [ Name: " + this.name + ", Owner Id: " + this.Id + " ]";
    }
}