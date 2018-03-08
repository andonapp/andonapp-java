package com.andonapp.client.exception;

/**
 * Generic exception when a request to Andon fails because there's something wrong
 * with the request.
 */
public class AndonBadRequestException extends AndonAppException {

	private static final long serialVersionUID = -6723350620991748023L;

	public AndonBadRequestException() {
	}

	public AndonBadRequestException(String message) {
		super(message);
	}

	public AndonBadRequestException(Throwable cause) {
		super(cause);
	}

	public AndonBadRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public AndonBadRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
