package com.bsit.uniread.infrastructure.handler.exceptions.reaction;

public class AlreadyReactedException extends RuntimeException {
    public AlreadyReactedException(String message) {
        super(message);
    }
}
