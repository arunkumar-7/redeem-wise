package com.redeemwise.recommendation.exception;

/**
 * Exception thrown when no reward options are found for a given card.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class RewardNotFoundException extends RuntimeException {

    public RewardNotFoundException(String message) {
        super(message);
    }
}
