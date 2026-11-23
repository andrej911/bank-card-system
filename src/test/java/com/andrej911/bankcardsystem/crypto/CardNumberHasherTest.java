package com.andrej911.bankcardsystem.crypto;

import org.junit.jupiter.api.Test;

import com.andrej911.bankcardsystem.crypto.CardNumberHasher;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardNumberHasherTest {

    private static final String KEY_A = keyOf("0123456789abcdef0123456789abcdef");
    private static final String KEY_B = keyOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ012345");

    private final CardNumberHasher hasher = new CardNumberHasher(KEY_A);

    private static String keyOf(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void hash_isDeterministic() {
        assertThat(hasher.hash("4111111111111111")).isEqualTo(hasher.hash("4111111111111111"));
    }

    @Test
    void hash_has64HexCharacters() {
        assertThat(hasher.hash("4111111111111111")).matches("[0-9a-f]{64}");
    }

    @Test
    void hash_doesNotContainTheNumber() {
        assertThat(hasher.hash("4111111111111111")).doesNotContain("4111111111111111");
    }

    @Test
    void hash_differsForDifferentNumbers() {
        assertThat(hasher.hash("4111111111111111")).isNotEqualTo(hasher.hash("5555555555554444"));
    }

    @Test
    void hash_differsForDifferentKeys() {
        CardNumberHasher other = new CardNumberHasher(KEY_B);

        assertThat(hasher.hash("4111111111111111")).isNotEqualTo(other.hash("4111111111111111"));
    }

    @Test
    void constructor_rejectsTooShortKey() {
        String shortKey = Base64.getEncoder().encodeToString(new byte[16]);

        assertThatThrownBy(() -> new CardNumberHasher(shortKey))
                .isInstanceOf(IllegalStateException.class);
    }
}