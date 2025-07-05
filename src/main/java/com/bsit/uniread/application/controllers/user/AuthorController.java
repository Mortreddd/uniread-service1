package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.application.services.user.AuthorService;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * User endpoint allowed for guest users
 * @url /api/v1/authors
 */
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTHORS)
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorDto>> getAuthors(
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "createdAt", required = false) String orderBy,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "bannedAt", required = false) String bannedAt,
            @RequestParam(name = "deletedAt", required = false) String deletedAt
    ) {
        Page<AuthorDto> authors = authorService.getAuthors(pageNo, pageSize, query, sortBy, orderBy, startDate, endDate, bannedAt, deletedAt)
                .map(AuthorDto::new);
        return ResponseEntity.ok().body(authors);
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDto> getAuthorById(
            @PathVariable(name = "authorId") UUID authorId
    ) {
        AuthorDto author = new AuthorDto(authorService.getAuthorById(authorId));
        return ResponseEntity.ok()
                .body(author);
    }
    @GetMapping(path = "/{userId}/books")
    public ResponseEntity<Page<BookDto>> getAuthorsBook(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = "0") int pageSize,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "status", required = false) BookStatus status,
            @RequestParam(name = "sortBy", defaultValue = "asc", required = false) String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "createdAt", required = false) String orderBy
    ) {

        Page<BookDto> books = authorService.getAuthorBooksById(userId, pageNo, pageSize, query, status, sortBy, orderBy).map(BookDto::new);
        return ResponseEntity.ok()
                .body(books);

    }


}
