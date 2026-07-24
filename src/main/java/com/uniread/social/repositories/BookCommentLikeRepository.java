package com.uniread.social.repositories;

import com.uniread.social.domain.entities.BookComment;
import com.uniread.social.domain.entities.BookCommentLike;
import com.uniread.user.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookCommentLikeRepository
        extends JpaRepository<BookCommentLike, UUID>, CrudRepository<BookCommentLike, UUID> {

    boolean existsByUserAndBookComment(User user, BookComment comment);
}
