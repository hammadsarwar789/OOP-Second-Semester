public class Detail{

	String name;
	String fatherName;
	String gender;
	String dateOfBirth;
	String regNo;
	int age;
	String email;
	String address;
	String grade;
	String institute;
	
	Detail(String name,String fatherName,String gender){
		this.name=name;
		this.fatherName=fatherName;
		this.gender=gender;
	}
	Detail(String regNo,String dateOfBirth,int age){
		this.regNo=regNo;
		this.dateOfBirth=dateOfBirth;
		this.age=age;
		this.email=email;
	}
	Detail(String email,String address,String grade,String institute){
		this.email=email;
		this.address=address;
		this.grade=grade;
		this.institute=institute;

	}
}