package com.bsit.uniread.application.services.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.book.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;
    private final BookService bookService;

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
    public Genre getGenre(int genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve selected genre"));
    }

    /**
     * Get all the books by genre
     * @param genreId
     * @param pageNo
     * @param pageSize
     * @return Pagination of Books
     */
    public Page<Book> getBooksByGenre(int genreId, int pageNo, int pageSize) {
        Genre genre = getGenre(genreId);
        return bookService.getBooksByGenre(genre, pageNo, pageSize);
    }
}
