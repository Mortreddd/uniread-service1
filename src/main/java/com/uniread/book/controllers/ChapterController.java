package com.uniread.book.controllers;

import com.uniread.auth.core.RateLimited;
import com.uniread.auth.core.RefillDurationType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/chapters")
@RestController
@RequiredArgsConstructor
public class ChapterController {


    @RateLimited(capacity = 5, refillTokens = 5, refillDurationType = RefillDurationType.DAY)
    @GetMapping
    public ResponseEntity<String> testRateLimiter() {
        var message = "Hello World";
        return ResponseEntity.ok(message);
    }

}
