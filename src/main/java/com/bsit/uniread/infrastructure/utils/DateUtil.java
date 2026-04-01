package com.bsit.uniread.infrastructure.utils;

import java.time.Instant;

/**
 * Utility class for handling date and time operations.
 * This method ensures that the current date and time are always in UTC.
 */
public class DateUtil {
    /**
     * Returns the current Instant in UTC.
     *
     * @return Instant in UTC
     */
    public static Instant now() {
        return Instant.now();
    }
}
