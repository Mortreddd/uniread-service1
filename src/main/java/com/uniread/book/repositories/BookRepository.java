package com.uniread.book.repositories;

import com.uniread.book.dto.response.BookDetailDto;
import com.uniread.book.dto.response.BookStatsDto;
import com.uniread.book.domain.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository
        extends JpaRepository<Book, UUID>, CrudRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    @Query("""
    SELECT new com.uniread.book.dto.response.BookStatsDto(
        b.id,
        (SELECT AVG(r.rating) FROM BookRating r WHERE r.book.id = b.id),
        (SELECT COUNT(r.id) FROM BookRating r WHERE r.book.id = b.id),
        (SELECT COUNT(l.id) FROM BookLike l WHERE l.book.id = b.id),
        (SELECT COUNT(c.id) FROM Chapter c WHERE c.book.id = b.id),
        (SELECT COUNT(lib.id) > 0 FROM Library lib WHERE lib.book.id = b.id AND lib.user.id = :authUserId)
    )
    FROM Book b
    WHERE b.id IN :bookIds
    """)
    List<BookStatsDto> findStatsForBooks(@Param("bookIds") List<UUID> bookIds, @Param("authUserId") UUID authUserId);

    @Query("""
    SELECT com.uniread.book.dto.response.BookDetailDto(
        b.id,
        b.title,
        b.description,
        com.uniread.book.dto.response.AuthorDto(
            author.id,
            author.username,
            userProfile.firstName,
            userProfile.lastName,
            userProfile.gender,
            userProfile.avatarUrl
        ),
        (SELECT AVG(r.rating) FROM BookRating r WHERE r.book.id = b.id),
        (SELECT COUNT(r.id) FROM BookRating r WHERE r.book.id = b.id),
        b.readCount,
        b.coverPhoto,
        (SELECT COUNT(l.id) FROM BookLike l WHERE l.book.id = b.id),
        (SELECT COUNT(c.id) FROM Chapter c WHERE c.book.id = b.id),
        b.status,
        b.completed,
        b.matured,
        (SELECT COUNT(l) > 0 FROM Library l WHERE l.book.id = b.id AND l.user.id = :authUserId),
        b.createdAt
    )
    FROM Book b
    JOIN b.user author
    JOIN author.profile userProfile
    WHERE b.id = :bookId
    """)
    Optional<BookDetailDto> findBookDetailsById(@Param("bookId") UUID bookId, @Param("authUserId") UUID authUserId);

    @Modifying
    @Query("UPDATE Book SET deletedAt = :deletedAt WHERE id = :bookId")
    void softDeleteBook(@Param("bookId") UUID bookId, @Param("deletedAt") Instant deletedAt);

}
