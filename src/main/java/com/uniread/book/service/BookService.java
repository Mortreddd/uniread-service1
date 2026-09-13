package com.uniread.book.service;

import com.uniread.auth.domain.entities.CustomUserDetails;
import com.uniread.auth.domain.entities.User;
import com.uniread.book.domain.entities.Book;
import com.uniread.book.domain.entities.BookStatus;
import com.uniread.book.dto.request.CreateBookRequest;
import com.uniread.book.repositories.BookRepository;
import com.uniread.common.exceptions.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final ApplicationEventPublisher publisher;
    private final BookCoverService coverService;
    private final GenreService genreService;
    private final BookRepository repository;

    @Transactional
    public void createBook(
        CustomUserDetails userDetails,
        CreateBookRequest request
    ) {
        validateBookCreate(request);

        var bookId = UUID.randomUUID();

        var uploadResult = coverService.uploadCoverPhoto(request.getCover(), bookId);
        var publicId = (String) uploadResult.get("public_id");

        var author = User.builder().id(userDetails.getId()).build();
        var genres = genreService.getGenresByIds(request.getGenres());

        // TODO: Add the collaborators including the notification event listener for the selected collaborators
        var book = Book.builder()
                .id(bookId)
                .title(request.getTitle())
                .user(author)
                .description(request.getDescription())
                .matured(request.getMatured())
                .genres(new HashSet<>(genres))
                .coverPublicId(publicId)
                .status(BookStatus.DRAFT)
                .build();

        // publisher.publishEvent(new NewDraftBookEvent(book.getId(), book));
        repository.save(book);

    }

    public void validateBookCreate(CreateBookRequest request) {
        if(request == null) {
            throw new ValidationException("Unable to proceed with the request");
        }

        if(request.getCover() == null) {
            throw new ValidationException("Book cover is required");
        }

        if(request.getTitle().isBlank()) {
            throw new ValidationException("Title of the book is required");
        }

        if(request.getDescription().isBlank()) {
            throw new ValidationException("Synopsis of the book is required");
        }

        if(request.getGenres() == null || request.getGenres().isEmpty()) {
            throw new ValidationException("Book genre must contain at least 1 genre");
        }

        if(request.getMatured() == null) {
            throw new ValidationException("Book mature content must not be null");
        }
    }
}
