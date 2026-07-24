package com.uniread.auth.security.validations.impl;

import com.uniread.user.service.UserValidator;
import com.uniread.auth.security.validations.constraints.UniqueUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserValidator userValidator;
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return userValidator.usernameExist(username);
    }

}
