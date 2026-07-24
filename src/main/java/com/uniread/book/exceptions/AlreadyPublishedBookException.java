package com.uniread.book.exceptions;

public class AlreadyPublishedBookException extends RuntimeException {
    public AlreadyPublishedBookException(String message) {
        super(message);
    }
}
