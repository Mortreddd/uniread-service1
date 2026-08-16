package com.uniread.social.repositories;

import com.uniread.social.domain.entities.BookLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookLikeRepository
        extends JpaRepository<BookLike, UUID> {
}
