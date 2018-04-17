package se.codepool.automapper.exceptions;


public class FieldNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 6268242549649057008L;
	
	public FieldNotFoundException(String message) {
		super(message);
	}
}
