package com.bsit.uniread.infrastructure.security.validations.impl;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.security.validations.constraints.EmailExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailExistsValidator implements ConstraintValidator<EmailExists, String> {

    @Autowired
    private UserService userService;

    /**
     * Check if the email exist in credentials or records
     * @param email
     * @param constraintValidatorContext
     * @return boolean
     */

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userService.emailExists(email);
    }
}
