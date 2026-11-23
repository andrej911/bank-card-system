package com.andrej911.bankcardsystem.crypto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.andrej911.bankcardsystem.crypto.CardNumberUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardNumberUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"4111111111111111", "5555555555554444", "4242424242424242"})
    void isValid_acceptsNumbersWithCorrectLuhn(String number) {
        assertThat(CardNumberUtils.isValid(number)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "4111111111111112",       // неверная контрольная цифра
            "1234",                   // слишком короткий
            "41111111111111111111",   // 20 цифр: слишком длинный
            "41111111111111aa",       // не только цифры
            "4111 1111 1111 1111",    // не нормализован (пробелы)
            ""                        // пустая строка
    })
    void isValid_rejectsInvalidNumbers(String number) {
        assertThat(CardNumberUtils.isValid(number)).isFalse();
    }

    @Test
    void isValid_rejectsNull() {
        assertThat(CardNumberUtils.isValid(null)).isFalse();
    }

    @Test
    void normalize_removesSpacesAndDashes() {
        assertThat(CardNumberUtils.normalize("4111 1111-1111 1111")).isEqualTo("4111111111111111");
    }

    @Test
    void normalize_keepsNull() {
        assertThat(CardNumberUtils.normalize(null)).isNull();
    }

    @Test
    void last4_returnsLastFourDigits() {
        assertThat(CardNumberUtils.last4("4111111111111234")).isEqualTo("1234");
    }

    @Test
    void last4_rejectsTooShortInput() {
        assertThatThrownBy(() -> CardNumberUtils.last4("12"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void masked_hidesEverythingExceptLast4() {
        assertThat(CardNumberUtils.masked("1234")).isEqualTo("**** **** **** 1234");
    }
}
