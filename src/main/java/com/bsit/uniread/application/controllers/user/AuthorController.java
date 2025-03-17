package com.bsit.uniread.application.controllers.user;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.application.services.user.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.AUTHORS)
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping(path = "/profile")
    public ResponseEntity<UserDto> getUserByUsername(
            @RequestParam(name = "username", required = false) String username
    ) {
        return ResponseEntity.ok()
                .body(authorService.getAuthorByUsername(username));
    }

}
