package com.bsit.uniread.infrastructure.handler.exceptions.book;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(String message) {
        super(message);
    }
}
