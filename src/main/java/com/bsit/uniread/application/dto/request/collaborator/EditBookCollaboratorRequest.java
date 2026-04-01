package com.bsit.uniread.application.dto.request.collaborator;

import com.bsit.uniread.domain.entities.collaborator.CollaboratorRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditBookCollaboratorRequest {

    private CollaboratorRequestStatus status;
}
