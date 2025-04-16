package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository
        extends JpaRepository<Book, UUID>, CrudRepository<Book, UUID> {

    /**
     * Get all the books by given status and genre
     * @param status
     * @param pageable
     * @param genres
     * @return page of book
     */
    Page<Book> findByStatusAndTitleContainingIgnoreCaseAndGenresIn(BookStatus status, String title, List<Genre> genres, Pageable pageable);
    /**
     * Get all the books by given genre and matched title
     * @param title
     * @param genres
     * @param pageable
     * @return page of book
     */
    Page<Book> findByTitleContainingIgnoreCaseAndGenresIn(String title, List<Genre> genres, Pageable pageable);


    /**
     * Get all the books by status and Genres
     * @param status
     * @param genres
     * @param pageable
     * @return page of book
     */
    Page<Book> findByStatusAndGenresIn(BookStatus status, List<Genre> genres, Pageable pageable);
    /**
     * Get all the  books based on matched genres using genre id
     *
     * @param genres
     * @param pageable
     * @return Pageable of book
     */
    Page<Book> findByGenresIn(List<Genre> genres, Pageable pageable);
    /**
     * Get the Books based on status or title and given genres
     * @param status
     * @param query
     * @param genres
     * @param pageable
     * @return pageable of book
     */


    /**
     * Search for books containing title, user's firstName, user's lastName, user's username in any case using given parameter
     * @params title, userUsername, userFirstName, userLastName
     * @source https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
     * @return Pageable of book
     */
    Page<Book> findByUserUsernameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCase(String title, String username, String firstName, String lastName, Pageable pageable);

    /**
     * Get the books based on user
     * @param user
     * @param pageable
     * @return page of books
     */
    Page<Book> findByUser(User user, Pageable pageable);

    /**
     * Get the books based on user, status, and searched title
     * @param user
     * @param status
     * @param title
     * @param pageable
     * @return page of books
     */
    Page<Book> findByUserAndStatusAndTitleContainingIgnoreCase(User user, BookStatus status, String title, Pageable pageable);

    /**
     * Get the books based on author and status
     * @param user
     * @param status
     * @param pageable
     * @return page of books
     */
    Page<Book> findByUserAndStatus(User user, BookStatus status, Pageable pageable);

    /**
     * Get the books based on status
     * @param status
     * @param pageable
     * @return page of books
     */
    Page<Book> findByStatus(BookStatus status, Pageable pageable);

    /**
     * Get the book depending on id and it's status
     * @param bookId
     * @param status
     * @return page of books
     */
    Optional<Book> findByIdAndStatus(UUID bookId, BookStatus status);

    /**
     * Get the books by author and searched title
     * @param user
     * @param title
     * @param pageable
     * @return page of books
     */
    Page<Book> findByUserAndTitleContainingIgnoreCase(User user, String title, Pageable pageable);

    /**
     * Get the books by searched title
     * @param title
     * @param pageable
     * @return page of books
     */
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Get the books by selected genre and searched title
     * @param genres
     * @param title
     * @param pageable
     * @return page of books
     */
    Page<Book> findByGenresInAndTitleContainingIgnoreCase(List<Genre> genres, String title, Pageable pageable);
    /**
     * Get all the published books and search for title, genre, author firstName, author lastName, author username
     * @param title
     * @param username
     * @param firstName
     * @param lastName
     * @param status
     * @param pageable
     * @return Pageable of book
     */
    Page<Book> findByUserUsernameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCaseAndStatus(String title, String username, String firstName, String lastName, BookStatus status, Pageable pageable);
}
