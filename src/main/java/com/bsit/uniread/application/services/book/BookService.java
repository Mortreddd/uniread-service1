package com.bsit.uniread.application.services.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.exceptions.BookNotFoundException;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


    /**
     * Get the books based on pageNumber, pageSize
     * @param pageNo
     * @param pageSize
     * @param bookTitle
     * @return Pagination of Books
     */
    public Page<Book> getBooks(int pageNo, int pageSize, @Nullable String bookTitle) {
        /**
         *  Decrement the pageNo for the index
         * @source https://stackoverflow.com/questions/69409082/spring-boot-pagination-pagingandsortingrepository-not-returning-any-results
         */
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        if(bookTitle != null) {
            return bookRepository.findByTitleContaining(bookTitle, pageRequest);
        }

        return bookRepository.findAll(pageRequest);
    }

    /**
     * Get the book based on id
     * @param bookId
     * @return
     */
    public Book getBookById(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Unable to get the book"));
    }


}
