class Principal extends Person{

	private int experienceYears;

	public Principal(String name, int age, int experienceYears){

        	super(name, age);
        if (experienceYears < 0){

            System.out.println("Experience cannot be negative");
            return;
        }

        this.experienceYears = experienceYears;
	}

	@Override
	public String toString(){

        return super.toString() + ", Experience: " + experienceYears + " years";
	}


}