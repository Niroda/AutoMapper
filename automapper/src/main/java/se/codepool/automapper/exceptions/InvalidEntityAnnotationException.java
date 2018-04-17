package se.codepool.automapper.exceptions;

public class InvalidEntityAnnotationException extends RuntimeException {

	private static final long serialVersionUID = -5904996929670777895L;
	public InvalidEntityAnnotationException(String message) {
		super(message);
	}
}
