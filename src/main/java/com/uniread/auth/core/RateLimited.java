package com.uniread.auth.core;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimited {
    int capacity() default 100;
    int refillTokens() default 100;
    int refillDuration() default 1;
    RefillDurationType refillDurationType() default RefillDurationType.MINUTE;
}
