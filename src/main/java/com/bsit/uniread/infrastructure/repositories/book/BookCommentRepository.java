package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.domain.entities.book.BookComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookCommentRepository
        extends JpaRepository<BookComment, UUID>, CrudRepository<BookComment, UUID> {
}
