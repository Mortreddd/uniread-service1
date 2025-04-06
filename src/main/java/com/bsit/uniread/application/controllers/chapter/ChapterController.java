package com.bsit.uniread.application.controllers.chapter;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.chapter.ChapterDto;
import com.bsit.uniread.application.services.chapter.ChapterService;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api endpoint - /api/v1/books/{bookId}/chapters
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiEndpoints.BOOK_CHAPTERS)
public class ChapterController {

    private final ChapterService chapterService;

    /**
     * Get the chapters of a book
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @return page of chapters
     */
    @GetMapping
    public ResponseEntity<Page<ChapterDto>> getBookChapters(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestParam(name = "pageNo", required = false) int pageNo,
            @RequestParam(name = "pageSize", required = false) int pageSize
    ) {

        Page<ChapterDto> chapters = chapterService.getBookChapters(bookId, pageNo, pageSize)
                .map(ChapterDto::new);

        return ResponseEntity.ok()
                .body(chapters);
    }

}
