class Seat{

	int seatNumber;
	int rowNumber;
	SeatType type;
	double price;
	boolean isBooked;

	public Seat(int seatNumber, int rowNumber, SeatType type, double price){

		this.seatNumber = seatNumber;
		this.rowNumber = rowNumber;
		this.type = type;
		this.price = price;
		this.isBooked = false;

	}

	public boolean bookSeat() {

		if (!isBooked) {

			isBooked = true;
			return true;

		}
		return false;

	}

	public void display() {

		System.out.println("Seat: " + seatNumber + " Row: " + rowNumber + " Type: " + type + " Price: " + price + " Booked: " + isBooked);
	}
}
