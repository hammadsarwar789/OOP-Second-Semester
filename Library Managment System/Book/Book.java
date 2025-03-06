package Book;
import Date.Date;
import Person.Person;

public class Book {
	private String title;
	private String issn;
	private Date publicationDate;
	private Person authorName;

	public Book(String title, String issn, Date publicationDate, Person authorName) {
        	this.title = title;
        	this.issn = issn;
        	this.publicationDate = publicationDate;
        	this.authorName = authorName;
	}

    	public Book(Book other) {
        	this.title = other.title;
        	this.issn = other.issn;
        	this.publicationDate = new Date(other.publicationDate);
        	this.authorName = new Person(other.authorName);
    	}

    	public void showBookDetails() {
        	System.out.println("Book Title: " + title);
        	System.out.println("ISSN: " + issn);
        	System.out.print("Publication Date: ");
        	publicationDate.showDate();
        	System.out.print("Author: ");
        	authorName.showPersonDetails();
    	}
	public String getTitle() {
        	return title;
    	}

    	public void setTitle(String title) {
        	this.title = title;
    	}
}
