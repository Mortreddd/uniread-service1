package com.bsit.uniread.application.services.user;

import com.bsit.uniread.application.dto.response.user.UserDto;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserDto getAuthorByUsername(String name) {
        User user = userService.getUserByUsername(name);
        return new UserDto(user);
    }
}
