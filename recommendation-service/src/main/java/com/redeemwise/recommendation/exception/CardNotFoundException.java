package com.redeemwise.recommendation.exception;

/**
 * Exception thrown when a card is not found or not accessible via Card Service.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class CardNotFoundException extends RuntimeException {

    public CardNotFoundException(String message) {
        super(message);
    }
}
