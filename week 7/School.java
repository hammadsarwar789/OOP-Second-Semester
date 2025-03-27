import java.util.ArrayList;

class School{

	private String name;
	private String address;
	private Principal principal;
	private ArrayList<Classroom> classrooms;

	public School(String name, String address, Principal principal){

        	this.name = name;
        	this.address = address;
        	this.principal = principal;
        	this.classrooms = new ArrayList<>();
	}

	public void addClassroom(Classroom classroom){

		classrooms.add(classroom);
	}

	@Override
	public String toString(){

        String result = "School: " + name + "\n";
        result += "Address: " + address + "\n";
        result += "Principal: " + principal + "\n";
        result += "Classrooms: " + classrooms + "\n";

        return result;

	}
}