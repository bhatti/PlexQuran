package com.plexobject.quran.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {
	private static final long serialVersionUID = 1L;

	public static class Error {
		public final String field;
		public final String message;
		public final String errorCode;

		public Error(String field, String message) {
			this(field, message, null);
		}

		public Error(String field, String message, String errorCode) {
			this.field = field;
			this.message = message;
			this.errorCode = errorCode;
		}
	}

	public final List<Error> errors = new ArrayList<Error>();

	public ValidationException() {
	}

	public void add(String field, String message) {
		this.errors.add(new Error(field, message, null));
	}

	public void add(String field, String message, String errorCode) {
		this.errors.add(new Error(field, message, errorCode));
	}

	public void raiseIfHasErrors() throws ValidationException {
		if (errors.size() > 0) {
			throw this;
		}
	}
}
