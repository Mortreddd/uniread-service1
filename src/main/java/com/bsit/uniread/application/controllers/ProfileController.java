package com.bsit.uniread.application.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/profile/{username}")
@RequiredArgsConstructor
public class ProfileController {

//    @GetMapping(path = "/")
//    public ResponseEntity<User> getUserProfile(
//            @PathVariable(name = "username", required = true) String username
//    ) {
//        return ResponseEntity.ok()
//
//    }
}
