package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.domain.entities.book.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookLikeRepository
        extends JpaRepository<BookLike, UUID>, CrudRepository<BookLike, UUID> {
}
