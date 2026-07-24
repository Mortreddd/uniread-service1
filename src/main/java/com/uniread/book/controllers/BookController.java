package com.uniread.book.controllers;

import com.uniread.book.dto.request.BookSearchFilter;
import com.uniread.book.dto.response.BookDetailDto;
import com.uniread.book.service.BookService;
import com.uniread.auth.domain.entities.CustomUserDetails;
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
@RequestMapping(path = "/books")
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
    public ResponseEntity<Page<BookDetailDto>> getBooks(
            @ModelAttribute BookSearchFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var authUserId = userDetails != null ? userDetails.getId() : null;
        var books = bookService.searchBooks(authUserId, filter);

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
    public ResponseEntity<BookDetailDto> getBookById(
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
