public class PersonPractice{

	public static void main(String args[]){
	
	Detail obj1=new Detail("Hammad","Sarwar","Male","asdas");
	Detail obj2=new Detail("FA24-BCS-068","9/11/2004",21);
	Detail obj3=new Detail("mhammadsarwar789@gmail.com","Faisalabad","A+","Comsats LHR");
	System.out.println("First Constructor");
	Print(obj1);
	System.out.println("Second Constructor");
	Print(obj2);
	System.out.println("Third Constructor");
	Print(obj3);


	}
	public static void Print(Detail d){
		System.out.println(d.name);
		System.out.println(d.fatherName);
		System.out.println(d.gender);
		System.out.println(d.regNo);
		System.out.println(d.dateOfBirth);
		System.out.println(d.age);
		System.out.println(d.email);
		System.out.println(d.address);
		System.out.println(d.grade);
		System.out.println(d.institute);
	}
}