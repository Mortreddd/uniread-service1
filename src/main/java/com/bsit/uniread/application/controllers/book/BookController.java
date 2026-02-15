package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.api.SuccessResponse;
import com.bsit.uniread.application.dto.request.book.BookCreationRequest;
import com.bsit.uniread.application.dto.request.book.BookSearchFilter;
import com.bsit.uniread.application.dto.response.book.BookDetailDto;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.book.DetailedBookDto;
import com.bsit.uniread.application.services.book.BookService;
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
import java.util.UUID;

@Slf4j
@RequestMapping(path = ApiEndpoints.BOOKS)
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * Get the books
     * @param filter
     * @return page of books
     */
    @GetMapping
    public ResponseEntity<Page<BookDto>> getBooks(
            @ModelAttribute BookSearchFilter filter
    ) {
        Page<BookDto> books = bookService.getBooks(filter)
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
    public ResponseEntity<DetailedBookDto> getBookById(
            @PathVariable(name = "bookId") UUID bookId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        UUID userId = (userDetails != null) ? userDetails.getId() : null;

        BookDetailDto detail = bookService.getBookDetailById(bookId, userId);
        DetailedBookDto detailedBook = new DetailedBookDto(bookService.getBookById(bookId), detail);
        return ResponseEntity.ok()
                .body(detailedBook);
    }

    /**
     * Create a new book
     * @param request
     * @return book
     * @throws IOException
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
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
    ) {
        bookService.deleteBookById(bookId);
        SuccessResponse response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully deleted")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{bookId}/force")
    public ResponseEntity<SuccessResponse> forceDeleteBookById(
            @PathVariable(name = "bookId") UUID bookId
    ) throws IOException {
        bookService.forceDeleteBookById(bookId);
        SuccessResponse response = SuccessResponse.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully deleted")
                .build();
        return ResponseEntity.ok(response);
    }

}
