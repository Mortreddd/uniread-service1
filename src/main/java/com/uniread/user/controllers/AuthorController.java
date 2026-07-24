package com.uniread.user.controllers;

import com.uniread.user.dto.request.AuthorFilter;
import com.uniread.user.dto.response.UserDetail;
import com.uniread.user.service.AuthorService;
import com.uniread.book.domain.entities.BookStatus;
import com.uniread.auth.domain.entities.CustomUserDetails;
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
@RequestMapping(path = "/authors")
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<UserDetail>> getAuthors(
            @ModelAttribute AuthorFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<UserDetail> authors = authorService.getUsersDetail(filter, userDetails.getId());
        return ResponseEntity.ok().body(authors);
    }

    @GetMapping(path = "/{authorId}")
    public ResponseEntity<?> getAuthorById(
            @PathVariable(name = "authorId") UUID authorId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        BookStatus bookStatus = BookStatus.PUBLISHED;
        UserDetail author = authorService.getUserDetailById(authorId, bookStatus);
        return ResponseEntity.ok().body(author);
    }
}
