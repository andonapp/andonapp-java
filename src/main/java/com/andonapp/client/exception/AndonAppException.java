package com.andonapp.client.exception;

/**
 * Generic catch-all exception when a request to Andon fails.
 */
public class AndonAppException extends RuntimeException {

	private static final long serialVersionUID = 3462127017595785569L;

	public AndonAppException() {
	}

	public AndonAppException(String message) {
		super(message);
	}

	public AndonAppException(Throwable cause) {
		super(cause);
	}

	public AndonAppException(String message, Throwable cause) {
		super(message, cause);
	}

	public AndonAppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
