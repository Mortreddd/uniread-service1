package com.uniread.book.repositories;

import com.uniread.book.domain.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GenreRepository
        extends JpaRepository<Genre, Integer> {
    List<Genre> findByBooksId(UUID bookId);
}
