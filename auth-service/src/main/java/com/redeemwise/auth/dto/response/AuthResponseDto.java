package com.redeemwise.auth.dto.response;

/**
 * DTO for authentication response.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class AuthResponseDto {

    private String token;

    public AuthResponseDto() {}

    public AuthResponseDto(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String token;

        public Builder token(String token) { this.token = token; return this; }

        public AuthResponseDto build() {
            return new AuthResponseDto(token);
        }
    }
}
