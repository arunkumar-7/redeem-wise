package com.redeemwise.auth.controller;

import com.redeemwise.auth.dto.request.LoginRequestDto;
import com.redeemwise.auth.dto.request.RegisterRequestDto;
import com.redeemwise.auth.dto.response.AuthResponseDto;
import com.redeemwise.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for authentication operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new user.
     *
     * @param request the registration request
     * @return the registration response
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with email and password")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequestDto request) {
        log.info("Received registration request for email: {}", request.getEmail());

        String message = authService.register(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Authenticate a user and return JWT token.
     *
     * @param request the login request
     * @return the authentication response with JWT token
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    public ResponseEntity<AuthResponseDto> login(
            @Valid @RequestBody LoginRequestDto request) {
        log.info("Received login request for email: {}", request.getEmail());

        AuthResponseDto response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}
