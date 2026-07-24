package com.uniread.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SuccessRegistrationResponse {
    private String message;
    private Integer code;
}
