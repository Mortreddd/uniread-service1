package com.bsit.uniread.book;

import com.bsit.uniread.application.dto.request.book.BookCreationRequest;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.application.services.book.GenreService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.book.Genre;
import com.bsit.uniread.domain.entities.user.CustomUserDetails;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import com.bsit.uniread.infrastructure.utils.ImageUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    BookRepository bookRepository;

    @Mock
    ImageUtils imageUtils;
    @Mock
    UserService userService;

    @Mock
    GenreService genreService;

    @InjectMocks
    BookService bookService;

    List<Book> books;

    int pageSize = 10;
    int pageNumber = 0;
    String query = "";
    List<Integer> genreIds = List.of();
    String sortBy = "asc";
    String orderBy = "createdAt";
    String startDate = null;
    String endDate = null;
    String deletedAt = null;

    @BeforeEach
    void setUp() {
        this.books = List.of(
                Book.builder()
                        .title("Harry Potter")
                        .description("No description")
                        .completed(false)
                        .matured(false)
                        .status(BookStatus.DRAFT)
                        .bannedAt(null)
                        .publishedAt(null)
                        .coverPhoto(null)
                        .deletedAt(null)
                        .build(),

                Book.builder()
                        .title("Beginning after the end")
                        .description("No description")
                        .completed(true)
                        .matured(true)
                        .status(BookStatus.PUBLISHED)
                        .bannedAt(null)
                        .publishedAt(DateUtil.now())
                        .coverPhoto(null)
                        .deletedAt(null)
                        .build()
        );

    }

//    @Test
//    @DisplayName("Test get paginated books")
//    void shouldGetBooks() {
//        BookStatus status = null;
//        Sort.Direction direction = this.sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
////        Pageable pageable  = PageRequest.of(this.pageNumber, this.pageSize, Sort.by(direction, this.orderBy));
//
//        Pageable pageable = PageRequest.of(this.pageNumber, this.pageSize, Sort.by(direction, this.orderBy));
//        Page<Book> bookPage = new PageImpl<>(this.books, pageable, this.books.size());
//        Mockito.doReturn(bookPage)
//                .when(bookRepository)
//                .findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
//
//        Page<Book> result = bookService.getBooks(
//                this.pageNumber,
//                this.pageSize,
//                this.query,
//                this.genreIds,
//                status,
//                this.sortBy,
//                this.orderBy,
//                this.startDate,
//                this.endDate,
//                this.deletedAt
//        );
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(bookPage, result);
//        Assertions.assertEquals(2, result.getContent().size());
//    }
//
//    @Test
//    @DisplayName("Test get the published books")
//    void shouldGetPublished() {
//        BookStatus status = BookStatus.PUBLISHED;
//        Sort.Direction direction = this.sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
////        Pageable pageable  = PageRequest.of(this.pageNumber, this.pageSize, Sort.by(direction, this.orderBy));
//
//        Pageable pageable = PageRequest.of(this.pageNumber, this.pageSize, Sort.by(direction, this.orderBy));
//        Page<Book> bookPage = new PageImpl<>(this.books, pageable, this.books.size());
//        Mockito.doReturn(bookPage)
//                .when(bookRepository)
//                .findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
//
//        Page<Book> result = bookService.getBooks(
//                this.pageNumber,
//                this.pageSize,
//                this.query,
//                this.genreIds,
//                status,
//                this.sortBy,
//                this.orderBy,
//                this.startDate,
//                this.endDate,
//                this.deletedAt
//        );
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(1, result.getContent().size());
//    }
//
//    @Test
//    @DisplayName("Test get unpublished books")
//    void shouldGetUnpublishedBook() {
//        BookStatus status = BookStatus.DRAFT;
//        Sort.Direction direction = this.sortBy.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Pageable pageable  = PageRequest.of(this.pageNumber, this.pageSize, Sort.by(direction, this.orderBy));
//
//        Pageable pageable = PageRequest.of(this.pageNumber, this.pageSize, Sort.by(direction, this.orderBy));
//        Page<Book> bookPage = new PageImpl<>(this.books, pageable, this.books.size());
//        Mockito.doReturn(bookPage)
//                .when(bookRepository)
//                .findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable));
//
//        Page<Book> result = bookService.getBooks(
//                this.pageNumber,
//                this.pageSize,
//                this.query,
//                this.genreIds,
//                status,
//                this.sortBy,
//                this.orderBy,
//                this.startDate,
//                this.endDate,
//                this.deletedAt
//        );
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(1, result.getContent().size());
//    }

    @Test
    @DisplayName("Test can add book")
    void shouldCreateBook() throws Exception {

        User owner = User.builder()
                .id(UUID.randomUUID())
                .firstName("Juan")
                .lastName("Dela Cruz")
                .email("juandelacruz@gmail.com")
                .role(Role.USER)
                .username("juandelacruz")
                .gender(Gender.MALE)
                .password("12345678")
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(owner);
        BookCreationRequest request = new BookCreationRequest();
        request.setTitle("Harry Potter");
        request.setDescription("This is description");
        request.setMatured(false);
        request.setGenreIds(List.of());

        Book storedBook = bookService.createBook(request, userDetails);

        Assertions.assertNotNull(storedBook);
        Assertions.assertEquals(request.getTitle(), storedBook.getTitle());

    }


    @Test
    @DisplayName("Test can soft delete book")
    void shouldSoftDeleteBook() throws IOException {
        User owner = User.builder()
                .id(UUID.randomUUID())
                .firstName("Juan")
                .lastName("Dela Cruz")
                .email("juandelacruz@gmail.com")
                .role(Role.USER)
                .username("juandelacruz")
                .gender(Gender.MALE)
                .password("12345678")
                .build();

        Book book = Book.builder()
                .title("Harry Potter")
                .description("This is description")
                .status(BookStatus.DRAFT)
                .user(owner)
                .bannedAt(null)
                .matured(true)
                .genres(List.of())
                .completed(false)
                .readCount(0)
                .build();


        Mockito.when(bookRepository.save(book)).thenReturn(book);
        bookService.deleteBookById(book.getId());

        Assertions.assertNotNull(book.getDeletedAt());

    }

}
