package com.plexobject.quran.persistence;

import java.io.IOException;

public class StaleRecordException extends IOException {
	private static final long serialVersionUID = 1L;

	public StaleRecordException(String message, Throwable cause) {
		super(message, cause);
	}

	public StaleRecordException(String message) {
		super(message);
	}

	public StaleRecordException(Throwable cause) {
		super(cause);
	}

}
