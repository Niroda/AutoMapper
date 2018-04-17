package se.codepool.automapper.exceptions;

public class InvalidArgumentOnAnnotationException extends RuntimeException {

	private static final long serialVersionUID = -345606069292916645L;
	public InvalidArgumentOnAnnotationException(String message) {
		super(message);
	}
}
