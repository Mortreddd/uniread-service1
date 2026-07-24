package com.uniread.collaborator.dto.request;

import com.uniread.collaborator.domain.entities.CollaboratorRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditBookCollaboratorRequest {

    private CollaboratorRequestStatus status;
}
