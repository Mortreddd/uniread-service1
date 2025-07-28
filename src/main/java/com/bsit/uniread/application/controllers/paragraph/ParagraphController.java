package com.bsit.uniread.application.controllers.paragraph;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.paragraph.ParagraphDto;
import com.bsit.uniread.application.services.paragraph.ParagraphService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Api Endpoint - /api/v1/books/{bookId}/chapters/{chapterId}/paragraphs
 */
@RequestMapping(path = ApiEndpoints.BOOK_CHAPTER_PARAGRAPHS)
@RequiredArgsConstructor
@RestController
public class ParagraphController {

    private final ParagraphService paragraphService;

    @GetMapping
    @PreAuthorize("@bookPermission.isPublished(#bookId, #userDetails) && @chapterPermission.isAccessible(#bookId, #chapterId, #userDetails)")
    public ResponseEntity<List<ParagraphDto>> getChapterParagraphs(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "chapterId") UUID chapterId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<ParagraphDto> paragraphs = paragraphService.getBookChapterParagraphs(bookId, chapterId)
                .stream()
                .map(ParagraphDto::new)
                .toList();

        return ResponseEntity.ok()
                .body(paragraphs);
    }

}
