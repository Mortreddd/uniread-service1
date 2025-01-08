package com.bsit.uniread.infrastructure.repositories.chapter;

import com.bsit.uniread.domain.entities.chapter.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChapterRepository
        extends JpaRepository<Chapter, UUID>, CrudRepository<Chapter, UUID> {
}
