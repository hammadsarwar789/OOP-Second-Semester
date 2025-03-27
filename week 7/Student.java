class Student extends Person{

	private int rollNumber;
	private Classroom classroom;

	public Student(String name, int age, int rollNumber){

        	super(name, age);
        	this.rollNumber = rollNumber;
	}

	public void setClassroom(Classroom classroom){

		this.classroom = classroom;
	}

	public Classroom getClassroom(){

		return classroom;
	}

	@Override
	public boolean equals(Object obj){

		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;

		Student student = (Student) obj;
		return rollNumber == student.rollNumber;

	}

	@Override
	public String toString(){

        return super.toString() + ", Roll Number: " + rollNumber;

	}


}
