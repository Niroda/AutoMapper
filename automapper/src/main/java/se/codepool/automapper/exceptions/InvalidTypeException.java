package se.codepool.automapper.exceptions;

public class InvalidTypeException extends RuntimeException {

	private static final long serialVersionUID = -4466801859396299804L;
	public InvalidTypeException(String message) {
		super(message);
	}
}
