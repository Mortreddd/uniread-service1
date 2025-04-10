package com.bsit.uniread.application.services.book;

import com.bsit.uniread.application.dto.request.book.BookCreationRequest;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.publishers.book.BookEventPublisher;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import com.bsit.uniread.infrastructure.utils.ImageDirectory;
import com.bsit.uniread.infrastructure.utils.ImageUtils;
import io.netty.util.internal.StringUtil;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final ImageUtils imageUtils;
    private final BookRepository bookRepository;
    private final BookEventPublisher publisher;
    private final GenreService genreService;
    private final UserService userService;


    /**
     * Get the books based on pageNumber, pageSize, and query if not empty or null.
     * The books queried that matches the query
     * @param pageNo
     * @param pageSize:
     * @param query
     * @return Pagination of Books
     */
    public Page<Book> getBooks(int pageNo, int pageSize, @Nullable String query) {

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
        boolean isNullOrEmpty = Optional.ofNullable(genreIds).map(List::isEmpty).orElse(true);
        if(isNullOrEmpty) {
            return getBooks(pageNo, pageSize, null);
        }
        List<Genre> genres = genreService.getGenresByIds(genreIds);
        return getBooksByMultipleGenre(genres, pageNo, pageSize);
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

    public Book createBook(BookCreationRequest request) throws IOException {
        String fileUrl = imageUtils.saveImage(ImageDirectory.COVER, request.getCoverPhoto());
        User author = userService.getUserById(request.getAuthorId());
        List<Genre> genres = genreService.getGenresByIds(Arrays.asList(request.getGenreIds()));
        Book book = bookRepository.save(Book.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .matured(request.getMatured())
                .genres(genres)
                .user(author)
                .coverPhoto(fileUrl)
                .readCount(0)
                .completed(false)
                .createdAt(DateUtil.now())
                .build()
        );

//        publisher.newBookCreated(book);

        return book;
    }


}
