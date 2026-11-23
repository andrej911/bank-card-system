package com.andrej911.bankcardsystem.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.HexFormat;

@Component
public class CardNumberHasher {

    private static final String ALGORITHM = "HmacSHA256";
    private static final int MIN_KEY_LENGTH_BYTES = 32;

    private final SecretKeySpec key;

    public CardNumberHasher(@Value("${app.card.hash-key}") String base64Key) {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(base64Key);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("app.card.hash-key is not valid Base64");
        }
        if (keyBytes.length < MIN_KEY_LENGTH_BYTES) {
            throw new IllegalStateException("app.card.hash-key must be at least 32 bytes (Base64)");
        }
        this.key = new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String hash(String normalizedNumber) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);
            byte[] digest = mac.doFinal(normalizedNumber.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Card number hashing failed", e);
        }
    }
}