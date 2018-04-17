package se.codepool.automapper_sample.viewmodels;

import java.util.ArrayList;
import java.util.List;

import se.codepool.automapper.annotations.MapFrom;
import se.codepool.automapper.annotations.MapTo;
import se.codepool.automapper.annotations.NonCircularReference;
import se.codepool.automapper.annotations.ViewModel;
import se.codepool.automapper_sample.entities.Author;
import se.codepool.automapper_sample.entities.Book;

@ViewModel(entity = Author.class)
public class AuthorViewModel {
	private int id;
	@MapFrom(getterMethods = { "getFirstname", "getLastname" })
	private String firstname;
	@NonCircularReference
	private List<Book> books = new ArrayList<>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return firstname;
	}
	public void setName(String name) {
		this.firstname = name;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	
	@Override
	public String toString() {
		String str = "ID: "+this.getId()+", Name: " + this.getName() + "\nBooks:";
		for(Book book : this.getBooks()) {
			str += "\tID: " + book.getId() + ", Name: " + book.getName() + ", Author: " + book.getAuthor() + "\n";
		}
		return str;
	}
}
