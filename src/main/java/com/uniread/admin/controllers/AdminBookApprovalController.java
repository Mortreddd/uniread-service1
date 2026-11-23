package com.uniread.admin.controllers;

import com.uniread.admin.dto.response.BookPendingApprovalDto;
import com.uniread.admin.services.BookApprovalService;
import com.uniread.auth.core.RateLimited;
import com.uniread.common.domain.entities.Approvable;
import com.uniread.common.domain.entities.PendingListable;
import com.uniread.common.domain.entities.Rejectable;
import com.uniread.common.dto.api.SuccessResponse;
import com.uniread.common.dto.request.ApprovalActionDto;
import com.uniread.common.dto.request.BookApprovalFilter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/admin/books/pendings")
public class AdminBookApprovalController {

    private final Approvable<ApprovalActionDto> approveService;
    private final Rejectable<ApprovalActionDto> rejectService;
    private final PendingListable<BookPendingApprovalDto, BookApprovalFilter> pendingService;

    public AdminBookApprovalController(
            BookApprovalService service
    ) {
        approveService = service;
        rejectService = service;
        pendingService = service;
    }

    @RateLimited
    @GetMapping
    public ResponseEntity<Page<BookPendingApprovalDto>> pendings(
        @ModelAttribute BookApprovalFilter filter
    ) {
        var pendings = pendingService.pendings(filter);
        return ResponseEntity.ok(pendings);
    }

    @PostMapping(path = "/{requestId}/approve")
    public ResponseEntity<SuccessResponse> approve(
            @PathVariable("requestId")UUID requestId,
            @RequestBody @Valid ApprovalActionDto action
    ) {
        approveService.approve(requestId, action);
        var response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully approved a request")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/{requestId}/reject")
    public ResponseEntity<SuccessResponse> reject(
            @PathVariable("requestId")UUID requestId,
            @RequestBody @Valid ApprovalActionDto action
    ) {
        rejectService.reject(requestId, action);
        var response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully rejected a request")
                .build();
        return ResponseEntity.ok(response);
    }

}
