package com.bsit.uniread.infrastructure.security.validations.constraints;

import com.bsit.uniread.infrastructure.security.validations.constants.ValidationMessagesConstant;
import com.bsit.uniread.infrastructure.security.validations.impl.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {
    String message() default ValidationMessagesConstant.UNIQUE_USERNAME;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
