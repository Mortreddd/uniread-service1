package com.uniread.admin.controllers;

import com.uniread.admin.dto.request.GenreRequest;
import com.uniread.admin.dto.request.GenreMonitoringFilter;
import com.uniread.admin.dto.response.GenreDetailDto;
import com.uniread.admin.services.AdminGenreService;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.exceptions.InvalidTokenException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/genres")
public class AdminGenreController {

    private final AdminGenreService genreService;

    @GetMapping
    public ResponseEntity<Page<GenreDetailDto>> getGenres(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute GenreMonitoringFilter filter
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var genres = genreService.getGenres(filter);
        return ResponseEntity.ok().body(genres);
    }

    @PostMapping
    public ResponseEntity<GenreDetailDto> createGenre(
            @Valid @RequestBody GenreRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var genre = genreService.createGenre(request, userDetails);
        return ResponseEntity.ok().body(genre);
    }

    @PutMapping(path = "/{genreId}")
    public ResponseEntity<GenreDetailDto> updateGenre(
            @PathVariable("genreId") UUID genreId,
            @Valid @RequestBody GenreRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        var genre = genreService.updateGenre(genreId, request, userDetails);
        return ResponseEntity.ok().body(genre);
    }

    @DeleteMapping(path = "/{genreId}")
    public ResponseEntity<Void> deleteGenre(
            @PathVariable("genreId") UUID genreId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        if(userDetails == null) throw new InvalidTokenException("Session is expired, required to logged in");

        genreService.deleteGenre(genreId, userDetails);

        return ResponseEntity.ok().build();
    }
}
