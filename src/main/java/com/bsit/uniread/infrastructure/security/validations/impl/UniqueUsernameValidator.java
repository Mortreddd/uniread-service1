package com.bsit.uniread.infrastructure.security.validations.impl;

import com.bsit.uniread.infrastructure.security.validations.constraints.UniqueUsername;
import com.bsit.uniread.application.services.user.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    @Autowired
    private UserService userService;

    /**
     * Verify the submitted username is unique
     * @param username
     * @param constraintValidatorContext
     * @return Boolean
     */
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !userService.usernameExists(username);
    }

}
