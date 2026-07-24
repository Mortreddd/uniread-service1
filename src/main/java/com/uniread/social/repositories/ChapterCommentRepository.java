package com.uniread.social.repositories;

import com.uniread.social.domain.entities.ChapterComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ChapterCommentRepository
        extends JpaRepository<ChapterComment, UUID>, CrudRepository<ChapterComment, UUID> {

}
