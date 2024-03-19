package com.lewandowskijan.itrunrecruitmentproject.model.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PeselValidator implements ConstraintValidator<ValidPesel, String> {

    @Override
    public void initialize(ValidPesel constraintAnnotation) {
    }

    @Override
    public boolean isValid(String pesel, ConstraintValidatorContext context) {
        return pesel != null && pesel.matches("\\d{11}");
    }

}
