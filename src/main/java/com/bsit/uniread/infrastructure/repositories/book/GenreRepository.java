package com.bsit.uniread.infrastructure.repositories.book;

import com.bsit.uniread.domain.entities.book.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository
        extends JpaRepository<Genre, Integer>, CrudRepository<Genre, Integer> {

    Optional<Genre> findByName(String name);
}
