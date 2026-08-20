package com.redeemwise.auth.service;

import com.redeemwise.auth.dto.request.LoginRequestDto;
import com.redeemwise.auth.dto.request.RegisterRequestDto;
import com.redeemwise.auth.dto.response.AuthResponseDto;
import com.redeemwise.auth.entity.User;
import com.redeemwise.auth.exception.UserAlreadyExistsException;
import com.redeemwise.auth.repository.UserRepository;
import com.redeemwise.auth.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for handling authentication operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Register a new user.
     *
     * @param request the registration request DTO
     * @return a success message
     * @throws UserAlreadyExistsException if email already exists
     */
    @Transactional
    public String register(RegisterRequestDto request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(
                    "User already exists with email: " + request.getEmail());
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .build();

        userRepository.save(user);

        log.info("User registered successfully with email: {}", request.getEmail());
        return "User registered successfully";
    }

    /**
     * Authenticate a user and return JWT token.
     *
     * @param request the login request DTO
     * @return the authentication response with JWT token
     */
    public AuthResponseDto login(LoginRequestDto request) {
        log.info("Authenticating user with email: {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));

        String token = jwtTokenProvider.generateToken(authentication);

        log.info("User authenticated successfully with email: {}", request.getEmail());
        return AuthResponseDto.builder()
                .token(token)
                .build();
    }
}
