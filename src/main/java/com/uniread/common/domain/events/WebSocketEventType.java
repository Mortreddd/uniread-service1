package com.uniread.common.domain.events;

public enum WebSocketEventType {
    CHAT_UPDATED,
    MESSAGE_RECEIVED,
    MESSAGE_READ,
    TYPING_STARTED,
    TYPING_STOPPED,
    PARTICIPANT_READ
}