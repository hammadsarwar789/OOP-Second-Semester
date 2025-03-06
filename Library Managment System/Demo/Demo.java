package Demo;

import Person.Person;
import Book.Book;
import Address.Address;
import Date.Date;

public class Demo {
	private String name;
	private Book book;
	private Person incharge;
	private Person staff;

	public Demo(String name, Book book, Person incharge, Person staff) {
        	this.name = name;
        	this.book = new Book(book);
        	this.incharge = new Person(incharge);
        	this.staff = new Person(staff);
    	}

    	public void showLibraryDetails() {
        	System.out.println("Library: " + name);
        	System.out.println("Book Details:");
        	book.showBookDetails();
        	System.out.println("Incharge Details:");
        	incharge.showPersonDetails();
        	System.out.println("Staff Details:");
        	staff.showPersonDetails();
    	}

    	public static void main(String[] args) {
        	Address authorAddress = new Address("Dream Garden", "Lahore");
        	Person author = new Person("Hammad", "Writer", authorAddress);
        	Date pubDate = new Date(6, 3, 2025);
        	Book book = new Book("Java Programming", "12345", pubDate, author);

        	Address inchargeAddress = new Address("2nd Avenue", "Lahore");
        	Person incharge = new Person("Zain", "Manager", inchargeAddress);

        	Address staffAddress = new Address("3rd Street", "Lahore");
        	Person staff = new Person("ALi", "Librarian", staffAddress);

        	Demo library = new Demo("Comsats Library", book, incharge, staff);
        	library.showLibraryDetails();
    	}
}
