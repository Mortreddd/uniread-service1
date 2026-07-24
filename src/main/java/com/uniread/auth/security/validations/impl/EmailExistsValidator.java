package com.uniread.auth.security.validations.impl;

import com.uniread.user.service.UserValidator;
import com.uniread.auth.security.validations.constraints.EmailExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailExistsValidator implements ConstraintValidator<EmailExists, String> {

    private final UserValidator userValidator;


    /**
     * Check if the email exist in credentials or records
     * @param email
     * @param constraintValidatorContext
     * @return boolean
     */

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userValidator.emailExists(email);
    }
}
