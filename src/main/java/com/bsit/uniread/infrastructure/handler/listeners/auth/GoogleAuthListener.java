package com.bsit.uniread.infrastructure.handler.listeners.auth;


import com.bsit.uniread.application.dto.response.auth.GoogleUserInfoResponse;
import com.bsit.uniread.application.services.role.RoleService;
import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.domain.entities.user.Gender;
import com.bsit.uniread.domain.entities.user.Role;
import com.bsit.uniread.domain.entities.user.User;
import com.bsit.uniread.domain.events.auth.GoogleAuthenticationEvent;
import com.bsit.uniread.infrastructure.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleAuthListener implements ApplicationListener<GoogleAuthenticationEvent> {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final RoleService roleService;
    @Override
    public void onApplicationEvent(GoogleAuthenticationEvent event) {
        GoogleUserInfoResponse userInfo = event.getUserInfo();
        Role userRole = roleService.getUserRole();
        User user = userService.save(User.builder()
                .firstName(userInfo.getGivenName())
                .lastName(userInfo.getFamilyName())
                .email(userInfo.getEmail())
                .username(null)
                .password(encoder.encode(StringUtils.randomAlphanumeric(16)))
                .emailVerifiedAt(DateUtil.now())
                .gender(Gender.OTHER)
                .googleUuid(userInfo.getSub())
                .photoUrl(userInfo.getPicture())
                .role(userRole)
                .build()
        );

        log.info("User email: {}", user.getEmail());

    }
}
