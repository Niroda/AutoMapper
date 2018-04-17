package se.codepool.automapper.utilities;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import se.codepool.automapper.viewmodels.AuthorViewModel;
import se.codepool.automapper.entities.Author;
import se.codepool.automapper.entities.Book;

public class MapperTest {
	private Author author;
	private Book book;
	public MapperTest() {
		author = new Author(1, "James", "Gosling");
    	book = new Book(1, "The Java Language Specification", author);
    	List<Book> books = Arrays.asList(book);
    	author.setBooks(books);
	}
	
	@Test
	public void checkViewModelReturnType() {
		Object mapper = Mapper.setViewModel(AuthorViewModel.class);
		assertTrue(mapper instanceof Mapper);
	}
	
	@Test
	public void checkSingleMapReturnType() {
		Mapper<AuthorViewModel> mapper = Mapper.setViewModel(AuthorViewModel.class);
		assertTrue(mapper.map(author) instanceof AuthorViewModel);
	}
	
	@Test
	public void checkMapReturnTypeValuesAndAnnotation() {
		Mapper<AuthorViewModel> mapper = Mapper.setViewModel(AuthorViewModel.class);
		AuthorViewModel vm = mapper.map(author);
		assertTrue(vm.getName().equals("James Gosling")); // check MapFrom annotation
		assertTrue(vm.getId() == 1);
		assertTrue(vm.getBooks() != null);
		assertTrue(vm.getBooks().size() > 0);
		assertTrue(vm.getBooks().get(0).getAuthor() == null); // check NonReference annotation
		assertTrue(vm.getBooks().get(0).getId() == 1);
		assertTrue(vm.getBooks().get(0).getName().equals("The Java Language Specification"));
	}
	
}
