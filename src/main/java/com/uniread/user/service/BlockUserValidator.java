package com.uniread.user.service;

import com.uniread.social.repositories.BlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BlockUserValidator {

    private final BlockRepository blockRepository;

    public Boolean areNotBlocked(UUID user1, UUID user2) {
        return !blockRepository.existsAnyBlocked(user1, user2);
    }

}
