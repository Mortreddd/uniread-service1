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
import com.bsit.uniread.infrastructure.specifications.BookSpecification;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import com.bsit.uniread.infrastructure.utils.ImageDirectory;
import com.bsit.uniread.infrastructure.utils.ImageUtils;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
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
     * @param genreIds
     * @param status
     * @param sortBy
     * @param orderBy
     * @param startDate
     * @param endDate
     * @param deletedAt
     * @return Pagination of Books
     */
    @Transactional(readOnly = true)
    public Page<Book> getBooks(
            int pageNo,
            int pageSize,
            String query,
            List<Integer>  genreIds,
            BookStatus status,
            String sortBy,
            String orderBy,
            String startDate,
            String endDate,
            String deletedAt
    ) {

        Sort.Direction direction = sortBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, orderBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Book> bookSpecification = Specification
                .where(BookSpecification.hasDeleted(deletedAt))
                .and(BookSpecification.hasGenres(genreIds))
                .and(BookSpecification.hasStatus(status))
                .and(BookSpecification.hasQuery(query));

        return bookRepository.findAll(bookSpecification, pageable);
    }

    /**
     * Get the user books based on given userId
     * @param user
     * @param pageNo
     * @param pageSize
     * @param query
     * @param status
     * @param sortBy
     * @param orderBy
     * @return Pageable of books
     */
    @Transactional(readOnly = true)
    public Page<Book> getUserBooks(User user, int pageNo, int pageSize, String query, BookStatus status, String sortBy, String orderBy) {
        Sort.Direction direction = sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, orderBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Specification<Book> bookSpecification = Specification
                .where(BookSpecification.hasAuthor(user))
                .and(BookSpecification.hasQuery(query))
                .and(BookSpecification.hasStatus(status));

        return bookRepository.findAll(bookSpecification, pageable);
    }

    /**
     * Get the book based on id
     * @param bookId
     * @return book
     */
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public Book getBookById(UUID bookId, BookStatus status) {
        if(status != null) {
            return bookRepository.findByIdAndStatus(bookId, status)
                    .orElseThrow(() -> new ResourceNotFoundException("No current book published"));
        }

        return getBookById(bookId);
    }


    /**
     * Create a book
     * @description if any exception occurs, all the created will be deleted
     * @param request
     * @return book
     * @throws IOException
     */
    public Book createBook(BookCreationRequest request) throws IOException {
        String fileUrl = imageUtils.saveImage(ImageDirectory.COVER, request.getPhoto());
        User author = userService.getUserById(request.getAuthorId());
        List<Genre> genres = genreService.getGenresByIds(request.getGenreIds());
        Book book = Book.builder()
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
                .build();

        return save(book);
    }

    /**
     * Update the book for published
     * @param bookId
     * @return book
     */
    public Book publishedBookById(UUID bookId) {
        Book book = getBookById(bookId);

        // Checks the book if already published
        if(book.getIsPublished()) {
            throw new AlreadyPublishedBookException("This book is already published");
        }

        book.setStatus(BookStatus.PUBLISHED);
        book.setUpdatedAt(DateUtil.now());

        publisher.newPublishBook(book);

        return save(book);
    }

    @Transactional
    public Book save(Book book) {
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
