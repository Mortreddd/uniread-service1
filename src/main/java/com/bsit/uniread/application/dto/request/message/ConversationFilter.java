package com.bsit.uniread.application.dto.request.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConversationFilter {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private Boolean isArchived = false;
}
