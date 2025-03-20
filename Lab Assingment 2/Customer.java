class Customer {

	int customerId;
	String name;
	String phoneNumber;
	String email;

    	public Customer(int customerId, String name, String phoneNumber, String email) {

        	this.customerId = customerId;
        	this.name = name;
        	this.phoneNumber = phoneNumber;
        	this.email = email;
    		
		}

		public boolean equalsObj(Object obj) {
        	
			if (this == obj) return true;
        		if (obj == null || getClass() != obj.getClass()) return false;
        		Customer customer = (Customer) obj;
        		return customerId == customer.customerId;
		
		}

    		public void displayCustomer() {
        		
			System.out.println("Customer ID: " + customerId +", Name: " + name +", Phone: " + phoneNumber + ", Email: " + email);

		}


}