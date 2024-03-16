package koke.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceNotAvailableException extends RuntimeException {

	private static final long serialVersionUID = 5197521722153903313L;

	public ResourceNotAvailableException() {
		super();
	}

	public ResourceNotAvailableException(String message) {
		super(message);
	}

	public ResourceNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}
}