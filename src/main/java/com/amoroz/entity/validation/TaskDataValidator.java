package com.amoroz.entity.validation;

import com.amoroz.entity.Task;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaskDataValidator implements ConstraintValidator<ValidateTaskData, Task> {
    private static final int MAX_LOCAL_USER_NAME_LENGTH = 40;
    private static final int MAX_FOREIGNER_USER_NAME_LENGTH = 30;

    @Override
    public void initialize(ValidateTaskData constraintAnnotation) {
    }

    @Override
    public boolean isValid(Task task, ConstraintValidatorContext constraintValidatorContext) {
        if (!(task instanceof Task)) {
            throw new IllegalArgumentException("@ValidateTaskData applies only to instance of Task");
        }
        if (task.getNationality().equals("RU")) {
            return isValidUserNameLength(task, MAX_LOCAL_USER_NAME_LENGTH);
        }
        return isValidUserNameLength(task, MAX_FOREIGNER_USER_NAME_LENGTH);
    }

    private boolean isValidUserNameLength(Task task, int maxLength) {
        return new StringBuilder(task.getFirstName()).append(task.getName()).append(task.getMiddleName()).length() <= maxLength;
    }
}