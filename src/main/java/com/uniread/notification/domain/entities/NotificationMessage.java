package com.uniread.notification.domain.entities;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class NotificationMessage {
    private UUID userId;
    private NotificationType type;
    private UUID targetId;
    private NotificationTargetType targetType;
    private Map<String, Object> variables;
    private Set<NotificationChannel> channels;
}
