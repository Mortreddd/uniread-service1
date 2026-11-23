package com.uniread.common.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class PasswordUtil {

    private static final String UPPERCASE = "ABCDEFGHJKLMNPQRSTUVWXYZ"; // no I, O
    private static final String LOWERCASE = "abcdefghijkmnopqrstuvwxyz"; // no l
    private static final String DIGITS = "23456789";                     // no 0, 1
    private static final String SPECIAL = "!@#$%^&*";

    private static final String ALL_CHARS = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;

    private static final int LENGTH = 12;

    private final SecureRandom random = new SecureRandom();

    public String generateRandomPassword() {
        List<Character> password = new ArrayList<>(LENGTH);

        // Guarantee at least one of each category
        password.add(randomChar(UPPERCASE));
        password.add(randomChar(LOWERCASE));
        password.add(randomChar(DIGITS));
        password.add(randomChar(SPECIAL));

        // Fill the rest from all characters
        for (int i = password.size(); i < LENGTH; i++) {
            password.add(randomChar(ALL_CHARS));
        }

        // Shuffle so the guaranteed chars aren't always in the same positions
        Collections.shuffle(password, random);

        StringBuilder sb = new StringBuilder(LENGTH);
        for (char c : password) {
            sb.append(c);
        }
        return sb.toString();
    }

    private char randomChar(String pool) {
        return pool.charAt(random.nextInt(pool.length()));
    }
}