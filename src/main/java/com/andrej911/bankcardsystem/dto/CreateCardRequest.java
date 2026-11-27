package com.andrej911.bankcardsystem.dto;

import com.andrej911.bankcardsystem.validation.ValidCardNumber;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCardRequest(

        @NotBlank(message = "Card number is required")
        @ValidCardNumber
        String cardNumber,

        @NotNull(message = "Owner id is required")
        @Positive(message = "Owner id must be positive")
        Long ownerId,

    
        @NotNull(message = "Expiry date is required")
        @Future(message = "Expiry date must be in the future")
        LocalDate expiryDate,

        @DecimalMin(value = "0.00", message = "Initial balance must not be negative")
        @Digits(integer = 17, fraction = 2, message = "Initial balance must have at most 2 decimal places")
        BigDecimal initialBalance) {

    
    @Override
    public String toString() {
        return "CreateCardRequest[ownerId=" + ownerId + ", cardNumber=***]";
    }
}