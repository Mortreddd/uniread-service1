package com.uniread.common.controllers;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import com.uniread.common.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {

    private final CloudinaryService service;

    @GetMapping(path = "/signature")
    public ResponseEntity<Map<String, Object>> giveSignatureUrl(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(name = "type") String type
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var payload = service.generateSignature(userDetails, type);
        return ResponseEntity.ok(payload);
    }
}
