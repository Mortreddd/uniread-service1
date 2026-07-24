package com.uniread.social.exceptions;

public class CannotFollowSelfException extends RuntimeException {
    public CannotFollowSelfException(String message) {
        super(message);
    }
}
