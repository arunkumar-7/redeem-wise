package com.redeemwise.reward.exception;

/**
 * Exception thrown when a reward option is not found with the given identifier.
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
