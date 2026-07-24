package com.uniread.collaborator.dto.request;

import com.uniread.collaborator.domain.entities.CollaboratorPermission;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ApproveCollaborationRequest {

    private UUID userCollaboratorId;
    private CollaboratorPermission[] permissions;

}
