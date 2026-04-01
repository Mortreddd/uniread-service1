package com.bsit.uniread.infrastructure.configurations.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.cookie")
@Data
public class CookieConfigProperties {
    private boolean secure = true;
    private String sameSite = "Strict";
    private int accessTokenMaxAgeMinutes = 15;
    private int refreshTokenMaxAgeDays = 7;
}
