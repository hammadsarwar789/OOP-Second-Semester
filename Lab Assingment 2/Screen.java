class Screen{

	int screenNumber;
	String movieTitle;
	Seat [][] seats;


	public Screen(int screenNumber, String movieTitle, int rows, int cols){

		this.screenNumber=screenNumber;
        	this.movieTitle=movieTitle;
		seats=new Seat[rows][cols];

	}
	
	public boolean bookSeat(int row, int col, Customer customer) {

    		if (seats[row][col] != null) {
        		if (!seats[row][col].isBooked) {
            		return seats[row][col].bookSeat();
        	} 

		else {
            		System.out.println("Seat " + (col + 1) + " in row " + (row + 1) + " is already booked.");
        		}
    		}
	 
		else {

        		System.out.println("Seat does not exist.");
			}

    			return false;
		}

	
		public void displayScreen(){

        	System.out.println("Screen Number: "+ screenNumber+ ", Movie: "+ movieTitle);

	}

}