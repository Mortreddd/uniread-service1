package com.uniread.book.controllers;

import com.uniread.auth.core.RateLimited;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.book.dto.request.BookVolumeFilter;
import com.uniread.book.dto.request.CreateVolumeRequest;
import com.uniread.book.dto.response.VolumeDto;
import com.uniread.book.service.VolumeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/books/{bookId}/volumes")
public class BookVolumeController {

    private final VolumeService volumeService;

    @RateLimited
    @GetMapping
    public ResponseEntity<Page<VolumeDto>> getBookVolumes(
            @PathVariable("bookId") UUID bookId,
            @ModelAttribute BookVolumeFilter filter,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        var bookVolumes = volumeService.getBookVolumes(bookId, filter, userDetails);
        return ResponseEntity.ok(bookVolumes);
    }

    @RateLimited
    @PostMapping
    public ResponseEntity<VolumeDto> createBookVolume(
            @PathVariable("bookId") UUID bookId,
            @Valid @RequestBody CreateVolumeRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        var volume = volumeService.createBookVolume(bookId, userDetails, request);
        return ResponseEntity.ok(volume);
    }


}
