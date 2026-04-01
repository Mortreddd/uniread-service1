package com.bsit.uniread.user;

import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.mappers.user.UserMapper;
import com.bsit.uniread.infrastructure.handler.exceptions.DuplicateResourceException;
import com.bsit.uniread.infrastructure.handler.exceptions.ResourceNotFoundException;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserMapper mapper;
    @Mock
    private UserRepository repository;
    @Mock
    private BCryptPasswordEncoder encoder;
    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUser() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .email("emmanmale@gmail.com")
                .username("emmanmale")
                .role(Role.USER)
                .build();

        UserDto dto = UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        Mockito.when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        Mockito.when(mapper.toDto(user))
                .thenReturn(dto);

        UserDto result = userService.getUserById(userId);

        Mockito.verify(repository).findById(userId);
        Mockito.verify(mapper).toDto(user);

        assertEquals("emmanmale", result.getUsername(), "Username should matched the username of dto");
        assertEquals("emmanmale@gmail.com", result.getEmail(), "Email should matched the email of dto");
    }

    @Test
    void shouldThrowResourceNotFound() {

        UUID randomUserId = UUID.randomUUID();

        Mockito.when(repository.findById(randomUserId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(randomUserId);
        });

        Mockito.verify(repository).findById(randomUserId);

    }

    @Test
    void shouldCreateFromGoogleOAuthResponse() {
        String googleUUID = UUID.randomUUID().toString();
        GoogleUserInfoResponse info = GoogleUserInfoResponse.builder()
                .sub(googleUUID)
                .emailVerified(true)
                .familyName("Male")
                .name("Emmanuel")
                .email("emmanmale@gmail.com")
                .build();

        UUID id = UUID.randomUUID();

        User user = User.builder()
                .id(id)
                .email("emmanmale@gmail.com")
                .username("emmanmale")
                .build();


        Mockito.when(repository.findByEmail(info.getEmail())).thenReturn(Optional.empty());
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);

        User saved = userService.createGoogleUser(info);

        assertNotNull(saved, "Should not be null for saving from google info response");
        assertEquals("emmanmale@gmail.com", saved.getEmail(), "Should be equals to the email from response of google");

        Mockito.verify(repository).findByEmail(info.getEmail());
        Mockito.verify(repository).save(Mockito.any());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionFromGoogle() {
        GoogleUserInfoResponse info = null;

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createGoogleUser(info);
        });
    }

    @Test
    void shouldInvalidateUserCreationForExistingEmail() {
        GoogleUserInfoResponse info = GoogleUserInfoResponse.builder()
                .email(null)
                .sub(UUID.randomUUID().toString())
                .build();


        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .email("emmanmale@gmail.com")
                .username("emmanmale")
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createGoogleUser(info);
        });


    }

    @Test
    void shouldThrowDuplicateResourceException() {
        GoogleUserInfoResponse info = GoogleUserInfoResponse.builder()
                .email("emmanmale@gmail.com")
                .sub(UUID.randomUUID().toString())
                .build();

        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .email("emmanmale@gmail.com")
                .username("emmanmale")
                .build();

        Mockito.when(repository.findByEmail(info.getEmail())).thenReturn(Optional.of(user));

        assertThrows(DuplicateResourceException.class, () -> {
            userService.createGoogleUser(info);
        });

        Mockito.verify(repository).findByEmail(info.getEmail());

    }

    @Test
    void shouldCreateFromGoogleUserResponse() {
        GoogleUserInfoResponse info = GoogleUserInfoResponse.builder()
                .email("emmanmale@gmail.com")
                .sub(UUID.randomUUID().toString())
                .build();

        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .email("emmanmale@gmail.com")
                .username("emmanmale")
                .build();


        Mockito.when(repository.findByEmail(info.getEmail())).thenReturn(Optional.empty());
        Mockito.when(repository.save(Mockito.any())).thenReturn(user);

        User saved = userService.createGoogleUser(info);

        assertEquals("emmanmale@gmail.com", saved.getEmail());
        assertNotNull(saved.getId());

        Mockito.verify(repository).findByEmail(info.getEmail());
        Mockito.verify(repository).save(Mockito.argThat(savedUser ->
                savedUser.getEmail().equals(info.getEmail())
        ));
    }

}
