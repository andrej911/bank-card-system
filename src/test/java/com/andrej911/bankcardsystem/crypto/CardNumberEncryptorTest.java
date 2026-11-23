package com.andrej911.bankcardsystem.crypto;

import org.junit.jupiter.api.Test;

import com.andrej911.bankcardsystem.crypto.CardNumberEncryptor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CardNumberEncryptorTest {

    // Тестовые ключи: ровно 32 байта, закодированные в Base64.
    private static final String KEY_A = keyOf("0123456789abcdef0123456789abcdef");
    private static final String KEY_B = keyOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ012345");
    private static final String NUMBER = "4111111111111111";

    private final CardNumberEncryptor encryptor = new CardNumberEncryptor(KEY_A);

    private static String keyOf(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void decrypt_returnsOriginalNumber() {
        String encrypted = encryptor.encrypt(NUMBER);

        assertThat(encryptor.decrypt(encrypted)).isEqualTo(NUMBER);
    }

    @Test
    void encrypt_doesNotLeakPlainText() {
        assertThat(encryptor.encrypt(NUMBER)).doesNotContain(NUMBER);
    }

    @Test
    void encrypt_givesDifferentResultEachTime_becauseOfRandomIv() {
        String first = encryptor.encrypt(NUMBER);
        String second = encryptor.encrypt(NUMBER);

        assertThat(first).isNotEqualTo(second);
      
        assertThat(encryptor.decrypt(first)).isEqualTo(NUMBER);
        assertThat(encryptor.decrypt(second)).isEqualTo(NUMBER);
    }

    @Test
    void decrypt_withWrongKey_fails() {
        String encrypted = encryptor.encrypt(NUMBER);
        CardNumberEncryptor otherKeyEncryptor = new CardNumberEncryptor(KEY_B);

        assertThatThrownBy(() -> otherKeyEncryptor.decrypt(encrypted))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void decrypt_ofTamperedData_fails() {
        byte[] bytes = Base64.getDecoder().decode(encryptor.encrypt(NUMBER));
        bytes[bytes.length - 1] ^= 1; 
        String tampered = Base64.getEncoder().encodeToString(bytes);

        assertThatThrownBy(() -> encryptor.decrypt(tampered))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void decrypt_ofTooShortInput_fails() {
        assertThatThrownBy(() -> encryptor.decrypt("AAAA"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void constructor_rejectsKeyOfWrongLength() {
        String shortKey = Base64.getEncoder().encodeToString(new byte[16]);

        assertThatThrownBy(() -> new CardNumberEncryptor(shortKey))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void constructor_rejectsInvalidBase64() {
        assertThatThrownBy(() -> new CardNumberEncryptor("not base64!!"))
                .isInstanceOf(IllegalStateException.class);
    }
}