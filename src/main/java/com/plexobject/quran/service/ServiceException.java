package com.plexobject.quran.service;

import java.io.IOException;

public class ServiceException extends IOException {

	private static final long serialVersionUID = 1L;
	private int errorCode = -1;

	/**
	 * Constructor to Initialize the object
	 * 
	 * @param errorCode
	 *            Error Code from the caller
	 * @param errorMessage
	 *            Error Message from the caller
	 */
	public ServiceException(int errorCode, String errorMessage) {
		super(errorMessage);

		this.errorCode = errorCode;
	}

	public ServiceException(Exception e) {
		super(e);
	}

	/**
	 * Constructor to Initialize the object
	 * 
	 * @param errorMessage
	 *            Error Message from the caller
	 */
	public ServiceException(String errorMessage) {
		super(errorMessage);
	}

	public ServiceException(int errorCode) {
		this.errorCode = errorCode;
	}

	public ServiceException(String errorMessage, Exception e) {
		super(errorMessage, e);
	}

	/**
	 * This method is used to set the error code
	 * 
	 * @param errorCode
	 *            from the caller
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * This method returns the error code
	 * 
	 * @return int errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

}
