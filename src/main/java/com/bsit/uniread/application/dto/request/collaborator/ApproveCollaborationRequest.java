package com.bsit.uniread.application.dto.request.collaborator;

import com.bsit.uniread.domain.entities.collaborator.CollaboratorPermission;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ApproveCollaborationRequest {

    private UUID userCollaboratorId;
    private CollaboratorPermission[] permissions;

}
