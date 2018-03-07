package com.andonapp.client.exception;

public class AndonInvalidRequestException extends AndonAppException {

	private static final long serialVersionUID = -7609652639321817105L;

	public AndonInvalidRequestException() {
	}

	public AndonInvalidRequestException(String message) {
		super(message);
	}

	public AndonInvalidRequestException(Throwable cause) {
		super(cause);
	}

	public AndonInvalidRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public AndonInvalidRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
