package com.andonapp.client;

import java.util.Objects;

final class Precondition {

	private Precondition() {
		// static class
	}
	
	public static <T> T checkNotNull(T object, String message) {
		if (Objects.isNull(object)) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}
	
	public static String checkNotBlank(String object, String message) {
		if (Objects.isNull(object) || object.trim().isEmpty()) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}
	
}
