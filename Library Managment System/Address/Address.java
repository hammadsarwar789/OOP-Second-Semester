package Address;

public class Address {
	private String street;
	private String city;

	public Address(String street, String city) {
        	this.street = street;
        	this.city = city;
    	}

    	public Address(Address other) {
        	this.street = other.street;
        	this.city = other.city;
    	}

    	public String getFullAddress() {
        	return "Street: " + street + ", City: " + city;
    	}

    	public String getStreet() {
        	return street;
    	}

    	public void setStreet(String street) {
        	this.street = street;
    	}

    	public String getCity() {
        	return city;
    	}

    	public void setCity(String city) {
        	this.city = city;
    	}
}
