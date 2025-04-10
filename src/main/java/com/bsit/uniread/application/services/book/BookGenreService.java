package com.bsit.uniread.application.services.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookGenreService {

    private final GenreService genreService;
    private final BookService bookService;
    /**
     * Get all the books by genre
     * @param genreId
     * @param pageNo
     * @param pageSize
     * @return Pagination of Books
     */
    public Page<Book> getBooksByGenre(int genreId, int pageNo, int pageSize) {
        Genre genre = genreService.getGenreById(genreId);
        return getBooksByGenre(genre, pageNo, pageSize);
    }

    /**
     * Get the books based on selected genre
     * @param genreIds
     * @param pageNo
     * @param pageSize
     * @return page of books
     */
    public Page<Book> getBooksByMultipleGenreById(List<Integer> genreIds, int pageNo, int pageSize) {
        return bookService.getBooksByMultipleGenreById(genreIds, pageNo, pageSize);
    }
    /**
     * Get all the books by genre
     * @param genre
     * @param pageNo
     * @param pageSize
     * @return Pagination of Books
     */
    public Page<Book> getBooksByGenre(Genre genre, int pageNo, int pageSize) {
        return bookService.getBooksByGenre(genre, pageNo, pageSize);
    }
}
