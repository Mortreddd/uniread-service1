package com.bsit.uniread.collaborator;

import com.bsit.uniread.application.dto.request.collaborator.ApproveCollaborationRequest;
import com.bsit.uniread.application.dto.request.collaborator.NewCollaboratorRequest;
import com.bsit.uniread.application.services.book.BookService;
import com.bsit.uniread.application.services.collaborator.CollaboratorService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.collaborator.CollaboratorPermission;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.collaborator.CollaboratorRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
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

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CollaboratorServiceTests {

    @Mock
    CollaboratorRepository collaboratorRepository;

    @Mock
    BookService bookService;

    @Mock
    UserService userService;

    @InjectMocks
    CollaboratorService collaboratorService;

    List<Collaborator> collaborators;

    private int defaultPageNo = 0;
    private int defaultPageSize = 10;

    private static final UUID COLLABORATOR_1_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID COLLABORATOR_2_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    private static final UUID COLLABORATOR_3_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
    private static final UUID BOOK_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174003");


    User collaborator1 = User.builder()
            .id(COLLABORATOR_1_ID)
            .firstName("Juan")
            .lastName("Dela Cruz")
            .username("juan")
            .gender(Gender.MALE)
            .role(Role.USER)
            .password("12345678")
            .email("juandelacruz@gmail.com")
            .bannedAt(null)
            .build();

    User collaborator2 = User.builder()
            .id(COLLABORATOR_2_ID)
            .firstName("Antonio")
            .lastName("Dela Cruz")
            .username("antonio")
            .gender(Gender.MALE)
            .role(Role.USER)
            .password("12345678")
            .email("antoniodelacruz@gmail.com")
            .bannedAt(null)
            .build();

    User collaborator3 = User.builder()
            .id(COLLABORATOR_3_ID)
            .firstName("Luna")
            .lastName("Macapagal")
            .username("luna")
            .gender(Gender.FEMALE)
            .role(Role.USER)
            .password("12345678")
            .email("lunamacapagal@gmail.com")
            .bannedAt(null)
            .build();

    Book book = Book.builder()
            .id(BOOK_ID)
            .title("Harry Potter")
            .description("No description")
            .completed(false)
            .matured(false)
            .status(BookStatus.DRAFT)
            .bannedAt(null)
            .publishedAt(null)
            .coverPhoto(null)
            .deletedAt(null)
            .build();


    @BeforeEach
    void setUp() {

        this.collaborators = List.of(
                Collaborator.builder()
                        .id(UUID.randomUUID())
                        .user(this.collaborator1)
                        .book(book)
                        .createdAt(DateUtil.now())
                        .permissions(List.of())
                        .bannedAt(null)
                        .unbannedAt(null)
                        .build(),

                Collaborator.builder()
                        .id(UUID.randomUUID())
                        .user(this.collaborator2)
                        .book(book)
                        .permissions(List.of())
                        .createdAt(DateUtil.now())
                        .bannedAt(null)
                        .unbannedAt(null)
                        .build()
        );
    }


    @Test
    @DisplayName(value = "Get the collaborators of a book")
    void shouldGetBookCollaborators() {

        Pageable pageable = PageRequest.of(this.defaultPageNo, this.defaultPageSize);
        Page<Collaborator> collaboratorPage = new PageImpl<>(this.collaborators, pageable, this.collaborators.size());

        Mockito.when(collaboratorRepository.findAll(ArgumentMatchers.any(Specification.class), ArgumentMatchers.eq(pageable))).thenReturn(collaboratorPage);

        Page<Collaborator> result = collaboratorService.getBookCollaborators(book.getId(), this.defaultPageNo, this.defaultPageSize);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.getContent().size());
    }

//    @Test
//    @DisplayName(value = "Get the collaborator of a book by id")
//    void shouldGetBookCollaboratorById() {
//        Collaborator collaborator = Collaborator.builder()
//                .id(UUID.randomUUID())
//                .user(collaborator3)
//                .book(book)
//                .permissions(List.of())
//                .bannedAt(null)
//                .unbannedAt(null)
//                .build();
//
//        Mockito.when(collaboratorService.save(ArgumentMatchers.any(Collaborator.class))).thenReturn(collaborator);
//
//        Collaborator result = collaboratorService.getCollaboratorById(collaborator.getId());
//        Collaborator fake = collaboratorService.getCollaboratorById(UUID.randomUUID());
//
//        Assertions.assertNotNull(result);
//        Assertions.assertEquals(collaborator.getId(), result.getId());
//        Assertions.assertEquals(collaborator.getUser().getId(), result.getUser().getId());
//        Assertions.assertNull(fake);
//        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
//            collaboratorService.getCollaboratorById(UUID.randomUUID());
//        });
//
//    }

    @Test
    @DisplayName(value = "Create a new collaborator")
    void shouldCreateBookCollaborator() {
        Collaborator collaborator = Collaborator.builder()
                .id(UUID.randomUUID())
                .user(collaborator3)
                .book(book)
                .permissions(List.of())
                .bannedAt(null)
                .unbannedAt(null)
                .build();

        Mockito.when(bookService.getBookById(book.getId())).thenReturn(book);
        Mockito.when(userService.getUserById(collaborator3.getId())).thenReturn(collaborator3);
        Mockito.when(collaboratorRepository.save(ArgumentMatchers.any(Collaborator.class))).thenReturn(collaborator);

        ApproveCollaborationRequest request = new ApproveCollaborationRequest();
        request.setPermissions(new CollaboratorPermission[]{});
        request.setUserCollaboratorId(collaborator3.getId());


        Collaborator result = collaboratorService.createCollaborator(book.getId(), request);
        Assertions.assertNotNull(result.getUser());
        Assertions.assertEquals(collaborator3.getFirstName(), result.getUser().getFirstName());
    }

    @Test
    @DisplayName(value = "Test ban a collaborator")
    void shouldBanBookCollaborator() {
        Collaborator collaborator = Collaborator.builder()
                .id(UUID.randomUUID())
                .user(collaborator3)
                .book(book)
                .permissions(List.of())
                .bannedAt(null)
                .unbannedAt(null)
                .build();

        collaborator.setBannedAt(DateUtil.now());
        collaborator.setUnbannedAt(DateUtil.now().plusDays(7L));

        Mockito.when(collaboratorService.save(ArgumentMatchers.any(Collaborator.class))).thenReturn(collaborator);
        Mockito.when(collaboratorService.getCollaboratorById(collaborator.getId())).thenReturn(collaborator);
        Mockito.when(bookService.getBookById(book.getId())).thenReturn(book);

        Collaborator result = collaboratorService.banCollaborator(collaborator.getId());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBannedAt());
        Assertions.assertNotNull(result.getUnbannedAt());
    }


}
