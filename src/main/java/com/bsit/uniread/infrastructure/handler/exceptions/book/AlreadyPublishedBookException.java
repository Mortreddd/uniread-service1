package com.bsit.uniread.infrastructure.handler.exceptions.book;

public class AlreadyPublishedBookException extends RuntimeException {
    public AlreadyPublishedBookException(String message) {
        super(message);
    }
}
