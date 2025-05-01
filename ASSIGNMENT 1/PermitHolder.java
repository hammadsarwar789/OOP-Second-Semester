// PermitHolder.java
public class PermitHolder extends Person {
    private static int idCounter = 1000;
    private int id;

    PermitHolder(String name) {
        super(name);
        this.id = idCounter++;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "[ Name: " + this.name + ", Permit ID: " + this.id + " ]";
    }
}
