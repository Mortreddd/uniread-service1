package com.uniread.admin.repositories;

import com.uniread.book.domain.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminTagRepository extends JpaRepository<Tag, UUID>,JpaSpecificationExecutor<Tag> {

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID tagId);
    boolean existsByNameIgnoreCase(String name);
}
