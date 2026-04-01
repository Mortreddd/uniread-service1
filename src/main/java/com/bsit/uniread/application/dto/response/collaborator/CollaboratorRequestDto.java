package com.bsit.uniread.application.dto.response.collaborator;

import com.bsit.uniread.domain.entities.collaborator.CollaboratorRequestStatus;
import com.bsit.uniread.application.dto.response.book.SimpleBookInfoDto;
import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class CollaboratorRequestDto {

    private UUID id;
    private SimpleUserInfo user;
    private SimpleBookInfoDto book;
    private String message;
    private CollaboratorRequestStatus status;
    private Instant createdAt;
    private Instant updatedAt;

}
