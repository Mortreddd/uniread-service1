package com.uniread.notification.dto.response;

import com.uniread.notification.domain.entities.NotificationTargetType;
import com.uniread.notification.domain.entities.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class NotificationDto {


    private UUID id;
    private NotificationType type;
    private UUID userId;
    private String title;
    private String description;
    private Boolean isRead = false;
    private Boolean isClicked = false;
    private UUID targetId; // ID of the related entity (book ID, user ID, etc.)
    private NotificationTargetType targetType; // Type of related entity ("BOOK", "USER", "CHAPTER", etc.)
    private Map<String, Object> metadata; // Additional flexible data

    private Instant createdAt;
    private Instant updatedAt;
}
