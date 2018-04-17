package se.codepool.automapper_sample;
import java.util.Arrays;
import java.util.List;

import se.codepool.automapper.entities.Author;
import se.codepool.automapper.entities.Book;
import se.codepool.automapper.utilities.Mapper;
import se.codepool.automapper.viewmodels.AuthorViewModel;
import se.codepool.automapper.viewmodels.BookViewModel;

public class App 
{
    public static void main( String[] args )
    {
    	Author author = new Author(1, "James", "Gosling");
    	Book book = new Book(1, "The Java Language Specification", author);
    	
    	List<Book> books = Arrays.asList(book);
    	author.setBooks(books);
    	
    	
    	BookViewModel vm = Mapper.setViewModel(BookViewModel.class).map(book);
    	System.out.println(vm);
    	
    	System.out.println("----------------------------------------");
    	
    	AuthorViewModel authorViewModel = Mapper.setViewModel(AuthorViewModel.class).map(author);
    	
    	System.out.println(authorViewModel);
    	
    }
}