package com.redeemwise.auth.exception;

/**
 * Exception thrown when a user already exists with the given email.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
