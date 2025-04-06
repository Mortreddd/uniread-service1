package com.bsit.uniread.infrastructure.repositories.chapter;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChapterRepository
        extends JpaRepository<Chapter, UUID>, CrudRepository<Chapter, UUID> {

    /**
     * Get the chapters of a book
     * @param book
     * @param pageable
     * @return page of chapters
     */
    Page<Chapter> findByBook(Book book, Pageable pageable);

}
