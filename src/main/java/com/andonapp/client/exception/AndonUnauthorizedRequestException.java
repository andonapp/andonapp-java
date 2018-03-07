package com.andonapp.client.exception;

public class AndonUnauthorizedRequestException extends AndonAppException {

	private static final long serialVersionUID = -4282010568019737039L;

	public AndonUnauthorizedRequestException() {
	}

	public AndonUnauthorizedRequestException(String message) {
		super(message);
	}

	public AndonUnauthorizedRequestException(Throwable cause) {
		super(cause);
	}

	public AndonUnauthorizedRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public AndonUnauthorizedRequestException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
