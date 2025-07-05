package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.domain.entities.book.BookComment;
import com.bsit.uniread.domain.entities.book.BookCommentLike;
import com.bsit.uniread.domain.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookCommentLikeRepository
        extends JpaRepository<BookCommentLike, UUID>, CrudRepository<BookCommentLike, UUID> {

    boolean existsByUserAndBookComment(User user, BookComment comment);
}
