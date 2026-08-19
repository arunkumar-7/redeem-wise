package com.redeemwise.card;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Card Service application entry point.
 * Manages credit card CRUD operations and card-related business logic.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardServiceApplication.class, args);
    }
}
