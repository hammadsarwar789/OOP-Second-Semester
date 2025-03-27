import java.util.ArrayList;

class Classroom {
 	
	private String className;
	private String classCode;
	private Teacher teacher;
	private Student[] students;
	private int studentCount;

	public Classroom(String className, String classCode, Teacher teacher){

		this.className = className;
		this.classCode = classCode;

        if (!teacher.assignToClassroom(this)){

            	System.out.println("Cannot assign teacher to this classroom");

        }

        this.teacher = teacher;
        this.students = new Student[5];
        this.studentCount = 0;
    }

	public boolean addStudent(Student student){

		if (studentCount < 5){

			students[studentCount++] = student;
			student.setClassroom(this);

            		return true;
        	}
	
		else{

            System.out.println("Class is full! Cannot enroll " + student.getName());
            return false;
        	}
	}

	@Override
	public String toString(){

		String result = "\nClass: " + className + " (" + classCode + ")\n";
		result += "Teacher: " + teacher + "\n";
		result += "Students: [";

        	for (int i = 0; i < studentCount; i++){

		result += students[i];

	if (i < studentCount - 1) result += ", ";
        }

        result += "]";
        return result;

	}

}
