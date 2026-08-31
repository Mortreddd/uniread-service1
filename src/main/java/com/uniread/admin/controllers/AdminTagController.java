package com.uniread.admin.controllers;

import com.uniread.admin.dto.request.TagMonitoringFilter;
import com.uniread.admin.dto.request.TagRequest;
import com.uniread.admin.dto.response.TagDetailDto;
import com.uniread.admin.services.AdminTagService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final AdminTagService tagService;

    @GetMapping
    public ResponseEntity<Page<TagDetailDto>> getTags(
            @ModelAttribute TagMonitoringFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var tags = tagService.getTags(filter);

        return ResponseEntity.ok().body(tags);
    }

    @PostMapping
    public ResponseEntity<TagDetailDto> createTag(
            @Valid @RequestBody TagRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var payload = tagService.createTag(request, userDetails);
        return ResponseEntity.ok().body(payload);

    }

    @PutMapping(path = "/{tagId}")
    public ResponseEntity<TagDetailDto> updateTag(
            @PathVariable("tagId") UUID tagId,
            @Valid @RequestBody TagRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var payload = tagService.updateTag(tagId, request, userDetails);
        return ResponseEntity.ok().body(payload);

    }
}
