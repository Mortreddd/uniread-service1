package com.uniread.book.controllers;

import com.uniread.auth.core.RateLimited;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.book.dto.request.ChapterFilter;
import com.uniread.book.dto.request.CreateChapterRequest;
import com.uniread.book.dto.response.ChapterDto;
import com.uniread.book.service.ChapterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/volumes/{volumeId}/chapters")
@RequiredArgsConstructor
public class VolumeChapterController {

    private final ChapterService chapterService;

    /**
     * TODO: Add endpoint for getting the chapters of one volume
     * @param volumeId
     * @param filter
     * @return paginated chapters
     */
    @RateLimited
    @GetMapping
    public ResponseEntity<Page<ChapterDto>> getVolumeChapters(
            @PathVariable("volumeId") UUID volumeId,
            @ModelAttribute ChapterFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        var chapters = chapterService.getChapters(volumeId, filter, userDetails);
        return ResponseEntity.ok(chapters);
    }

    @RateLimited(capacity = 10)
    @PostMapping
    public ResponseEntity<ChapterDto> createChapter(
            @PathVariable("volumeId") UUID volumeId,
            @Valid @RequestBody CreateChapterRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var payload = chapterService.createChapter(volumeId, request, userDetails);
        return ResponseEntity.ok().body(payload);
    }
}
