package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.book.BookCreationRequest;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping(path = ApiEndpoints.BOOKS)
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Get the books
     * @param pageNo
     * @param pageSize
     * @param query
     * @param genres
     * @param status
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @param deletedAt
     * @return page of books
     */
    @GetMapping
    public ResponseEntity<Page<BookDto>> getBooks(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "query", defaultValue = "", required = false) String query,
            @RequestParam(name = "genres", required = false) List<Integer> genres,
            @RequestParam(name = "status", required = false) BookStatus status,
            @RequestParam(name = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "createdAt", required = false) String orderBy,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "deletedAt", required = false) String deletedAt
    ) {
        Page<BookDto> books = bookService.getBooks(pageNo, pageSize, query, genres, status, sortBy, orderBy, startDate, endDate, deletedAt)
                .map(BookDto::new);
        return ResponseEntity.ok()
                        .body(books);
    }

    /**
     * Get the specified book
     * @param bookId
     * @return book
     */
    @GetMapping(path = "/{bookId}")
    @PreAuthorize("@bookPermission.isPublished(#bookId, #userDetails)")
    public ResponseEntity<BookDto> getBookById(
            @PathVariable(name = "bookId") UUID bookId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BookDto book = new BookDto(bookService.getBookById(bookId));
        return ResponseEntity.ok()
                .body(book);
    }

    /**
     * Create a new book
     * @param request
     * @return book
     * @throws IOException
     */
    @PostMapping(path = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BookDto> createBook(
            @Valid @ModelAttribute BookCreationRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws IOException {
        BookDto book = new BookDto(bookService.createBook(request, userDetails));
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @DeleteMapping(path = "/{bookId}")
    public ResponseEntity<SuccessResponse> deleteBookById(
            @PathVariable(name = "bookId") UUID bookId
    ) throws IOException {
        bookService.deleteBookById(bookId);
        SuccessResponse response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully deleted")
                .build();
        return ResponseEntity.ok(response);
    }

}
