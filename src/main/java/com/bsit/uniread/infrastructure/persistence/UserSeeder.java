package com.bsit.uniread.infrastructure.persistence;

import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.entities.user.UserProfile;
import com.bsit.uniread.infrastructure.repositories.user.UserProfileRepository;
import com.bsit.uniread.infrastructure.repositories.user.UserRepository;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public void run(String... args) throws Exception {

        if(userRepository.count() == 0) {
            Role superAdminRole = Role.SUPER_ADMIN;
            Role userRole = Role.USER;

            User user1 = userRepository.save(User.builder()
                    .emailVerifiedAt(DateUtil.now())
                    .username("mortreddd".toLowerCase(Locale.ROOT))
                    .createdAt(DateUtil.now())
                    .email("emmanmale@gmail.com")
                    .role(superAdminRole)
                    .password(new BCryptPasswordEncoder().encode("12345678"))
                    .build());

            UserProfile userProfile1 = UserProfile.builder()
                    .firstName("Emmanuel")
                    .lastName("Male")
                    .gender(Gender.MALE)
                    .user(user1)
                    .displayName("Emmanuel Male")
                    .bio("")
                    .coverPhoto(null)
                    .avatarPhoto(null)
                    .build();

            User user2 = userRepository.save(User.builder()
                    .username("edlyn".toLowerCase(Locale.ROOT))
                    .createdAt(DateUtil.now())
                    .email("edlynmale@gmail.com")
                    .emailVerifiedAt(DateUtil.now())
                    .role(userRole)
                    .password(new BCryptPasswordEncoder().encode("12345678"))
                    .build());

            UserProfile userProfile2 = UserProfile.builder()
                    .firstName("Edlyn")
                    .lastName("Male")
                    .gender(Gender.FEMALE)
                    .user(user2)
                    .displayName("Edlyn Male")
                    .bio("")
                    .coverPhoto(null)
                    .avatarPhoto(null)
                    .build();

            userProfileRepository.saveAll(List.of(userProfile1, userProfile2));
        }
    }
}

