package com.bsit.uniread.application.services.message;

import com.bsit.uniread.infrastructure.repositories.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneToOneConversationService {

    private final MessageRepository messageRepository;


}
