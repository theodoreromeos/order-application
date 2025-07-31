package com.theodore.order.management.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NoDuplicateProductValidator.class)
@Documented
public @interface NoDuplicateProduct {
    String message() default "Duplicate products not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
