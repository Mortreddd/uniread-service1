package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.services.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(path = ApiEndpoints.BOOKS)
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<Page<BookDto>> getBooks(
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(name = "query", defaultValue = "", required = false) String query
    ) {
        Page<BookDto> books = bookService.getBooks(pageNo, pageSize, query).map(BookDto::new);
        return ResponseEntity.ok()
                        .body(books);
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity<BookDto> getBookById(
            @PathVariable(name = "bookId") UUID bookId
    ) {
        BookDto book = new BookDto(bookService.getBookById(bookId));
        return ResponseEntity.ok()
                .body(book);
    }
}
