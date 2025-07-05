package com.bsit.uniread.application.controllers.chapter;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.chapter.EditChapterRequest;
import com.bsit.uniread.application.dto.request.chapter.PublishChapterRequest;
import com.bsit.uniread.application.dto.response.chapter.ChapterDto;
import com.bsit.uniread.application.dto.request.chapter.NewChapterRequest;
import com.bsit.uniread.application.services.chapter.ChapterService;
import com.bsit.uniread.domain.entities.chapter.ChapterStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
     * @param query
     * @param status
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @param deletedAt
     * @return page of chapters
     */
    @GetMapping
    @PreAuthorize("@bookPermission.isPublished(#bookId, #userDetails)")
    public ResponseEntity<Page<ChapterDto>> getBookChapters(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "status", required = false) ChapterStatus status,
            @RequestParam(name = "sortBy", required = false, defaultValue = "asc") String sortBy,
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdAt") String orderBy,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "deletedAt", required = false) String deletedAt,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        log.debug("Deleted At: {}", deletedAt);
        Page<ChapterDto> chapters = chapterService.getBookChapters(bookId, pageNo, pageSize, query, status, sortBy, orderBy, startDate, endDate, deletedAt)
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
    @PreAuthorize("@bookPermission.isAuthor(#bookId, #userDetails)")
    public ResponseEntity<ChapterDto> publishChapter(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "chapterId") UUID chapterId,
            @RequestBody PublishChapterRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ChapterDto chapter = new ChapterDto(chapterService.publishChapter(bookId, chapterId, request.getStatus()));
        return ResponseEntity.ok()
                .body(chapter);
    }


    @DeleteMapping(path = "/{chapterId}/delete")
    @PreAuthorize("@bookPermission.isAuthor(#bookId, #userDetails)")
    public ResponseEntity<SuccessResponse> deleteChapter(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "chapterId") UUID chapterId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        chapterService.deleteById(bookId, chapterId);
        SuccessResponse response = SuccessResponse.builder()
                .message("You successfully deleted the chapter")
                .code(HttpStatus.NO_CONTENT.value())
                .build();

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(response);
    }
}
