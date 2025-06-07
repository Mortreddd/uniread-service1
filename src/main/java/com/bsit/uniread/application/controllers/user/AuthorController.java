package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.application.services.user.AuthorService;
import com.bsit.uniread.domain.entities.book.BookStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTHORS)
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorDto>> getAuthors(
        @RequestParam(name = "pageNo", required = false) int pageNo,
        @RequestParam(name = "pageSize", required = false) int pageSize,
        @RequestParam(name = "query", required = false) String query,
        @RequestParam(name = "startDate", required = false) String startDate,
        @RequestParam(name = "endDate", required = false) String endDate,
        @RequestParam(name = "bannedAt", required = false) String bannedAt,

        @RequestParam(name = "sortBy", defaultValue = "asc", required = false) String sortBy,
        @RequestParam(name = "orderBy", defaultValue = "createdAt", required = false) String orderBy
    ) {
        Page<AuthorDto> authors = authorService.getAuthors(pageNo, pageSize, query).map(AuthorDto::new);
        return ResponseEntity.ok().body(authors);
    }
    @GetMapping(path = "/{userId}/books")
    public ResponseEntity<Page<BookDto>> getAuthorsBook(
            @PathVariable(name = "userId") UUID userId,
            @RequestParam(name = "pageNo", required = false) int pageNo,
            @RequestParam(name = "pageSize", required = false) int pageSize,
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
