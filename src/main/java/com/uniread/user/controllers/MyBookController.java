package com.uniread.user.controllers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.user.dto.request.UserBookFilter;
import com.uniread.user.dto.response.UserBookDetail;
import com.uniread.user.service.MyBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/me/books")
public class MyBookController {

    private final MyBookService myBookService;


    @GetMapping
    public ResponseEntity<Page<UserBookDetail>> getUserBooks(
            @ModelAttribute UserBookFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var books = myBookService.getUserBooks(filter, userDetails);
        return ResponseEntity.ok(books);
    }

    @GetMapping(path = "/{bookId}")
    public ResponseEntity<UserBookDetail> getUserBook(
            @PathVariable("bookId") UUID bookId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var book = myBookService.getUserBook(bookId, userDetails);

        return ResponseEntity.ok().body(book);
    }
}
