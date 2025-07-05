package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.book.GenreDto;
import com.bsit.uniread.application.services.book.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiEndpoints.GENRES)
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public ResponseEntity<List<GenreDto>> getGenres() {
        List<GenreDto> genres = genreService.getGenres().stream().map(GenreDto::new).toList();
        return ResponseEntity.ok()
                .body(genres);
    }
}
