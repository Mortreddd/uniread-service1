package com.bsit.uniread.application.controllers.comment;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.book.BookCommentDto;
import com.bsit.uniread.application.services.comment.BookCommentService;
import com.bsit.uniread.domain.entities.book.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Api endpoint - /api/v1/books/{bookId}/comments
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(path = ApiEndpoints.BOOK_COMMENTS)
public class BookCommentController {

    private final BookCommentService bookCommentService;

    /**
     * Get the comments of a book
     *
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @return page of book comment
     */
    @GetMapping
    public ResponseEntity<Page<BookCommentDto>> getBookComments(
            @PathVariable(name = "{bookId}") UUID bookId,
            @RequestParam(name = "pageNo", required = false) int pageNo,
            @RequestParam(name = "pageSize", required = false) int pageSize
    ) {
        Page<BookCommentDto> comments = bookCommentService.getBookComments(bookId, pageNo, pageSize)
                .map(BookCommentDto::new);

        return ResponseEntity.ok()
                .body(comments);
    }

    /**
     * Get the replies of a comment in a book comment
     * @param bookId
     * @param bookCommentId
     * @param pageNo
     * @param pageSize
     * @return page of book comment
     */
    @GetMapping(path = "/{bookCommentId}")
    public ResponseEntity<Page<BookCommentDto>> getBookCommentReplies(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "bookCommentId") UUID bookCommentId,
            @RequestParam(name = "pageNo", required = false) int pageNo,
            @RequestParam(name = "pageSize", required = false) int pageSize
    ) {
        Page<BookCommentDto> bookCommentReplies = bookCommentService.getBookCommentReplies(bookId, bookCommentId, pageNo, pageSize)
                .map(BookCommentDto::new);

        return ResponseEntity.ok()
                .body(bookCommentReplies);
    }
}