// Vehicle
public class Vehicle {
    private static String[] existingPlates = new String[100];
    private static int plateCount = 0;

    private String plate;
    private String type;
    private Owner owner;

    Vehicle(String plate, String type, Owner owner) {
        if (isDuplicatePlate(plate)) {
            System.out.println("Error: Duplicate license plate " + plate + " is not allowed.");
            return;
        }
        this.plate = plate;
        this.type = type;
        this.owner = owner;
        existingPlates[plateCount++] = plate;
    }

    private boolean isDuplicatePlate(String plate) {
        for (int i = 0; i < plateCount; i++) {
            if (existingPlates[i].equals(plate)) {
                return true;
            }
        }
        return false;
    }

    
    public Vehicle shallowCopy() {   // Shallow copy
        return new Vehicle(this.plate + "_COPY", this.type, this.owner);
    }

    
    public Vehicle deepCopy() {     // Deep copy
        Owner newOwner = new Owner(this.owner.name);
        return new Vehicle(this.plate + "_COPY", this.type, newOwner);
    }

    public Owner getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return "Vehicle: [ License plate: " + this.plate + ", Type: " + this.type + ", " +
                (owner != null ? owner.toString() : "No owner") + " ]";
    }

}
