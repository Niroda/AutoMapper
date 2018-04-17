package se.codepool.automapper.entities;

import java.util.ArrayList;
import java.util.List;

import se.codepool.automapper.entities.Book;

public class Author {
	private int id;
	private String firstname;
	private String lastname;
	private List<Book> books = new ArrayList<>();
	
	public Author() {}
	
	public Author(int id, String firstname, String lastname) {
		this.setId(id);
		this.setFirstname(firstname);
		this.setLastname(lastname);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	public String getFullname() {
		return this.firstname + " " + this.lastname;
	}
}
