package com.bsit.uniread.infrastructure.persistence;

import com.bsit.uniread.application.services.role.RoleService;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.RoleName;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.infrastructure.repositories.user.RoleRepository;
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

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            // Create roles
            List<Role> roles = List.of(
                    Role.builder().name(RoleName.SUPER_ADMIN).build(),
                    Role.builder().name(RoleName.ADMIN).build(),
                    Role.builder().name(RoleName.USER).build()
            );

            roleRepository.saveAll(roles);
        }


        if(userRepository.count() == 0) {
            Role superAdminRole = roleService.getSuperAdminRole();
            Role userRole = roleService.getUserRole();
            List<User> users = List.of(
                    User.builder()
                            .username("mortreddd".toLowerCase(Locale.ROOT))
                            .emailVerifiedAt(DateUtil.now())
                            .firstName("Emmanuel")
                            .lastName("Male")
                            .createdAt(DateUtil.now())
                            .gender(Gender.MALE)
                            .email("emmanmale@gmail.com")
                            .role(superAdminRole) // SUPER_ADMIN
                            .password(new BCryptPasswordEncoder().encode("12345678"))
                            .build(),
                    User.builder()
                            .username("edlyn".toLowerCase(Locale.ROOT))
                            .firstName("Edlyn")
                            .lastName("Male")
                            .createdAt(DateUtil.now())
                            .gender(Gender.FEMALE)
                            .email("edlynmale@gmail.com")
                            .emailVerifiedAt(DateUtil.now())
                            .role(userRole)
                            .password(new BCryptPasswordEncoder().encode("12345678"))
                            .build()
            );

            userRepository.saveAll(users);
        }
    }
}

