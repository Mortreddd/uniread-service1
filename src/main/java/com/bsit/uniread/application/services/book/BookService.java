package com.bsit.uniread.application.services.book;

import com.bsit.uniread.application.dto.request.book.BookCreationRequest;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.handler.exceptions.book.AlreadyPublishedBookException;
import com.bsit.uniread.infrastructure.handler.publishers.book.BookEventPublisher;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import com.bsit.uniread.infrastructure.utils.ImageDirectory;
import com.bsit.uniread.infrastructure.utils.ImageUtils;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    public Page<Book> getBooks(int pageNo, int pageSize, String query, BookStatus status) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        if(status != null && !StringUtil.isNullOrEmpty(query)) {
            return bookRepository.findByUserUsernameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCaseAndStatus(query, query, query, query, status, pageRequest);
        } else if (!StringUtil.isNullOrEmpty(query)) {
            return bookRepository.findByUserUsernameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCase(query, query, query, query, pageRequest);
        } else if (status != null) {
            return getBooksByStatus(status, pageNo, pageSize);
        }
        return bookRepository.findAll(pageRequest);
    }

    /**
     * Get the user books based on given userId
     * @param user
     * @param pageNo
     * @param pageSize
     * @param query
     * @param status
     * @return Pageable of books
     */
    public Page<Book> getUserBooks(User user, int pageNo, int pageSize, String query, BookStatus status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        if(status != null && StringUtil.isNullOrEmpty(query)) {
            return bookRepository.findByUserAndStatusAndTitleContainingIgnoreCase(user, status, query, pageable);
        } else if (!StringUtil.isNullOrEmpty(query)) {
            return bookRepository.findByUserAndTitleContainingIgnoreCase(user, query, pageable);
        } else if (status != null){
            return bookRepository.findByUserAndStatus(user, status, pageable);
        }
        return bookRepository.findByUser(user, pageable);
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
     * Get the book by id and status
     * @param bookId
     * @param status
     * @return
     */
    public Book getBookById(UUID bookId, BookStatus status) {
        if(status != null) {
            return bookRepository.findByIdAndStatus(bookId, status)
                    .orElseThrow(() -> new ResourceNotFoundException("No current book published"));
        }

        return getBookById(bookId);
    }

    /**
     * Get all the books based on matching genres
     * @param genres
     * @param pageNo
     * @param pageSize
     * @return Pagination of Books
     */
    public Page<Book> getBooksByMultipleGenre(List<Genre> genres, int pageNo, int pageSize, BookStatus status) {
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        if(status != null) {
            return bookRepository.findByStatusAndGenresIn(status, genres, pageable);
        }

        return bookRepository.findByGenresIn(genres, pageable);
    }

    /**
     * Get all the books by genre
     * @param genreId
     * @param pageNo
     * @param pageSize
     * @return Pagination of Books
     */
    public Page<Book> getBooksByGenre(int genreId, int pageNo, int pageSize, String query, BookStatus status) {
        Genre genre = genreService.getGenreById(genreId);
        return getBooksByGenre(genre, pageNo, pageSize, query, status);
    }

    /**
     * Get the books based on selected genre
     * @param genreIds
     * @param pageNo
     * @param pageSize
     * @return page of books
     */
    public Page<Book> getBooksByMultipleGenreById(List<Integer> genreIds, int pageNo, int pageSize, String query, BookStatus status) {
        boolean isNullOrEmpty = Optional.ofNullable(genreIds).map(List::isEmpty).orElse(true);
        if(isNullOrEmpty) {
            if(!StringUtil.isNullOrEmpty(query) && status != null) {
                return getBooks(pageNo, pageSize, query, status);
            } else if(status != null) {
                return getBooksByStatus(status, pageNo, pageSize);
            } else if(query != null) {
                return getBooksByTitle(query, pageNo, pageSize);
            }
        }
        List<Genre> genres = genreService.getGenresByIds(genreIds);
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        if(!StringUtil.isNullOrEmpty(query) && status != null) {
            return bookRepository.findByStatusAndTitleContainingIgnoreCaseAndGenresIn(status, query, genres, pageable);
        } else if(!StringUtil.isNullOrEmpty(query)) {
            return bookRepository.findByGenresInAndTitleContainingIgnoreCase(genres, query, pageable);
        }

        return getBooksByMultipleGenre(genres, pageNo, pageSize, status);

    }

    public Page<Book> getBooksByTitle(String title, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    /**
     * Get all the books by genre
     * @param genre
     * @param pageNo
     * @param pageSize
     * @return Pagination of Books
     */
    public Page<Book> getBooksByGenre(Genre genre, int pageNo, int pageSize, String query, BookStatus status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        if(!StringUtil.isNullOrEmpty(query) && status != null) {
            return bookRepository.findByStatusAndTitleContainingIgnoreCaseAndGenresIn(status, query, List.of(genre), pageable);
        } else if(status != null) {
            return bookRepository.findByStatusAndGenresIn(status, List.of(genre), pageable);
        } else if(!StringUtil.isNullOrEmpty(query)) {
            return bookRepository.findByTitleContainingIgnoreCaseAndGenresIn(query, List.of(genre), pageable);
        }
        return bookRepository.findByGenresIn(List.of(genre), pageable);
    }

    /**
     * Get the books by status
     * @param status
     * @param pageNo
     * @param pageSize
     * @return page of books
     */
    public Page<Book> getBooksByStatus(BookStatus status, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable =  PageRequest.of(pageNo, pageSize, sort);
        return bookRepository.findByStatus(status, pageable);
    }
    /**
     * Create a book
     * @description if any exception occurs, all the created will be deleted
     * @param request
     * @return book
     * @throws IOException
     */
    @Transactional
    public Book createBook(BookCreationRequest request) throws IOException {
        String fileUrl = imageUtils.saveImage(ImageDirectory.COVER, request.getPhoto());
        User author = userService.getUserById(request.getAuthorId());
        List<Genre> genres = genreService.getGenresByIds(request.getGenreIds());
        Book book = bookRepository.save(Book.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .matured(request.getMatured())
                .genres(genres)
                .user(author)
                .coverPhoto(fileUrl)
                .status(BookStatus.DRAFT)
                .readCount(0)
                .completed(false)
                .createdAt(DateUtil.now())
                .build()
        );

        return book;
    }

    /**
     * Update the book for published
     * @param bookId
     * @return book
     */
    @Transactional
    public Book publishedBookById(UUID bookId) {
        Book book = getBookById(bookId);

        if(book.isPublished()) {
            throw new AlreadyPublishedBookException("This book is already published");
        }

        book.setStatus(BookStatus.PUBLISHED);
        book.setUpdatedAt(DateUtil.now());

        publisher.newPublishBook(book);

        return bookRepository.save(book);
    }

    /**
     * Delete the book
     * @param bookId
     * @throws IOException
     */
    @Transactional
    public void deleteBookById(UUID bookId) throws IOException {
        Book book = getBookById(bookId);

        try {
            if(imageUtils.deleteImage(book.getCoverPhoto())) {
                bookRepository.deleteById(bookId);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("Unable to find cover photo of the book");
        }
    }


}
