package com.andrej911.bankcardsystem.validation;

import com.andrej911.bankcardsystem.crypto.CardNumberUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CardNumberValidator implements ConstraintValidator<ValidCardNumber, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) {
            return true;
        }

        return CardNumberUtils.isValid(CardNumberUtils.normalize(value));
    }
}
