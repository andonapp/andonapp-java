package com.andonapp.client.exception;

public class AndonResourceNotFoundException extends AndonAppException {

	private static final long serialVersionUID = 9142554095875911027L;

	public AndonResourceNotFoundException() {
	}

	public AndonResourceNotFoundException(String message) {
		super(message);
	}

	public AndonResourceNotFoundException(Throwable cause) {
		super(cause);
	}

	public AndonResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public AndonResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
