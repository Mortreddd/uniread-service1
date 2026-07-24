package com.uniread.book.repositories;

import com.uniread.book.domain.entities.Chapter;
import com.uniread.book.domain.entities.Paragraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface ParagraphRepository extends CrudRepository<Paragraph, UUID> {
    List<Paragraph> findByChapter(Chapter chapter);
}
