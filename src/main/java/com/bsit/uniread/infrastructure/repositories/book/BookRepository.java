package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The custom methods are based on documentation
 * @source https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html
 */
@Repository
public interface BookRepository
        extends JpaRepository<Book, UUID>, CrudRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    /**
     * Get the book depending on id and it's status
     * @param bookId
     * @param status
     * @return page of books
     */
    Optional<Book> findByIdAndStatus(UUID bookId, BookStatus status);

}
