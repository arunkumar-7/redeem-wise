package com.redeemwise.auth.exception;

/**
 * Exception thrown when login credentials are invalid.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
