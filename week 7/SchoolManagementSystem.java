public class SchoolManagementSystem{


	public static void main(String[] args){

        	Principal principal = new Principal("Hamza", 40, 15);
        	School school = new School("Comsats Lhr", "Defence road", principal);

        	Teacher teacher1 = new Teacher("Fateh Ali", 32, "Math", 101);
        	Teacher teacher2 = new Teacher("Shahid Bhatti", 40, "OOP", 102);

        	Classroom class1 = new Classroom("First Semmester", "C3", teacher1);
        	Classroom class2 = new Classroom("Sec Semmester", "C8", teacher2);
        
        	school.addClassroom(class1);
        	school.addClassroom(class2);

        	Student s1 = new Student("Zain", 21, 1);
        	Student s2 = new Student("Ammar", 20, 2);
        	Student s3 = new Student("Umer", 21, 3);
        	Student s4 = new Student("Hashim", 22, 4);
        	Student s5 = new Student("Abdullah", 20, 5);
        	Student s6 = new Student("Moiz", 21, 6);
        
        	class1.addStudent(s1);
        	class1.addStudent(s2);
        	class1.addStudent(s3);
        	class1.addStudent(s4);
        	class1.addStudent(s5);
        	class1.addStudent(s6);
	
		Student s7 = new Student("Zain", 21, 1);
        	Student s8 = new Student("Ammar", 20, 2);
        	Student s9 = new Student("Umer", 21, 3);
        	Student s10 = new Student("Hashim", 22, 4);
		Student s11 = new Student("Zain", 21, 1);

		class2.addStudent(s7);
        	class2.addStudent(s8);
        	class2.addStudent(s9);
        	class2.addStudent(s10);
		class2.addStudent(s11);

		System.out.println(school);
	
	}

}
