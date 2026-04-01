package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.request.user.AuthorFilter;
import com.bsit.uniread.application.dto.response.user.AuthorDetail;
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
    public ResponseEntity<Page<AuthorDetail>> getAuthors(
            @ModelAttribute AuthorFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<AuthorDetail> authors = authorService.getAuthors(filter, userDetails.getId());
        return ResponseEntity.ok().body(authors);
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<?> getAuthorById(
            @PathVariable(name = "authorId") UUID authorId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BookStatus bookStatus = BookStatus.PUBLISHED;
        AuthorDetail author = authorService.getAuthorById(authorId, bookStatus);
        return ResponseEntity.ok().body(author);
    }
}
