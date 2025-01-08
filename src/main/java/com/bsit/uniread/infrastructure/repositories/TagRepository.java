package com.bsit.uniread.infrastructure.repositories;

import com.bsit.uniread.domain.entities.book.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository
        extends JpaRepository<Tag, Integer>, CrudRepository<Tag, Integer> {
}
