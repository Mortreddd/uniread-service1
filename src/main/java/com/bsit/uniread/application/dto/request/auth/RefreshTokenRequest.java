package com.bsit.uniread.application.dto.request.auth;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class RefreshTokenRequest {
    private String refreshToken;
}
