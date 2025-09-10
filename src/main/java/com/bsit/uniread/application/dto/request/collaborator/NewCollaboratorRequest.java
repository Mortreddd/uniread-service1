package com.bsit.uniread.application.dto.request.collaborator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewCollaboratorRequest {
    private String message;

}
