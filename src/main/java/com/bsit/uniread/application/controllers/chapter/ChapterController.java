package com.bsit.uniread.application.controllers.chapter;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.chapter.EditChapterRequest;
import com.bsit.uniread.application.dto.request.chapter.PublishChapterRequest;
import com.bsit.uniread.application.dto.response.chapter.ChapterDto;
import com.bsit.uniread.application.dto.request.chapter.NewChapterRequest;
import com.bsit.uniread.application.services.chapter.ChapterService;
import com.bsit.uniread.domain.entities.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping(path = "/{chapterId}")
    public ResponseEntity<ChapterDto> getBookChapter(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "chapterId") UUID chapterId
    ) {
        ChapterDto chapter = new ChapterDto(chapterService.getBookChapterById(bookId, chapterId));
        return ResponseEntity.ok()
                .body(chapter);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ChapterDto> createBook(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestBody NewChapterRequest request
    ) {
        ChapterDto chapter = new ChapterDto(chapterService.createNewChapter(bookId, request.getTitle()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(chapter);

    }

    @PutMapping(path = "/{chapterId}/update")
    public ResponseEntity<ChapterDto> updateChapter(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "chapterId") UUID chapterId,
            @RequestBody EditChapterRequest request
    ) {
        ChapterDto chapter = new ChapterDto(chapterService.editBookChapterById(bookId, chapterId, request));

        return ResponseEntity.ok()
                .body(chapter);

    }

    @PutMapping(path = "/{chapterId}/publish")
    @PreAuthorize("@bookPermissions.isAuthor(#bookId, authentication)")
    public ResponseEntity<ChapterDto> publishChapter(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "chapterId") UUID chapterId,
            @RequestBody PublishChapterRequest request,
            @AuthenticationPrincipal User user
    ) {
        ChapterDto chapter = new ChapterDto(chapterService.publishChapter(bookId, chapterId, request.getStatus()));
        return ResponseEntity.ok()
                .body(chapter);
    }

    @DeleteMapping(path = "/{chapterId}/delete")
    public ResponseEntity<SuccessResponse> deleteChapter(
            @PathVariable(name = "chapterId") UUID chapterId
    ) {
        chapterService.deleteById(chapterId);
        SuccessResponse response = SuccessResponse.builder()
                .message("You successfully deleted the chapter")
                .code(HttpStatus.NO_CONTENT.value())
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(response);
    }
}
