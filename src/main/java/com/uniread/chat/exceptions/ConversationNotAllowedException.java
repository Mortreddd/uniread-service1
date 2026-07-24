package com.uniread.chat.exceptions;

public class ConversationNotAllowedException extends RuntimeException {
    public ConversationNotAllowedException(String message) {
        super(message);
    }
}
