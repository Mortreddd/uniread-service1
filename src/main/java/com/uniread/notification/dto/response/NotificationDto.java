package com.uniread.notification.dto.response;

import com.uniread.book.dto.response.AuthorDto;
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
