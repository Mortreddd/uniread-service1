package com.bsit.uniread.infrastructure.handler.exceptions.message;

public class ConversationNotAllowedException extends RuntimeException {
    public ConversationNotAllowedException(String message) {
        super(message);
    }
}
