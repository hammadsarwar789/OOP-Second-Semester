class Person{

	protected String name;
	protected int age;

	public Person(String name,int age){

        	if (name == null || name.isEmpty()) {

            		System.out.println("Name cannot be null or empty");
            	return;
        }

        	if (age < 0) {
            		System.out.println("Age cannot be negative");

            	return;
        
	}

        this.name = name;
        this.age = age;
	}

    	public String getName(){
		return name;
	}

    	public int getAge(){
		return age;
	}

	@Override
	public String toString(){

        return "Name: " + name + ", Age: " + age;

	}

}