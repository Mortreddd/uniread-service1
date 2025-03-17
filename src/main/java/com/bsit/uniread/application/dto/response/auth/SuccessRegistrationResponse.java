package com.bsit.uniread.application.dto.response.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessRegistrationResponse {
    private String message;
    private Integer code;
}
