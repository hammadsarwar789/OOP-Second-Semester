// ParkingZone.java
public class ParkingZone {
    private static int zoneCounter = 1;
    private String Id;
    private Vehicle[] vehicles = new Vehicle[5];
    private int count = 0;

    ParkingZone() {
        this.Id = "Z" + zoneCounter++;
    }

    public void addVehicle(Vehicle v) {
        if (count < 5 && v != null) {
            vehicles[count++] = v;
        }
    }

    @Override
    public String toString() {
        String result = "Parking Zone: " + Id + "\n";
        for (int i = 0; i < count; i++) {
            result += "  " + vehicles[i] + "\n";
        }
        return result;
    }
}
