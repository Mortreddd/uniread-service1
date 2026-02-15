package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.application.dto.response.book.BookDetailDto;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT 
        new com.bsit.uniread.application.dto.response.book.BookDetailDto(
            (SELECT COALESCE(COUNT(c.id), 0)
             FROM Chapter c
             WHERE c.book.id = :bookId AND c.status = 'PUBLISHED'),
             
             (SELECT COALESCE(SUM(bc.rating), 0)
             FROM BookComment bc
             WHERE bc.book.id = :bookId),
                      
            (SELECT COALESCE(COUNT(bl.id), 0) 
             FROM BookLike bl 
             WHERE bl.book.id = :bookId),
             
             book.readCount,
                          
            (SELECT CASE 
                      WHEN COUNT(b.id) > 0 THEN true ELSE false 
                    END
             FROM Book b 
             WHERE b.id = :bookId AND b.user.id = :userId),
             
             
            (SELECT CASE 
                      WHEN COUNT(c.id) > 0 THEN true ELSE false
                    END
             FROM Collaborator c 
             WHERE c.book.id = :bookId AND c.user.id = :userId),
             
            (SELECT CASE 
                      WHEN COUNT(bm.id) > 0 THEN true ELSE false 
                    END
             FROM Bookmark bm
             JOIN bm.paragraph p
             JOIN p.chapter c
             WHERE c.book.id = :bookId AND bm.user.id = :userId
             )
        )
        FROM Book book
        WHERE book.id = :bookId
    """)
    Optional<BookDetailDto> findBookDetailByIdAndUserId(@Param("bookId") UUID bookId, @Param("userId") UUID userId);

    @Query("""
        SELECT 
        new com.bsit.uniread.application.dto.response.book.BookDetailDto(
            (SELECT COALESCE(COUNT(c.id), 0)
             FROM Chapter c
             WHERE c.book.id = :bookId AND c.status = 'PUBLISHED'),
             
             (SELECT COALESCE(SUM(bc.rating), 0)
             FROM BookComment bc
             WHERE bc.book.id = :bookId),
                      
            (SELECT COALESCE(COUNT(bl.id), 0) 
             FROM BookLike bl 
             WHERE bl.book.id = :bookId),
             book.readCount,              
            false, 
            false,
            false
        )
        FROM Book book
        WHERE book.id = :bookId
    """)
    Optional<BookDetailDto> findBookDetailById(@Param("bookId") UUID bookId);
}
