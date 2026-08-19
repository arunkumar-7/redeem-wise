package com.redeemwise.reward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Reward Service application entry point.
 * Manages reward point balances and the redemption options catalog.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class RewardServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardServiceApplication.class, args);
    }
}
