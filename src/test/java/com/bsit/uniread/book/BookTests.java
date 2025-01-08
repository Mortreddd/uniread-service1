package com.bsit.uniread.book;

import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
public class BookTests {

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testGetBooks() {
        List<Book> books = bookRepository.findAll();

        assertNotNull(books);
    }

}
