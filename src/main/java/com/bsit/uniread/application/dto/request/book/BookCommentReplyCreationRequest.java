package com.bsit.uniread.application.dto.request.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookCommentReplyCreationRequest {

    private UUID commenterId;
    private String content;
    private Integer rating;

}
