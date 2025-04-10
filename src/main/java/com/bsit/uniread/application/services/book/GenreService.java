package com.bsit.uniread.application.services.book;

import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    /**
     * Get all the genres
     * @return list of Genres
     */
    public List<Genre> getGenres() {
        return genreRepository.findAll();
    }

    /**
     * Get the genre based on id
     * @param genreId
     * @return Genre
     */
    public Genre getGenreById(int genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve selected genre"));
    }

    /**
     * Get all the genres by matched id
     * @param genreIds
     * @return list of genres
     */
    public List<Genre> getGenresByIds(List<Integer> genreIds) {
        return genreRepository.findAllById(genreIds);
    }

}
