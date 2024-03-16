package koke.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidLogInException extends RuntimeException {

	private static final long serialVersionUID = 5197521722153903313L;

	public InvalidLogInException() {
		super();
	}

	public InvalidLogInException(String message) {
		super(message);
	}

	public InvalidLogInException(String message, Throwable cause) {
		super(message, cause);
	}
}
