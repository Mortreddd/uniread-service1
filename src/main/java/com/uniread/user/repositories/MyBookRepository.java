package com.uniread.user.repositories;

import com.uniread.book.domain.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MyBookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    @Query("SELECT b.id FROM Book b WHERE b.user.id = :userId")
    Page<UUID> findBooksByUser(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT DISTINCT b
        FROM Book b
        LEFT JOIN FETCH b.genres
        WHERE b.id IN :ids
    """)
    List<Book> findAllWithGenreByIds(@Param("ids") Collection<UUID> bookIds);

    @EntityGraph(attributePaths = { "genres" })
    @Query("""
        SELECT b
        FROM Book b
        WHERE b.id = :bookId AND b.user.id = :userId
    """)
    Optional<Book> findUserBookById(@Param("userId") UUID userId, @Param("bookId") UUID bookId);


}
