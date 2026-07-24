package com.uniread.social.exceptions;

public class AlreadyFollowingUserException extends RuntimeException {
    public AlreadyFollowingUserException(String message) {
        super(message);
    }

}
