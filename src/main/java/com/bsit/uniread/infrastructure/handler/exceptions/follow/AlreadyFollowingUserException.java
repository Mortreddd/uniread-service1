package com.bsit.uniread.infrastructure.handler.exceptions.follow;

public class AlreadyFollowingUserException extends RuntimeException {
    public AlreadyFollowingUserException(String message) {
        super(message);
    }

}
