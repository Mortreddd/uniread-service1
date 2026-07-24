package com.uniread.social.exceptions;

public class UserNotFollowedException extends RuntimeException {
    public UserNotFollowedException(String message) {
        super(message);
    }
}
