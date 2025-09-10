package com.bsit.uniread.application.controllers.collaborator;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.collaborator.ApproveCollaborationRequest;
import com.bsit.uniread.application.dto.request.collaborator.NewCollaboratorRequest;
import com.bsit.uniread.application.services.collaborator.CollaboratorService;
import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(path = ApiEndpoints.BOOK_COLLABORATORS)
@RequiredArgsConstructor
@RestController
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    @GetMapping
    public ResponseEntity<Page<Collaborator>> getBookCollaborators(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize
    ) {

        Page<Collaborator> collaborators = collaboratorService.getBookCollaborators(bookId, pageNo, pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(collaborators);
    }

    // TODO: Add verification if the approve is owner or has permission
    @PostMapping
    public ResponseEntity<Collaborator> storeCollaborator(
            @PathVariable(name = "bookId") UUID bookId,
            @Valid @RequestBody ApproveCollaborationRequest request,
            @AuthenticationPrincipal  CustomUserDetails userDetails
    ) {

        Collaborator collaborator = collaboratorService.createCollaborator(bookId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(collaborator);
    }

    @PutMapping(value = "/{collaboratorId}")
    public ResponseEntity<Collaborator> editCollaborator(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "collaboratorId") UUID collaboratorId,
            @AuthenticationPrincipal  CustomUserDetails userDetails
    ) {
        return null;
    }
    
    
    @DeleteMapping(value = "/{collaboratorId}")
    public ResponseEntity<SuccessResponse> deleteCollaborator(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "collaboratorId") UUID collaboratorId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        collaboratorService.deleteCollaborator(collaboratorId);
        SuccessResponse response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully removed collaborator")
                .build();

        return ResponseEntity.ok()
                .body(response);
    }

    @PutMapping(value = "/{collaboratorId}/bans")
    public ResponseEntity<Collaborator> banCollaborator(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "collaboratorId") UUID collaboratorId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Collaborator collaborator = collaboratorService.banCollaborator(collaboratorId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(collaborator);
    }

    @DeleteMapping
    public ResponseEntity<Collaborator> unbanCollaborator(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "collaboratorId") UUID collaboratorId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Collaborator collaborator = collaboratorService.unbanCollaborator(collaboratorId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(collaborator);
    }

}
