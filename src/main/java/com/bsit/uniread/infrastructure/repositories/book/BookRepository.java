package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.application.dto.response.book.BookDetailsDto;
import com.bsit.uniread.domain.entities.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookRepository
        extends JpaRepository<Book, UUID>, CrudRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    @EntityGraph(attributePaths = {"user", "user.profile", "genres"})
    Page<Book> findAll(Specification<Book> spec, Pageable pageable);


    @Query("""
    SELECT com.bsit.uniread.application.dto.response.book.BookDetailsDto(
    b.id,
    b.title,
    com.bsit.uniread.application.dto.response.user.SimpleUserInfo(
        author.id,
        author.username,
        CONCAT(userProfile.firstName, ' ', userProfile.lastName),
        userProfile.displayName,
        userProfile.avatarUrl
    ),
    (SELECT AVG(r.ratingValue) FROM BookRating r WHERE r.book.id = b.id),
    (SELECT COUNT(r.id) FROM BookRating r WHERE r.book.id = b.id),
    b.readCount,
    b.coverPhoto,
    b.description,
    (SELECT COUNT(l.id) FROM BookLike l WHERE l.book.id = b.id),
    (SELECT COUNT(c.id) FROM Chapter c WHERE c.book.id = b.id),
    b.status,
    b.completed,
    b.matured,
    NULL,
    (SELECT COUNT(l.id) > 0 FROM Library l WHERE l.book_id = :id AND l.user_id = :authUserId)
    b.createdAt
    )
    FROM Book b
    JOIN b.user author
    JOIN author.profile userProfile
    WHERE b.id = :id
    """)
    Optional<BookDetailsDto> findBookDetailsById(@Param("id") UUID bookId, @Param("authUserId") UUID authUserId);


    @Modifying
    @Query("UPDATE Book SET deletedAt = NOW() WHERE id = :bookId")
    void softDeleteBook(@Param("bookId") UUID bookId);

}
