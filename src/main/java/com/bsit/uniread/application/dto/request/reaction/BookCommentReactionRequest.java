package com.bsit.uniread.application.dto.request.reaction;

import com.bsit.uniread.domain.entities.Reaction;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class BookCommentReactionRequest {
    private Reaction reaction;
}
