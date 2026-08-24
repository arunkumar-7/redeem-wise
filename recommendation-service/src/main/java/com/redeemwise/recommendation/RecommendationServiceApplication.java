package com.redeemwise.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Recommendation Service application entry point.
 * Calculates value per point and generates personalized redemption recommendations.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class RecommendationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationServiceApplication.class, args);
    }
}
