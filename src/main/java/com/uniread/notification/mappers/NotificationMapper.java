package com.uniread.notification.mappers;

import com.uniread.notification.domain.entities.Notification;
import com.uniread.notification.dto.response.NotificationDto;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDto toDto(Notification notification) {
        if(notification == null) return null;

        return NotificationDto.builder()
                .id(notification.getId())
                .type(notification.getType())
                .userId(notification.getUser().getId())
                .description(notification.getDescription())
                .title(notification.getTitle())
                .targetId(notification.getTargetId())
                .targetType(notification.getTargetType())
                .isClicked(notification.getIsClicked())
                .isRead(notification.getIsRead())
                .metadata(notification.getMetadata())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}
