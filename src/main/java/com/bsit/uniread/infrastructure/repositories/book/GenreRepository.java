package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.domain.entities.book.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepository
        extends JpaRepository<Genre, Integer>, CrudRepository<Genre, Integer> {
    List<Genre> findByBookId(UUID bookId);
}
