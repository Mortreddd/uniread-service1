package com.bsit.uniread.infrastructure.security.validations.impl;

import com.bsit.uniread.application.services.user.UserValidator;
import com.bsit.uniread.infrastructure.security.validations.constraints.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserValidator userValidator;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userValidator.emailExists(email);
    }
}
