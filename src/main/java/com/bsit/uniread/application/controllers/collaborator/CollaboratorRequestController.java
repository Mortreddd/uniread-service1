package com.bsit.uniread.application.controllers.collaborator;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.collaborator.EditBookCollaboratorRequest;
import com.bsit.uniread.application.dto.request.collaborator.NewCollaboratorRequest;
import com.bsit.uniread.application.dto.response.collaborator.CollaboratorRequestDto;
import com.bsit.uniread.application.services.collaborator.CollaboratorRequestService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = ApiEndpoints.BOOK_COLLABORATION_REQUEST)
@RequiredArgsConstructor
public class CollaboratorRequestController {

    private final CollaboratorRequestService collaboratorRequestService;

    @GetMapping
    public ResponseEntity<Page<CollaboratorRequestDto>> getBookCollaboratorRequests(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        Page<CollaboratorRequestDto> collaboratorRequests = collaboratorRequestService.getBookCollaborationRequests(bookId, pageNo, pageSize)
                .map(CollaboratorRequestDto::new);

        return ResponseEntity.status(HttpStatus.OK)
                .body(collaboratorRequests);
    }

    @PostMapping
    public ResponseEntity<CollaboratorRequestDto> createBookCollaboratorRequest(
        @PathVariable(name = "bookId") UUID bookId,
        @RequestBody NewCollaboratorRequest collaboratorRequest,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        CollaboratorRequestDto request = new CollaboratorRequestDto(collaboratorRequestService.createBookCollaboratorRequest(bookId, collaboratorRequest, userDetails));

        return ResponseEntity.status(HttpStatus.OK)
                .body(request);
    }

    @PutMapping(path  ="/{collaboratorRequestId}")
    public ResponseEntity<CollaboratorRequestDto> editBookCollaboratorRequest(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "collaboratorRequestId") UUID collaboratorRequestId,
            @RequestBody EditBookCollaboratorRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        CollaboratorRequestDto collaboratorRequest = new CollaboratorRequestDto(collaboratorRequestService.editCollaboratorRequest(bookId, collaboratorRequestId, request, userDetails));

        return ResponseEntity.status(HttpStatus.OK)
                .body(collaboratorRequest);
    }

}
