package com.bsit.uniread.infrastructure.repositories.chapter;

import com.bsit.uniread.domain.entities.chapter.ChapterComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ChapterCommentRepository
        extends JpaRepository<ChapterComment, UUID>, CrudRepository<ChapterComment, UUID> {

}
