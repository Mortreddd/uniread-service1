package com.bsit.uniread.application.dto.request.collaborator;

import com.bsit.uniread.domain.entities.collaborator.CollaboratorPermission;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewCollaboratorRequest {

    private UUID userCollaboratorId;
    CollaboratorPermission[] permissions;

}
