package se.codepool.automapper.entities;

import se.codepool.automapper.entities.Author;

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
