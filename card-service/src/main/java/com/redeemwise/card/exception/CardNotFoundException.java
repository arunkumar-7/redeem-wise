package com.redeemwise.card.exception;

/**
 * Exception thrown when a card is not found with the given identifier.
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
