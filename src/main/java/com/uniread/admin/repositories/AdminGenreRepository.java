package com.uniread.admin.repositories;

import com.uniread.book.domain.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminGenreRepository extends JpaRepository<Genre, UUID>, JpaSpecificationExecutor<Genre> {

    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID genreId);
    boolean existsByNameIgnoreCase(String name);



}
