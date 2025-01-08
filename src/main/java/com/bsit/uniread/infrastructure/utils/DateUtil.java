package com.bsit.uniread.infrastructure.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Utility class for handling date and time operations.
 * This method ensures that the current date and time are always in UTC.
 */
public class DateUtil {

    public static final Long JSON_WEB_TOKEN_EXPIRATION = 1_209_600L;

    /**
     * Returns the current LocalDateTime in UTC.
     *
     * @return LocalDateTime in UTC
     */
    public static LocalDateTime now() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("UTC");
        return LocalDateTime.ofInstant(instant, zoneId);
    }
}
