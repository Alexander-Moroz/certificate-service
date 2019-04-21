package com.amoroz.entity.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TaskDataValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateTaskData {
    String message() default "{com.amoroz.entity.validation.ValidateTaskData.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}