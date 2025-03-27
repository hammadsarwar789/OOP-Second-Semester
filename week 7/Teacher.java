class Teacher extends Person{

	private String subject;
	private int teacherID;
	private Classroom assignedClassroom;

	public Teacher(String name, int age, String subject, int teacherID){

        	super(name, age);
        	this.subject = subject;
        	this.teacherID = teacherID;
        	this.assignedClassroom = null;
    
	}

	public boolean assignToClassroom(Classroom classroom){

        	if (assignedClassroom != null){

            	System.out.println("Teacher is already assigned to another classroom");
            	return false;
        }

        this.assignedClassroom = classroom;
        return true;
	
	}

	public void removeFromClassroom(){

		this.assignedClassroom = null;
	}

	@Override
	public boolean equals(Object obj){

        	if (this == obj) return true;
        	if (obj == null || getClass() != obj.getClass()) return false;
        	Teacher teacher = (Teacher) obj;
        	return teacherID == teacher.teacherID;


    }

	@Override
	public String toString(){

        return super.toString() + ", Subject: " + subject + ", Teacher ID: " + teacherID;
	}
}