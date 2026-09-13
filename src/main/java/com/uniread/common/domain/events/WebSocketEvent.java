package com.uniread.common.domain.events;

public record WebSocketEvent<T>(
        String type,
        T data
) {}