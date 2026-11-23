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
    private final BookValidator bookValidator;
    private final GenreService genreService;
    private final BookRepository repository;

    @Transactional
    public void createBook(
        CustomUserDetails userDetails,
        CreateBookRequest request
    ) {
        bookValidator.validateBookCreate(request);

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


}
