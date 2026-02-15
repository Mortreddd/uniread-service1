package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.user.AuthorBookFilter;
import com.bsit.uniread.application.dto.request.user.AuthorFilter;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.user.AuthorDetail;
import com.bsit.uniread.application.dto.response.user.AuthorDto;
import com.bsit.uniread.application.services.user.AuthorService;
import com.bsit.uniread.domain.entities.Follow;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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
    private final FollowRepository followRepository;

    @GetMapping
    public ResponseEntity<Page<AuthorDetail>> getAuthors(
            @ModelAttribute AuthorFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<AuthorDetail> authors = authorService.getAuthors(filter, userDetails);
        return ResponseEntity.ok().body(authors);
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<AuthorDetail> getAuthorById(
            @PathVariable(name = "authorId") UUID authorId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BookStatus bookStatus = BookStatus.PUBLISHED;
        AuthorDetail author = authorService.getAuthorById(authorId, bookStatus);
        return ResponseEntity.ok().body(author);
    }

    @GetMapping(path = "/{authorId}/books")
    public ResponseEntity<Page<BookDto>> getAuthorsBook(
            @PathVariable(name = "authorId") UUID authorId,
            @ModelAttribute AuthorBookFilter filter
    ) {

        Page<BookDto> books = authorService.getAuthorBooksById(authorId, filter).map(BookDto::new);
        return ResponseEntity.ok()
                .body(books);

    }


}
