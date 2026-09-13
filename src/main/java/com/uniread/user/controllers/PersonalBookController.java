package com.uniread.user.controllers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.user.dto.request.UserBookFilter;
import com.uniread.user.dto.response.UserBookDetail;
import com.uniread.user.service.PersonalBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/me/books")
public class PersonalBookController {

    private final PersonalBookService personalBookService;


    @GetMapping
    public ResponseEntity<Page<UserBookDetail>> getUserBooks(
            @ModelAttribute UserBookFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var books = personalBookService.getUserBooks(filter, userDetails);
        return ResponseEntity.ok(books);
    }
}
