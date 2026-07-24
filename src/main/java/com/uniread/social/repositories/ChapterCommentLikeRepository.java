package com.uniread.social.repositories;

import com.uniread.social.domain.entities.ChapterCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChapterCommentLikeRepository
        extends JpaRepository<ChapterCommentLike, UUID>, CrudRepository<ChapterCommentLike, UUID> {
}
