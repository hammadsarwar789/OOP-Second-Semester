public class DatePractice{

    	public static void main(String[] args) {
        	Date date = new Date(27, 2, 2025);
        	displayDate(date);
    	}
	public void displayDate(Date d) {
        	System.out.println(d.getDay() + "/" + d.getMonth() + "/" + d.getYear());
    	}
}