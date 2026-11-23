package com.uniread.book.controllers;

import com.uniread.auth.core.RateLimited;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.dto.response.VolumeDto;
import com.uniread.book.service.VolumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/volumes")
public class VolumeController {

    private final VolumeService volumeService;

    @RateLimited
    @GetMapping(path = "/{volumeId}")
    public ResponseEntity<VolumeDto> getVolume(
            @PathVariable("volumeId") UUID volumeId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        var volume = volumeService.getBookVolume(volumeId, userDetails);
        return ResponseEntity.ok(volume);
    }
}
