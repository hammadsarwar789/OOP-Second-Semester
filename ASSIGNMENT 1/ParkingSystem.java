// ParkingSystem.java
import java.util.ArrayList;

public class ParkingSystem {
    private static ParkingSystem instance = null;

    private String campus;
    private Supervisor supervisor;
    private ArrayList<ParkingZone> zones = new ArrayList<>();
    private ArrayList<PermitHolder> permitHolders = new ArrayList<>();

    private ParkingSystem(String campus, Supervisor supervisor) {
        if (supervisor == null) {
            throw new IllegalArgumentException("System must have a supervisor assigned.");
        }
        this.campus = campus;
        this.supervisor = supervisor;
    }

    public static ParkingSystem getInstance(String campus, Supervisor supervisor) {
        if (instance == null) {
            instance = new ParkingSystem(campus, supervisor);
        } else {
            System.out.println("Warning: ParkingSystem instance already exists. Returning existing instance.");
        }
        return instance;
    }

    public void addZone(ParkingZone z) {
        zones.add(z);
    }

    public void addPermitHolder(PermitHolder p) {
        permitHolders.add(p);
    }

    @Override
    public String toString() {
        String output = "Campus: " + campus + "\n" + supervisor + "\n";
        for (ParkingZone z : zones) {
            output += z.toString();
        }
        output += "Permit Holders:\n";
        for (PermitHolder p : permitHolders) {
            output += p.toString() + "\n";
        }
        return output;
    }
}
