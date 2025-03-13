import java.util.ArrayList;

class Lab{
	
	String name;
	String address;
	ArrayList <Computer>computers;
	
	public Lab(String name, String address){

		this.name=name;
		this.address=address;
		this.computers=new ArrayList<>();
	}
	
	public void addComputer(Computer computer){
	
		computers.add(computer);
	}
}