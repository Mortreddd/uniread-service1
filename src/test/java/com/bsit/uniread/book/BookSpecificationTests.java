package com.bsit.uniread.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.specifications.book.BookSpecification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class BookSpecificationTests {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testBookSpecificationWithoutPageable() {

        int pageNo = 0;
        int pageSize = 10;
        String query =  "r";
        BookStatus status = BookStatus.PUBLISHED;
        String sortBy = "createdAt";
        String orderBy = "desc";
        String startDate = null;
        String endDate = null;
        String deletedAt = null;

        Specification<Book> bookSpecification = Specification
                .where(BookSpecification.hasQuery(query))
                .and(BookSpecification.hasDeleted(deletedAt))
                .and(BookSpecification.hasStatus(status));

        List<Book> books = bookRepository.findAll(bookSpecification);



        assertNotNull(books.getFirst());
    }

    @Test
    public void testBookSpecificationWithPageable() {
        int pageNo = 0;
        int pageSize = 10;
        String query =  "r";
        BookStatus status = BookStatus.PUBLISHED;
        String sortBy = "createdAt";
        String orderBy = "desc";
        String startDate = null;
        String endDate = null;
        String deletedAt = null;

        Sort sort = Sort.by(Sort.Direction.DESC, sortBy);

        Specification<Book> bookSpecification = Specification
                .where(BookSpecification.hasQuery(query))
                .and(BookSpecification.hasDeleted(deletedAt))
                .and(BookSpecification.hasStatus(status));

        Page<Book> books = bookRepository.findAll(bookSpecification, PageRequest.of(pageNo, pageSize, sort));

        assertNotNull(books);


    }

}
