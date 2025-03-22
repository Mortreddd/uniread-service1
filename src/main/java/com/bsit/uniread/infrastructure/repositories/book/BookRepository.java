package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository
        extends JpaRepository<Book, UUID>, CrudRepository<Book, UUID> {

    /**
     * Get all the  books based on matched genres using genre id
     * @param genres
     * @param pageable
     * @return Pageable of book
     */
    Page<Book> findByGenresIn(List<Genre> genres, Pageable pageable);
    /**
     * Search for books containing title, genre, user's firstName, user's lastName, user's username in any case using given parameter
     * @params title, userUsername, userFirstName, userLastName
     * @return Pageable of book
     */
    Page<Book> findByUserUsernameContainingIgnoreCaseOrTitleContainingIgnoreCaseOrUserFirstNameContainingIgnoreCaseOrUserLastNameContainingIgnoreCase(String title, String username, String firstName, String lastName, Pageable pageable);
}
