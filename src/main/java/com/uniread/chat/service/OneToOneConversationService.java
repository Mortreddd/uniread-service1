package com.uniread.chat.service;

import com.uniread.chat.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OneToOneConversationService {

    private final MessageRepository messageRepository;


}
