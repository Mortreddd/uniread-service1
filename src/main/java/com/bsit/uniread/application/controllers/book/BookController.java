package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.book.BookSearchFilter;
import com.bsit.uniread.application.dto.response.book.BookDetailsDto;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping(path = ApiEndpoints.BOOKS)
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Get the books
     * @param filter - selected filters
     * @param userDetails - current user
     * @return page of books
     */
    @GetMapping
    @Operation(summary = "Search Books", description = "Search books which supports multiple filters")
    public ResponseEntity<Page<BookDto>> getBooks(
            @ModelAttribute BookSearchFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var books = bookService.searchBooks(userDetails, filter);
        return ResponseEntity.ok()
                        .body(books);
    }

    /**
     * Get the specified book
     * @param bookId
     * @return book
     */
    @GetMapping(path = "/{bookId}")
    @Operation(summary = "Get the specific book by id")
    public ResponseEntity<BookDetailsDto> getBookById(
            @PathVariable(name = "bookId") UUID bookId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var book = bookService.findBookById(userDetails, bookId);
        return ResponseEntity.ok()
                .body(book);
    }

    @DeleteMapping(path = "/{bookId}")
    @Operation(summary = "Mark the specific book as soft delete")
    public ResponseEntity deleteBookById(
            @PathVariable(name = "bookId") UUID bookId
    ) {
        bookService.deleteBook(bookId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
