package com.bsit.uniread.infrastructure.handler.exceptions.auth;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }
    public InvalidTokenException(String message, Throwable exception) {
        super(message, exception);
    }

}
