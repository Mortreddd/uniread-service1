package com.bsit.uniread.infrastructure.security.validations.impl;

import com.bsit.uniread.application.services.user.UserService;
import com.bsit.uniread.infrastructure.security.validations.constraints.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserService userService;

    /**
     * Validate given email if already exists
     * @param email
     * @param constraintValidatorContext
     * @return boolean
     */
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return userService.emailNotExists(email);
    }
}
