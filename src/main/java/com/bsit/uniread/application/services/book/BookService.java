package com.bsit.uniread.application.services.book;

import com.bsit.uniread.application.dto.request.book.BookSearchFilter;
import com.bsit.uniread.application.dto.request.book.UpdateBookRequest;
import com.bsit.uniread.application.dto.response.book.BookDetailsDto;
import com.bsit.uniread.application.dto.response.book.BookDto;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.mappers.book.BookMapper;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.specifications.book.BookSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookMapper mapper;
    private final BookRepository bookRepository;
    private final GenreService genreService;

    public Page<BookDto> searchBooks(CustomUserDetails userDetails, BookSearchFilter filter) {
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getOrderBy())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);

        Specification<Book> bookSpecification =
                Specification.where(BookSpecification.hasDeleted(filter.getDeletedAt()))
                        .and(BookSpecification.hasGenres(filter.getGenres()))
                        .and(BookSpecification.hasStatus(filter.getStatus()))
                        .and(BookSpecification.hasQuery(filter.getQuery()))
                        .and(BookSpecification.hasAuthorById(filter.getAuthorId()));
        if(filter.getGenres() != null && !filter.getGenres().isEmpty()) {

            bookSpecification = bookSpecification.and(BookSpecification.hasGenres(filter.getGenres()));
        }
        return bookRepository.findAll(bookSpecification, pageable)
                .map(b -> {
                    var book = mapper.toDto(b);
                    var tempGenres = b.getGenres();
                    book.setGenres(genreService.mapToDto(tempGenres));
                    return book;
                });

    }

    public BookDetailsDto findBookById(CustomUserDetails userDetails, UUID bookId) {
        UUID authUserId = userDetails != null ? userDetails.getId() : null;
        var book = bookRepository.findBookDetailsById(bookId, authUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Book does not found"));
        var genres = genreService.getBookGenres(book.getId());
        book.setGenres(genres);

        return book;
    }

    public void updateBook(UUID bookId, UpdateBookRequest bookRequest) {
        // TODO : Add a operation for update and might involved with collaborator
    }

    @Transactional
    public void deleteBook(UUID bookId) {
        var book = getBookByIdOrThrow(bookId);
        // if the selected book is already soft deleted
        var isDeleted = book != null && book.getDeletedAt() != null;
        if(isDeleted) return;

        bookRepository.softDeleteBook(bookId);
    }

    public Book getBookByIdOrThrow(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book does not found"));
    }
}
