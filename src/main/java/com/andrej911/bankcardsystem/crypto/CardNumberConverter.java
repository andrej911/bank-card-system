package com.andrej911.bankcardsystem.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;

@Converter
@Component
public class CardNumberConverter implements AttributeConverter<String, String> {

    private final CardNumberEncryptor encryptor;

    public CardNumberConverter(CardNumberEncryptor encryptor) {
        this.encryptor = encryptor;
    }

    @Override
    public String convertToDatabaseColumn(String plainNumber) {
        return plainNumber == null ? null : encryptor.encrypt(plainNumber);
    }

    @Override
    public String convertToEntityAttribute(String encryptedNumber) {
        return encryptedNumber == null ? null : encryptor.decrypt(encryptedNumber);
    }
}