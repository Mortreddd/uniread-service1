package com.uniread.social.repositories;

import com.uniread.social.domain.entities.ChapterLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChapterLikeRepository
        extends JpaRepository<ChapterLike, UUID> {
}
