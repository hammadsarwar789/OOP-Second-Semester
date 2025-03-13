import java.util.ArrayList;

class Campus{

	String name;
	String address;
	String city;
	ArrayList <Department>departments;

	public Campus(String name,String address, String city){

		this.name=name;
		this.address=address;
		this.city=city;
		this.departments=new ArrayList<>();
	}
	
	public void addDepartment(Department department){
	
		departments.add(department);
	}

    	public void displayCampus() {
        	System.out.println("Campus Name: " + name);
        	System.out.println("Address: " + address + ", City: " + city);
        	System.out.println("Departments: ");
        	
		for (Department dept : departments) {
            		
			System.out.println("  Department: " + dept.name + " (Incharge: " + dept.incharge.name + ")");
            	for (Lab lab : dept.labs) {
                	
			System.out.println("    Lab: " + lab.name + " (Address: " + lab.address + ")");
                	System.out.println("      Computers:");
                
		for (Computer comp : lab.computers) {
                    	
			System.out.println("        Core: " + comp.core + ", RAM: " + comp.ram + "GB, Disk: " + comp.diskSize + "GB");
				}
			}
		}
	}
}