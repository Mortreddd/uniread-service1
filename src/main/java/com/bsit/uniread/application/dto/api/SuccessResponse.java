package com.bsit.uniread.application.dto.api;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SuccessResponse {

    private Integer code;
    private String message;

}
