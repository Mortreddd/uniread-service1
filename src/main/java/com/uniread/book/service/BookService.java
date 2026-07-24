package com.uniread.book.service;

import com.uniread.book.dto.request.BookSearchFilter;
import com.uniread.book.dto.request.UpdateBookRequest;
import com.uniread.book.dto.response.AuthorDto;
import com.uniread.book.dto.response.BookDetailDto;
import com.uniread.book.dto.response.BookStatsDto;
import com.uniread.book.domain.entities.Book;
import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.common.exceptions.ResourceNotFoundException;
import com.uniread.book.repositories.BookRepository;
import com.uniread.book.specifications.BookSpecification;
import com.uniread.common.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final GenreService genreService;

    public Page<BookDetailDto> searchBooks(UUID authUserId, BookSearchFilter filter) {
        Sort.Direction direction = "desc".equalsIgnoreCase(filter.getOrderBy())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Sort sort = Sort.by(direction, filter.getSortBy());
        Pageable pageable = PageRequest.of(filter.getPageNo(), filter.getPageSize(), sort);

        var bookSpecification =
                Specification.where(BookSpecification.hasDeleted(filter.getDeletedAt()))
                        .and(BookSpecification.hasGenres(filter.getGenres()))
                        .and(BookSpecification.hasStatus(filter.getStatus()))
                        .and(BookSpecification.hasQuery(filter.getQuery()))
                        .and(BookSpecification.hasAuthorById(filter.getAuthorId()));

        var booksPage = bookRepository.findAll(bookSpecification, pageable);
        if(booksPage.isEmpty()) return Page.empty();
        var bookIds = booksPage.stream()
                .map(Book::getId)
                .toList();
        var statsList = bookRepository.findStatsForBooks(bookIds, authUserId);
        var statsMap = statsList.stream()
                .collect(Collectors.toMap(BookStatsDto::getBookId, s -> s));

        return mapBookDetailDto(booksPage, statsMap);

    }

    public BookDetailDto findBookById(CustomUserDetails userDetails, UUID bookId) {
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

        bookRepository.softDeleteBook(bookId, DateUtil.now());
    }

    public Book getBookByIdOrThrow(UUID bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book does not found"));
    }

    private Page<BookDetailDto> mapBookDetailDto(Page<Book> books, Map<UUID, BookStatsDto> statsMap) {
        return books.map(book -> {
            BookStatsDto stats = statsMap.get(book.getId());

            return BookDetailDto.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .description(book.getDescription())
                    .author(new AuthorDto(
                            book.getUser().getId(),
                            book.getUser().getUsername(),
                            book.getUser().getProfile().getFirstName(),
                            book.getUser().getProfile().getLastName(),
                            book.getUser().getProfile().getGender(),
                            book.getUser().getProfile().getAvatarPhoto()
                    ))
                    .averageRating(stats != null ? stats.getAverageRating().floatValue() : 0.0f)
                    .totalRating(stats != null ? stats.getTotalRating().floatValue() : 0.0f)
                    .readCount(book.getReadCount().longValue())
                    .coverPhoto(book.getCoverPhoto())
                    .totalLikes(stats != null ? stats.getTotalLikes() : 0L)
                    .totalChapters(stats != null ? stats.getTotalChapters() : 0L)
                    .status(book.getStatus())
                    .completed(book.getCompleted())
                    .matured(book.getMatured())
                    .genres(genreService.mapToDto(book.getGenres()))
                    .isAddedToLibrary(stats != null ? stats.getIsAddedToLibrary() : false)
                    .createdAt(book.getCreatedAt())
                    .build();

        });
    }

}
