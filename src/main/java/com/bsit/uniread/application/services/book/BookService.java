package com.bsit.uniread.application.services.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import io.netty.util.internal.StringUtil;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Get the books based on pageNumber, pageSize, and query if not empty or null.
     * The books queried that matches the query
     * @param pageNo
     * @param pageSize:
     * @param query
     * @return Pagination of Books
     */
    public Page<Book> getBooks(int pageNo, int pageSize, @Nullable String query) {
        /**
         *  Decrement the pageNo for the index
         * @source https://stackoverflow.com/questions/69409082/spring-boot-pagination-pagingandsortingrepository-not-returning-any-results
         */
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        if(!StringUtil.isNullOrEmpty(query)) {
            return bookRepository.findByUserUsernameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCase(query, query, query, query, pageRequest);
        }

        return bookRepository.findAll(pageRequest);
    }

    /**
     * Get the user books based on given userId
     * @param user
     * @param pageNo
     * @param pageSize
     * @param query
     * @return Pageable of books
     */
    public Page<Book> getUserBooks(User user, int pageNo, int pageSize, String query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        if(StringUtil.isNullOrEmpty(query)) {
            return bookRepository.findByUser(user, pageable);
        }
        return bookRepository.findByUserAndTitleContainingIgnoreCase(user, query, pageable);
    }

    /**
     * Get the book based on id
     * @param bookId
     * @return book
     */
    public Book getBookById(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Unable to retrieve the book"));
    }

    /**
     * Get all the books based on matching genres
     * @param genres
     * @param pageNo
     * @param pageSize
     * @return Pagination of Books
     */
    public Page<Book> getBooksByMultipleGenre(List<Genre> genres, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return bookRepository.findByGenresIn(genres, pageable);
    }

    /**
     * Get all the books by genre
     * @param genre
     * @param pageNo
     * @param pageSize
     * @return Pagination of Books
     */
    public Page<Book> getBooksByGenre(Genre genre, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return bookRepository.findByGenresIn(List.of(genre), pageable);
    }



}
