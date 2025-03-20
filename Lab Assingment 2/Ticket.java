class Ticket{

	int ticketId;
	int screenNumber;
	int seatNumber;
	Customer customer;
	String movieTitle;
	double price;
	
	public Ticket(int ticketId, Customer customer, int screenNumber, int seatNumber, String movieTitle, double price){

		this.ticketId=ticketId;
		this.screenNumber=screenNumber;
		this.seatNumber=seatNumber;
		this.customer=customer;
		this.movieTitle=movieTitle;
		this.price=price;


	}

	public void displayTicket(){

		System.out.println("Ticket ID: "+ ticketId + ", Movie: "+ movieTitle +", Screen: " + screenNumber +", Seat: " + seatNumber +", Price: "+ price);

        	customer.displayCustomer();
	
	}

}
