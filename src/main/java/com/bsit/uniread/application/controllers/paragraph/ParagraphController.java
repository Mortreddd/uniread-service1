package com.bsit.uniread.application.controllers.paragraph;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.chapter.EditChapterRequest;
import com.bsit.uniread.application.dto.response.paragraph.ParagraphDto;
import com.bsit.uniread.application.services.paragraph.ParagraphService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<ParagraphDto>> getChapterParagraphs(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "chapterId") UUID chapterId
    ) {
        List<ParagraphDto> paragraphs = paragraphService.getBookChapterParagraphs(bookId, chapterId)
                .stream()
                .map(ParagraphDto::new)
                .toList();

        return ResponseEntity.ok()
                .body(paragraphs);
    }

}
