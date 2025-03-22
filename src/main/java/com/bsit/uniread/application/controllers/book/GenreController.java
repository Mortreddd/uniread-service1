package com.bsit.uniread.application.controllers.book;

import com.bsit.uniread.application.constants.ApiEndpoints;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.application.dto.response.book.GenreDto;
import com.bsit.uniread.application.services.book.GenreService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping(path = "/options")
    public ResponseEntity<Page<BookDto>> getBooksByMultipleGenre(
            @RequestParam(name = "genres", required = false) List<Integer> genres,
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        Page<BookDto> books = genreService.getBooksByMultipleGenreById(genres, pageNo, pageSize).map(BookDto::new);
        return ResponseEntity.ok().body(books);
    }


    @GetMapping(path = "/{genreId}/books")
    public ResponseEntity<Page<BookDto>> getBooksByGenre(
            @PathVariable(name = "genreId") int genreId,
            @RequestParam(name = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        Page<BookDto> books = genreService.getBooksByGenre(genreId, pageNo, pageSize).map(BookDto::new);
        return ResponseEntity.ok()
                .body(books);
    }
}
