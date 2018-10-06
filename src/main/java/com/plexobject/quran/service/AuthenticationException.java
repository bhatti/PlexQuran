package com.plexobject.quran.service;

public class AuthenticationException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public AuthenticationException(Exception e) {
        super(e);
    }

    public AuthenticationException(int errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public AuthenticationException(int errorCode) {
        super(errorCode);
    }

    public AuthenticationException(String errorMessage, Exception e) {
        super(errorMessage, e);
    }

    public AuthenticationException(String errorMessage) {
        super(errorMessage);
    }

}
