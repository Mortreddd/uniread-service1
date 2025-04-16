package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.book.BookCreationRequest;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.BookStatus;
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
     * @return page of books
     */
    @GetMapping
    public ResponseEntity<Page<BookDto>> getBooks(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "query", defaultValue = "", required = false) String query,
            @RequestParam(name = "status", required = false) BookStatus status
    ) {
        Page<BookDto> books = bookService.getBooks(pageNo, pageSize, query, status).map(BookDto::new);
        return ResponseEntity.ok()
                        .body(books);
    }

    /**
     * Get the specified book
     * @param bookId
     * @return book
     */
    @GetMapping(path = "/{bookId}")
    public ResponseEntity<BookDto> getBookById(
            @PathVariable(name = "bookId") UUID bookId,
            @RequestParam(name = "status", required = false) BookStatus status
    ) {
        BookDto book = new BookDto(bookService.getBookById(bookId, status));
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
            @Valid @ModelAttribute BookCreationRequest request
    ) throws IOException {
        BookDto book = new BookDto(bookService.createBook(request));
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
