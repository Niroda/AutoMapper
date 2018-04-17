package se.codepool.automapper_sample;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.codepool.automapper.utilities.Mapper;
import se.codepool.automapper_sample.entities.Author;
import se.codepool.automapper_sample.entities.Book;
import se.codepool.automapper_sample.viewmodels.AuthorViewModel;
import se.codepool.automapper_sample.viewmodels.BookViewModel;

public class App 
{
    public static void main( String[] args ) throws NoSuchFieldException, SecurityException
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