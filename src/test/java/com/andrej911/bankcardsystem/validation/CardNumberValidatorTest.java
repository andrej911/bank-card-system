package com.andrej911.bankcardsystem.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CardNumberValidatorTest {

    private final CardNumberValidator validator = new CardNumberValidator();

    @Test
    void acceptsValidNumber() {
        assertThat(validator.isValid("4111111111111111", null)).isTrue();
    }

    @Test
    void acceptsValidNumberWithSpacesAndDashes() {
        assertThat(validator.isValid("4111 1111-1111 1111", null)).isTrue();
    }

    @Test
    void rejectsWrongChecksum() {
        assertThat(validator.isValid("4111111111111112", null)).isFalse();
    }

    @Test
    void rejectsLetters() {
        assertThat(validator.isValid("41111111111111aa", null)).isFalse();
    }

    @Test
    void treatsNullAsValid_becauseRequiredIsCheckedByNotBlank() {
        assertThat(validator.isValid(null, null)).isTrue();
    }
}