package com.andrej911.bankcardsystem.crypto;

public final class CardNumberUtils {

    private static final int MIN_LENGTH = 13; 
    private static final int MAX_LENGTH = 19; 
    private static final String MASK_PREFIX = "**** **** **** ";

    private CardNumberUtils() {
    }

    
    public static String normalize(String raw) {
        return raw == null ? null : raw.replaceAll("[\\s-]", "");
    }

    public static boolean isValid(String normalized) {
        if (normalized == null) {
            return false;
        }
        int length = normalized.length();
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char c = normalized.charAt(i);

            if (c < '0' || c > '9') {
                return false;
            }
        }
        return passesLuhn(normalized);
    }

    public static String last4(String normalized) {
        if (normalized == null || normalized.length() < 4) {
            throw new IllegalArgumentException("Card number is too short");
        }
        return normalized.substring(normalized.length() - 4);
    }

    public static String masked(String last4) {
        return MASK_PREFIX + last4;
    }

    private static boolean passesLuhn(String digits) {
        int sum = 0;
        boolean doubleIt = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int digit = digits.charAt(i) - '0';
            if (doubleIt) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleIt = !doubleIt;
        }
        return sum % 10 == 0;
    }
}