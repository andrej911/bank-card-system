package com.andrej911.bankcardsystem.dto;

import com.andrej911.bankcardsystem.crypto.CardNumberUtils;
import com.andrej911.bankcardsystem.model.Card;
import com.andrej911.bankcardsystem.model.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardResponse(
        Long id,
        String maskedNumber,
        Long ownerId,
        LocalDate expiryDate,
        CardStatus status,
        BigDecimal balance) {

    public static CardResponse from(Card card) {
        return new CardResponse(
                card.getId(),
                CardNumberUtils.masked(card.getLast4()),
                card.getOwner().getId(),
                card.getExpiryDate(),
                card.getStatus(),
                card.getBalance());
    }
}