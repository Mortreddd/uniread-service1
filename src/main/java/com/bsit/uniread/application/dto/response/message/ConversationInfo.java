package com.bsit.uniread.application.dto.response.message;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ConversationInfo {

    private UUID id;
    private String name;

}
