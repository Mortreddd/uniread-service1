package com.bsit.uniread.application.services.chapter;

import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.chapter.Chapter;
import com.bsit.uniread.infrastructure.repositories.chapter.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final BookService bookService;
    private final ChapterRepository chapterRepository;

    /**
     * Get the chapters of book
     * @param bookId
     * @param pageNo
     * @param pageSize
     * @return page of chaptes
     */
    public Page<Chapter> getBookChapters(UUID bookId, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Book book = bookService.getBookById(bookId);
        return chapterRepository.findByBook(book, pageable);
    }
}
