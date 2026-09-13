package com.uniread.book.controllers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.book.dto.request.CreateBookRequest;
import com.uniread.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(path = "/books")
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Void> createBook(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @ModelAttribute CreateBookRequest request
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");
        bookService.createBook(userDetails, request);
        return ResponseEntity.ok().build();
    }

}
