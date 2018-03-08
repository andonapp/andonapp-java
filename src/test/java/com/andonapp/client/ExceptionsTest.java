package com.andonapp.client;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.andonapp.client.exception.AndonAppException;
import com.andonapp.client.exception.AndonBadRequestException;
import com.andonapp.client.exception.AndonInternalErrorException;
import com.andonapp.client.exception.AndonInvalidRequestException;
import com.andonapp.client.exception.AndonResourceNotFoundException;
import com.andonapp.client.exception.AndonUnauthorizedRequestException;
import com.andonapp.client.model.ErrorResponse;
import com.andonapp.client.model.SpringErrorResponse;

public class ExceptionsTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	private String message = "message";
	
	@Test
	public void shouldDoNothingWhenErrorResponseNull() {
		Exceptions.throwFromErrorResponse(null);
	}
	
	@Test
	public void shouldDoNothingWhenErrorResponseTypeNull() {
		Exceptions.throwFromErrorResponse(new ErrorResponse(null, message));
	}
	
	@Test
	public void shouldThrowBadRequestExceptionWhenBadRequest() {
		exception.expect(AndonBadRequestException.class);
		exception.expectMessage(message);
		Exceptions.throwFromErrorResponse(new ErrorResponse("BAD_REQUEST", message));
	}
	
	@Test
	public void shouldThrowInvalidRequestExceptionWhenInvalidRequest() {
		exception.expect(AndonInvalidRequestException.class);
		exception.expectMessage(message);
		Exceptions.throwFromErrorResponse(new ErrorResponse("INVALID_REQUEST", message));
	}
	
	@Test
	public void shouldThrowResourceNotFoundExceptionWhenResourceNotFoundRequest() {
		exception.expect(AndonResourceNotFoundException.class);
		exception.expectMessage(message);
		Exceptions.throwFromErrorResponse(new ErrorResponse("RESOURCE_NOT_FOUND", message));
	}
	
	@Test
	public void shouldThrowUnauthorizedExceptionWhenUnauthorizedRequest() {
		exception.expect(AndonUnauthorizedRequestException.class);
		exception.expectMessage(message);
		Exceptions.throwFromErrorResponse(new ErrorResponse("UNAUTHORIZED_REQUEST", message));
	}
	
	@Test
	public void shouldThrowInternalErrorWhenInternalError() {
		exception.expect(AndonInternalErrorException.class);
		exception.expectMessage(message);
		Exceptions.throwFromErrorResponse(new ErrorResponse("INTERNAL_ERROR", message));
	}
	
	@Test
	public void shouldThrowGeneralExceptionWhenUnknownError() {
		exception.expect(AndonAppException.class);
		exception.expectMessage(message);
		Exceptions.throwFromErrorResponse(new ErrorResponse("ASDF", message));
	}
	
	@Test
	public void shouldDoNothingWhenSpringResponseNull() {
		Exceptions.throwFromSpringErrorResponse(null);
	}
	
	@Test
	public void shouldDoNothingWhenSpringResponseCodeNull() {
		Exceptions.throwFromSpringErrorResponse(new SpringErrorResponse("t", null, "error", message, "path"));
	}
	
	@Test
	public void shouldThrowUnauthorizedExceptionWhenUnauthorized() {
		exception.expect(AndonUnauthorizedRequestException.class);
		exception.expectMessage(message);
		Exceptions.throwFromSpringErrorResponse(new SpringErrorResponse("t", 401, "error", message, "path"));
	}
	
	@Test
	public void shouldThrowBadRequestExceptionWhen400Code() {
		exception.expect(AndonBadRequestException.class);
		exception.expectMessage(message);
		Exceptions.throwFromSpringErrorResponse(new SpringErrorResponse("t", 400, "error", message, "path"));
	}
	
	@Test
	public void shouldThrowInternalExceptionWhen500Code() {
		exception.expect(AndonInternalErrorException.class);
		exception.expectMessage(message);
		Exceptions.throwFromSpringErrorResponse(new SpringErrorResponse("t", 500, "error", message, "path"));
	}
	
}
