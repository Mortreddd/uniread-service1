package com.uniread.book.repositories;

import com.uniread.book.domain.entities.Chapter;
import com.uniread.book.domain.entities.ChapterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChapterRepository
        extends JpaRepository<Chapter, UUID>, JpaSpecificationExecutor<Chapter> {

    Page<Chapter> findByVolumeIdAndStatus(UUID volumeId, ChapterStatus status, Pageable pageable);
    Page<Chapter> findByVolumeId(UUID volumeId, Pageable pageable);

    boolean existsByVolumeIdAndStatus(UUID volumeId, ChapterStatus status);
}
