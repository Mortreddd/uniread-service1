package com.bsit.uniread.infrastructure.repositories.paragraph;

import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.domain.entities.paragraph.Paragraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface ParagraphRepository extends CrudRepository<Paragraph, UUID> {
    List<Paragraph> findByChapter(Chapter chapter);
}
