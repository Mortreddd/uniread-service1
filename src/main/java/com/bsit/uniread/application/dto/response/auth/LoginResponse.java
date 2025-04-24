package com.bsit.uniread.application.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String iss;
    private Long iat;
    private String accessToken;
    private String refreshToken;

}
