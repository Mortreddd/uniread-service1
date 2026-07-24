package com.uniread.book.repositories;

import com.uniread.book.domain.entities.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChapterRepository
        extends JpaRepository<Chapter, UUID>, CrudRepository<Chapter, UUID>, JpaSpecificationExecutor<Chapter> {
}
