package com.uniread.user.listeners;

import com.uniread.auth.domain.events.GoogleRegistrationEvent;
import com.uniread.auth.domain.events.UserRegisteredEvent;
import com.uniread.user.domain.entities.Gender;
import com.uniread.auth.domain.entities.User;
import com.uniread.user.domain.entities.UserProfile;
import com.uniread.user.repositories.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NewRegisteredUserListener {

    private final UserProfileRepository profileRepository;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreate(GoogleRegistrationEvent event) {
        var userInfo = event.getResponse();
        var user = event.getUser();

        var displayName = String.format("%s %s", userInfo.getGivenName(), userInfo.getFamilyName());

        var profile = UserProfile.builder()
                .user(user)
                .displayName(displayName)
                .firstName(userInfo.getGivenName())
                .lastName(userInfo.getFamilyName())
                .gender(Gender.OTHER)
                .build();

        profileRepository.save(profile);

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCreate(UserRegisteredEvent event) {
        var user = User.builder().id(event.getUserId()).build();
        var request = event.getRequest();
        var displayName = String.format("%s %s", request.getFirstName(), request.getLastName());

        var profile = UserProfile.builder()
                .user(user)
                .displayName(displayName)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .build();


        profileRepository.save(profile);
    }
}
