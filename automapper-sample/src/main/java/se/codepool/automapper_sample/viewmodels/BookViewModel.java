package se.codepool.automapper_sample.viewmodels;

import se.codepool.automapper.annotations.MapTo;
import se.codepool.automapper.annotations.ViewModel;
import se.codepool.automapper_sample.entities.Book;

@ViewModel(entity = Book.class)
public class BookViewModel {
	private int id;
	private String name;
	@MapTo(getterMethod = "getAuthor", selectedProperties = { "getFirstname", "getLastname" })
	private String authorname;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthorname() {
		return authorname;
	}
	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}
	
	
	@Override
	public String toString() {
		return "{Book id: " + this.getId() + " with the title '" + this.getName() + "' is written by: " + this.getAuthorname() + " }";
	}
}
