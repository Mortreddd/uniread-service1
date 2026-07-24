package com.uniread.auth.service;

import com.uniread.auth.dto.response.LoginResponse;

public interface OAuthService {
    LoginResponse handleOAuthLogin(String accessToken);
}
