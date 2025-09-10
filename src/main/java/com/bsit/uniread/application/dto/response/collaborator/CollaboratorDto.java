package com.bsit.uniread.application.dto.response.collaborator;

import com.bsit.uniread.application.dto.response.book.SimpleBookInfoDto;
import com.bsit.uniread.application.dto.response.user.SimpleUserInfo;
import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class CollaboratorDto {

    private UUID id;
    private SimpleUserInfo user;
    private SimpleBookInfoDto book;
    private LocalDateTime bannedAt;
    private LocalDateTime unbannedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean isAdmin;
    private boolean canEditBook;
    private boolean canDeleteBook;
    private boolean canPublishBook;

    private boolean canAddChapter;
    private boolean canEditChapter;
    private boolean canPublishChapter;

    private boolean canAddCollaborator;
    private boolean canModifyPermissions;

    public CollaboratorDto(Collaborator collaborator) {
        this.id = collaborator.getId();
        this.user = new SimpleUserInfo(collaborator.getUser());
        this.book = new SimpleBookInfoDto(collaborator.getBook());
        this.bannedAt = collaborator.getBannedAt();
        this.unbannedAt = collaborator.getUnbannedAt();
        this.createdAt = collaborator.getCreatedAt();
        this.updatedAt = collaborator.getUpdatedAt();

        this.isAdmin = collaborator.getIsAdmin();
        this.canEditBook = collaborator.getCanEditBook();
        this.canDeleteBook = collaborator.getCanDeleteBook();
        this.canPublishBook = collaborator.getCanPublishBook();

        this.canAddChapter = collaborator.getCanAddChapter();
        this.canEditChapter = collaborator.getCanEditChapter();
        this.canPublishChapter = collaborator.getCanPublishChapter();

        this.canAddCollaborator = collaborator.getCanAddCollaborator();
        this.canModifyPermissions = collaborator.getCanModifyPermissions();
    }
}
