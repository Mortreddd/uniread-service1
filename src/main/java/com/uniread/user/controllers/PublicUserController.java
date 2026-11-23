package com.uniread.user.controllers;

import com.uniread.auth.core.RateLimited;
import com.uniread.user.dto.request.PublicUserFilter;
import com.uniread.user.dto.response.AuthorProfileDetail;
import com.uniread.user.service.PublicUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/authors")
public class PublicUserController {

    private final PublicUserService publicUserService;

    @RateLimited
    @GetMapping
    public ResponseEntity<Page<AuthorProfileDetail>> getAuthors(
            @ModelAttribute PublicUserFilter filter
    ) {

        var publicUsers = publicUserService.getPublicUsers(filter);
        return ResponseEntity.ok(publicUsers);

    }

//    @RateLimited
//    @GetMapping("/{username}")
//    public ResponseEntity<AuthorProfileDetail> getPublicAuthor(
//        @PathVariable("username") String username
//    ) {
//        var author = publicUserService.getPublicUser(username);
//        return ResponseEntity.ok(author);
//
//    }
}
