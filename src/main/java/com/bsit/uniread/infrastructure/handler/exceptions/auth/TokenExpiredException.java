package com.bsit.uniread.infrastructure.handler.exceptions.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
