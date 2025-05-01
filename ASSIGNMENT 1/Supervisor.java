// Supervisor.java
public class Supervisor extends Person {
    int yearOfExperence;

    Supervisor(String name, int yearOfExperence) {
        super(name);
        this.yearOfExperence = yearOfExperence;
    }

    @Override
    public String toString() {
        return "Supervisor: Name : " + this.name + ", Experence: " + this.yearOfExperence;
    }
}