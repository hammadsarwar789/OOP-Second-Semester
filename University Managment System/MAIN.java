public class MAIN{

	public static void main(String args[]){
		
		Campus campus= new Campus("CUI Lahore","Defence Road","Lahpre");
		
		for(int i=1; i<=10; i++){
			
			Department department= new Department("Department" + i, new Incharge("Incharge" + i));
		
		for(int j=1;j<=5;j++){

			Lab lab= new Lab( "Building" + i,"Lab" + j);
			lab.addComputer(new Computer("i5", 8, 256));
                	lab.addComputer(new Computer("i7", 16, 512));
                	department.addLab(lab);

			}

		 	campus.addDepartment(department);
		}
			campus.displayCampus();
	}
}