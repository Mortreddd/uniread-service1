package com.uniread.book.repositories;

import com.uniread.book.domain.entities.Volume;
import com.uniread.book.domain.entities.VolumeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, UUID>, JpaSpecificationExecutor<Volume> {

    Optional<Volume> findByIdAndStatus(UUID volumeId, VolumeStatus status);

    Optional<Volume> findByIdAndBookId(UUID bookId, UUID volumeId);

    @Query("""
        SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END
        FROM Volume v
        WHERE v.book.id = :bookId AND v.chaptersCount <= 0
    """)
    boolean hasEmptyChapters(@Param("bookId") UUID bookId);
}
