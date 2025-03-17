package com.bsit.uniread.infrastructure.handler.exceptions.follow;

public class UserNotFollowedException extends RuntimeException {
    public UserNotFollowedException(String message) {
        super(message);
    }
}
