package se.codepool.automapper_sample.entities;

public class Book {
	private int id;
	private String name;
	private Author author;
	
	public Book() {}
	
	public Book(int id, String name, Author author) {
		this.setId(id);
		this.setName(name);
		this.setAuthor(author);
	}

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

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}
}
