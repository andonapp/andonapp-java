package com.andonapp.client.exception;

public class AndonInternalErrorException extends AndonAppException {

	private static final long serialVersionUID = 1990102739578050575L;

	public AndonInternalErrorException() {
	}

	public AndonInternalErrorException(String message) {
		super(message);
	}

	public AndonInternalErrorException(Throwable cause) {
		super(cause);
	}

	public AndonInternalErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public AndonInternalErrorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
