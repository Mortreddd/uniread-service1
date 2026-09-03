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
        extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

    @Modifying
    @Query("UPDATE Book SET deletedAt = :deletedAt WHERE id = :bookId")
    void softDeleteBook(@Param("bookId") UUID bookId, @Param("deletedAt") Instant deletedAt);

}
