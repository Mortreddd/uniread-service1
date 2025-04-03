package com.bsit.uniread.application.dto.response.notification;

import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.Notification;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class NotificationDto {

    private UUID id;

    private AuthorDto user;

    private String title;

    private String description;

    private Boolean isRead;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public NotificationDto(Notification notification) {
        this.id = notification.getId();
        this.user = new AuthorDto(notification.getUser());
        this.title = notification.getTitle();
        this.description = notification.getDescription();
        this.isRead = notification.getIsRead();
        this.createdAt = notification.getCreatedAt();
        this.updatedAt = notification.getUpdatedAt();
    }
}
