package com.bsit.uniread.application.services.follow;

import com.bsit.uniread.infrastructure.repositories.follow.FollowRepository;
import com.bsit.uniread.infrastructure.repositories.user.BlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendService {

    private final BlockRepository blockRepository;
    private final FollowRepository followRepository;


    public Boolean isBlocked(UUID user1, UUID user2) {
        return blockRepository.existsAnyBlocked(user1, user2);
    }

    public Boolean areFriends(UUID user1, UUID user2) {
        return followRepository.existsAsFriends(user1, user2);
    }
}
