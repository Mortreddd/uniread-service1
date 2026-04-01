package com.bsit.uniread.infrastructure.handler.exceptions.book;

public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException(String message) {
        super(message);
    }
}
