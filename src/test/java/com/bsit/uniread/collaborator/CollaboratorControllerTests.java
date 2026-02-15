package com.bsit.uniread.collaborator;

import com.bsit.uniread.application.controllers.collaborator.CollaboratorController;
import com.bsit.uniread.application.services.collaborator.CollaboratorService;
import com.bsit.uniread.domain.entities.collaborator.Collaborator;
import com.bsit.uniread.domain.entities.book.Book;
import com.bsit.uniread.domain.entities.book.BookStatus;
import com.bsit.uniread.domain.entities.collaborator.CollaboratorPermission;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.book.BookRepository;
import com.bsit.uniread.infrastructure.repositories.collaborator.CollaboratorRepository;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class CollaboratorControllerTests {

    int defaultPageNo = 0;
    int defaultPageSize = 10;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CollaboratorService collaboratorService;

    @Autowired
    CollaboratorRepository collaboratorRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    private static final UUID COLLABORATOR_1_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID COLLABORATOR_2_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    private static final UUID COLLABORATOR_3_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174002");
    private static final UUID BOOK_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174003");

    User collaborator1 = User.builder()
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
            .title("Harry Potter")
            .description("No description")
            .completed(false)
            .user(collaborator1)
            .matured(false)
            .status(BookStatus.DRAFT)
            .bannedAt(null)
            .publishedAt(null)
            .coverPhoto(null)
            .deletedAt(null)
            .build();

    @BeforeEach
    void setUp() {
        collaboratorRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
        this.collaborator1 = userRepository.save(this.collaborator1);
        this.collaborator2 = userRepository.save(this.collaborator2);
        this.collaborator3 = userRepository.save(this.collaborator3);
        this.book = bookRepository.save(this.book);
    }

    @Test
    void shouldGetCollaborators() throws Exception {
        List<Collaborator> collaborators = List.of(
                Collaborator.builder()
                        .id(UUID.randomUUID())
                        .user(collaborator1)
                        .book(book)
                        .permissions(List.of(CollaboratorPermission.ADMINISTRATOR))
                        .bannedAt(null)
                        .unbannedAt(null)
                        .build(),
                Collaborator.builder()
                        .id(UUID.randomUUID())
                        .user(collaborator2)
                        .book(book)
                        .permissions(List.of())
                        .bannedAt(null)
                        .unbannedAt(null)
                        .build(),
                Collaborator.builder()
                        .id(UUID.randomUUID())
                        .user(collaborator3)
                        .book(book)
                        .permissions(List.of())
                        .bannedAt(null)
                        .unbannedAt(null)
                        .build()

        );


        collaborators = this.collaboratorRepository.saveAll(collaborators);

        this.mockMvc.perform(get("/api/v1/books/" + book.getId() + "/collaborators")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].id").value(collaborators.getFirst().getId()));
    }

    @Test
    void shouldThrowForbiddenStatus() throws Exception {
        List<Collaborator> mock = List.of(
                Collaborator.builder()
                        .id(UUID.randomUUID())
                        .user(collaborator1)
                        .book(book)
                        .permissions(List.of())
                        .bannedAt(null)
                        .unbannedAt(null)
                        .build(),
                Collaborator.builder()
                        .id(UUID.randomUUID())
                        .user(collaborator2)
                        .book(book)
                        .permissions(List.of())
                        .bannedAt(null)
                        .unbannedAt(null)
                        .build(),
                Collaborator.builder()
                        .id(UUID.randomUUID())
                        .user(collaborator3)
                        .book(book)
                        .permissions(List.of())
                        .bannedAt(null)
                        .unbannedAt(null)
                        .build()

        );

        List<Collaborator> collaborators = this.collaboratorRepository.saveAll(mock);

        this.mockMvc.perform(get("/api/v1/books/" + book.getId() + "/collaborators/" + collaborators.getFirst().getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

}
