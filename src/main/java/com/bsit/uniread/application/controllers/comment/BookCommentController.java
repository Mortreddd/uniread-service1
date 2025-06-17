package com.bsit.uniread.application.controllers.comment;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.book.BookCommentCreationRequest;
import com.bsit.uniread.application.dto.request.book.BookCommentReplyCreationRequest;
import com.bsit.uniread.application.dto.response.book.BookCommentDto;
import com.bsit.uniread.application.services.comment.BookCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Controller handling for book comments, review, replies
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.BOOK_COMMENTS) // /api/v1/books/{bookId}/comments
public class BookCommentController {

    private final BookCommentService bookCommentService;

    /**
     * Get the comments of a specific book
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @return page of book comment
     */
    @GetMapping
    public ResponseEntity<Page<BookCommentDto>> getBookComments(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "createdAt", required = false) String orderBy,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate
    ) {

        Page<BookCommentDto> bookComments = bookCommentService
                .getBookComments(bookId, pageNo, pageSize, sortBy, orderBy, startDate, endDate)
                .map(BookCommentDto::new);

        return ResponseEntity.ok()
                .body(bookComments);
    }

    /**
     * Get the replies from a comment
     * @param bookId
     * @param bookCommentId
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @return page of bookComment
     */
    @GetMapping(path = "/{bookCommentId}")
    public ResponseEntity<Page<BookCommentDto>> getBookCommentReplies(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "bookCommentId") UUID bookCommentId,
            @RequestParam(name = "pageNo", required = false) int pageNo,
            @RequestParam(name = "pageSize", required = false) int pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = "asc") String sortBy,
            @RequestParam(name = "orderBy", required = false, defaultValue = "createdAt") String orderBy,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate
    ) {

        Page<BookCommentDto> bookCommentReplies = bookCommentService
                .getBookCommentReplies(bookId, bookCommentId, pageNo, pageSize, sortBy, orderBy, startDate, endDate)
                .map(BookCommentDto::new);

        return ResponseEntity.ok()
                .body(bookCommentReplies);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<BookCommentDto> createBookComment(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestBody BookCommentCreationRequest request
    ) {
        BookCommentDto bookComment = new BookCommentDto(
                bookCommentService.createBookComment(bookId, request.getContent(), request.getRating())
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookComment);
    }

    @PostMapping(path = "/{bookCommentId}/create")
    public ResponseEntity<BookCommentDto> createBookCommentReply(
            @PathVariable(name = "bookId") UUID bookId,
            @PathVariable(name = "bookCommentId") UUID parentBookCommentId,
            @RequestBody BookCommentReplyCreationRequest request
    ) {
        BookCommentDto bookComment = new BookCommentDto(
                bookCommentService.createBookCommentReply(bookId, parentBookCommentId, request.getCommenterId(), request.getContent(), request.getRating())
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookComment);
    }

}
