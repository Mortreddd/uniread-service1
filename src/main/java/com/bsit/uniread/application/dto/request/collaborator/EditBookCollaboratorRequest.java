package com.bsit.uniread.application.dto.request.collaborator;

import com.bsit.uniread.application.controllers.collaborator.RequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EditBookCollaboratorRequest {

    private RequestStatus status;
}
