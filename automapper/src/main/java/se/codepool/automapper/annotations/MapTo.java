/**
 * 
 */
package se.codepool.automapper.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * An annotation used in case we want to transform from a type to another, 
 * in this case 'selectedProperties()' method is required.
 * Example:<pre>{@code
 * class Book {
 * 	// some fields which are omitted 
 * 	private Author author;
 * 	// getters and setters omitted
 * }
 * class BookViewModel {
 * 	// some fields omitted
 * 	private String author;
 * 	@MapTo(getterMethod = "getAuthor", selectedProperties = { "getFirstname", "getLastname" })
 * 	public String getAuthor() {
 * 		return this.author;
 * 	}
 * 	// getters and setters omitted
 * }
 * }</pre>
 * 
 * in this case not following JavaBean convention, 'selectedProperties()' method is required.
 * Example:<pre>{@code
 * class Book {
 * 	// some fields which are omitted 
 * 	private Author author;
 * 	// getters and setters omitted
 * 	public Author getWriter() {
 * 		return this.author;
 * 	}
 * }
 * class BookViewModel {
 * 	// some fields omitted
 * 	private String authorName;
 * 	@MapTo(getterMethod = "getWriter", selectedProperties = { "getFirstname", "getLastname" })
 * 	public String getAuthorName() {
 * 		return this.author;
 * 	}
 * 	// getters and setters omitted
 * }
 * }</pre>
 * @author Ali
 */
@Retention(RUNTIME)
@Target(FIELD)
public @interface MapTo {
	/**
	 * Maps selected property to desired property from the Entity class.
	 * @return
	 * 		Getter function declared in the Entity class.
	 */
	String getterMethod();
	/**
	 * Defines all methods that will be invoked and combined to transform from a type to another.
	 * In case the new type is not a primitive type, only one method name is allowed.
	 * Example:<pre>{@code
	 * class Student {
	 * 	// other fields, getters and setters are omitted
	 * 	private University university;
	 * }
	 * class University {
	 * 	// other fields, getters and setters are omitted
	 * 	private List<Course> courses = new ArrayList<>();
	 * }
	 * 
	 * class StudentViewModel {
	 * 	// other fields, getters and setters are omitted
	 * 	private List<Course> courses;
	 * 	@MapTo(getterMethod = "getUniversity", selectedProperties = { "getCourses" })
	 * 	public List<Course> getCourses() {
	 * 		return this.courses;
	 * 	}
	 * }
	 * }</pre>
	 * @return
	 * 		Return type of selected property.
	 */
	String[] selectedProperties() default {};
}
