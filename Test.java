public class Test{

	public static void main(String agr[]){
		
		Person p1 = new Person();
		
		p1.id=1;
		p1.name="Hammad";
		p1.age=21;
		System.out.println(Person.age);
		printData(p1);	

	}
	public static void printData(Person p){
		System.out.println(p.id);
		System.out.println(p.name);
		System.out.println(p.age);
	}
	
}