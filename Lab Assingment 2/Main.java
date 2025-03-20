public class Main {

	public static void main(String[] args) {

        	Cinema cinema = new Cinema("Naz Cinema", "Lahore", 2);
        	cinema.displayCinema();

        	Screen screen1 = new Screen(1, "Scam: 1992", 3, 3);
        	Screen screen2 = new Screen(2, "Razz", 3, 3);
        
        	cinema.addScreen(0, screen1);
        	cinema.addScreen(1, screen2);


        	screen1.displayScreen();
        	screen2.displayScreen();


        	Seat seat1 = new Seat(1, 1, SeatType.VIP, 1000.0);
        	Seat seat2 = new Seat(2, 1, SeatType.REGULAR, 700.0);


        	screen1.seats[0][0] = seat1;
        	screen1.seats[0][1] = seat2;


        	seat1.display();
        	seat2.display();
		
		System.out.println("-------------------------------------------------");

		Customer customer1 = new Customer(172, "Hammad Sarwar", "03304223123", "mhammadsarwar@example.com");
		customer1.displayCustomer();

		boolean booked1 = screen1.bookSeat(0, 0, customer1);
		System.out.println("Seat booked: " + booked1);

		if (booked1) {
			Ticket ticket1 = new Ticket(1, customer1, 1, 1, "Scam: 1992", 1000.0);
			ticket1.displayTicket();
		}

		System.out.println("-------------------------------------------------");


        	Customer customer2 = new Customer(173, "Ahmad", "03398423327", "ahmad@example.com");
        	customer2.displayCustomer();

        	boolean booked2 = screen1.bookSeat(0, 1, customer2);
        	System.out.println("Seat booked: " + booked2);

        	if (booked2) {
            		Ticket ticket2 = new Ticket(2, customer2, 1, 1, "Scam: 1992", 700.0);
            		ticket2.displayTicket();
        	} 
		else {
            
			System.out.println("Customer " + customer2.name + " could not book seat 1, as it is already taken.");
        
		}

        	System.out.println("-------------------------------------------------");


        	Customer customer3 = new Customer(174, "Zain Ali", "03211234567", "zainali@example.com");
        	customer3.displayCustomer();

        	boolean booked3 = screen1.bookSeat(0, 1, customer3); // Booking seat 2
        	System.out.println("Seat booked: " + booked3);

        	if (booked3) {

            		Ticket ticket3 = new Ticket(3, customer3, 1, 2, "Scam: 1992", 700.0);
            		ticket3.displayTicket();
        	}

	}
}
