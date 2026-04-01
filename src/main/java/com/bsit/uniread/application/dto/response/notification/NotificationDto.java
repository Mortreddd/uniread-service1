package com.bsit.uniread.application.dto.response.notification;

import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.domain.entities.notification.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NotificationDto {

    private UUID id;

    private AuthorDto user;

    private String title;

    private String description;

    private Boolean isRead;

    private Instant createdAt;
}
