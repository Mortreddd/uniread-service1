package com.bsit.uniread.application.dto.response.collaborator;

import com.bsit.uniread.application.controllers.collaborator.RequestStatus;
import com.bsit.uniread.application.dto.response.book.SimpleBookInfoDto;
import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import com.bsit.uniread.domain.entities.collaborator.CollaboratorRequest;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CollaboratorRequestDto {

    private UUID id;
    private SimpleUserInfo user;
    private SimpleBookInfoDto book;
    private String message;
    private RequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CollaboratorRequestDto(CollaboratorRequest collaboratorRequest) {
        this.id = collaboratorRequest.getId();
        this.user = new SimpleUserInfo(collaboratorRequest.getUser());
        this.book = new SimpleBookInfoDto(collaboratorRequest.getBook());
        this.message = collaboratorRequest.getMessage();
        this.status = collaboratorRequest.getStatus();
        this.createdAt = collaboratorRequest.getCreatedAt();
        this.updatedAt = collaboratorRequest.getUpdatedAt();
    }

}
