import java.util.ArrayList;

class Department{

	String name;
	Incharge incharge;
	ArrayList <Lab> labs;

	public Department(String name,Incharge incharge){
		
		this.name=name;
		this.incharge=incharge;
		this.labs=new ArrayList<>();
	}
	
	public void addLab(Lab lab){
		labs.add(lab);
	}
}