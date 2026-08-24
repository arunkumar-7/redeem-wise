package com.redeemwise.recommendation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * OpenFeign client for communicating with Reward Service.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@FeignClient(name = "reward-service", path = "/api/rewards")
public interface RewardServiceClient {

    /**
     * Retrieve all active reward options for a specific card.
     *
     * @param cardId the card ID
     * @return the reward options list wrapped in a standard API response map
     */
    @GetMapping("/card/{cardId}")
    Map<String, Object> getRewardsByCardId(@PathVariable("cardId") Long cardId);

    /**
     * Retrieve all active reward options.
     *
     * @return all reward options wrapped in a standard API response map
     */
    @GetMapping
    Map<String, Object> getAllRewards();
}
