package com.bsit.uniread.infrastructure.security.validations.impl;

import com.bsit.uniread.infrastructure.security.validations.constraints.UniqueUsername;
import com.bsit.uniread.application.services.user.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    @Autowired
    public UniqueUsernameValidator(UserService userService) {
        this.userService = userService;
    }
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
