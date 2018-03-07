package com.andonapp.client;

import java.util.Objects;

import com.andonapp.client.exception.AndonAppException;
import com.andonapp.client.exception.AndonBadRequestException;
import com.andonapp.client.exception.AndonInternalErrorException;
import com.andonapp.client.exception.AndonInvalidRequestException;
import com.andonapp.client.exception.AndonResourceNotFoundException;
import com.andonapp.client.exception.AndonUnauthorizedRequestException;
import com.andonapp.client.model.ErrorResponse;
import com.andonapp.client.model.SpringErrorResponse;

final class Exceptions {

	private Exceptions() {
		// static class
	}
	
	public static void throwFromErrorResponse(ErrorResponse response) {
		if (Objects.isNull(response) || Objects.isNull(response.getErrorType())) {
			return;
		}
		
		String errorMessage = response.getErrorMessage();
		
		switch (response.getErrorType()) {
		case "BAD_REQUEST": 
			throw new AndonBadRequestException(errorMessage);
		case "INVALID_REQUEST": 
			throw new AndonInvalidRequestException(errorMessage);
		case "RESOURCE_NOT_FOUND": 
			throw new AndonResourceNotFoundException(errorMessage);
		case "UNAUTHORIZED_REQUEST": 
			throw new AndonUnauthorizedRequestException(errorMessage);
		case "INTERNAL_ERROR": 
			throw new AndonInternalErrorException(errorMessage);
		default:
			throw new AndonAppException(errorMessage);
		}
	}
	
	public static void throwFromSpringErrorResponse(SpringErrorResponse response) {
		if (Objects.isNull(response) || Objects.isNull(response.getStatus())) {
			return;
		}
		
		int status = response.getStatus();
		String message = response.getMessage();
		
		if (status == 401) {
			throw new AndonUnauthorizedRequestException(message);
		} else if (status >= 400 && status < 500) {
			throw new AndonBadRequestException(message);
		} else {
			throw new AndonInternalErrorException(message);
		}
	}
	
}
