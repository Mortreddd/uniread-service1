package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.application.services.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(path = ApiEndpoints.BOOKS)
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "search", defaultValue = "", required = false) String bookTitle
    ) {
        return ResponseEntity.ok()
                        .body(bookService.getBooks(pageNo, pageSize, bookTitle));
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity<Book> getBookById(
            @PathVariable(name = "bookId") UUID bookId
    ) {

        return ResponseEntity.ok()
                .body(bookService.getBookById(bookId));

    }
}
