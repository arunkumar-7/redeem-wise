package com.redeemwise.reward.controller;

import com.redeemwise.reward.dto.request.CreateRewardRequestDto;
import com.redeemwise.reward.dto.request.UpdateRewardRequestDto;
import com.redeemwise.reward.dto.response.RewardResponseDto;
import com.redeemwise.reward.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for reward option management operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/rewards")
@Tag(name = "Reward Management", description = "Reward redemption options catalog management operations")
public class RewardController {

    private static final Logger log = LoggerFactory.getLogger(RewardController.class);

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * Create a new reward option.
     *
     * @param request the reward creation request
     * @return the created reward response
     */
    @PostMapping
    @Operation(summary = "Create a new reward option", description = "Add a new reward redemption option to the catalog")
    public ResponseEntity<Map<String, Object>> createReward(
            @Valid @RequestBody CreateRewardRequestDto request) {
        log.info("Received request to create reward option for cardId: {}, category: {}",
                request.getCardId(), request.getRedemptionCategory());

        RewardResponseDto response = rewardService.createReward(request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Reward option created successfully");
        body.put("data", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /**
     * Get all active reward options.
     *
     * @return list of all active reward options
     */
    @GetMapping
    @Operation(summary = "Get all reward options", description = "Retrieve all active reward redemption options from the catalog")
    public ResponseEntity<Map<String, Object>> getAllRewards() {
        log.info("Received request to get all reward options");

        List<RewardResponseDto> rewards = rewardService.getAllRewards();

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Reward options retrieved successfully");
        body.put("data", rewards);
        body.put("totalElements", rewards.size());

        return ResponseEntity.ok(body);
    }

    /**
     * Get a reward option by ID.
     *
     * @param id the reward option ID
     * @return the reward response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get reward option by ID", description = "Retrieve a reward redemption option by its unique identifier")
    public ResponseEntity<Map<String, Object>> getRewardById(@PathVariable Long id) {
        log.info("Received request to get reward option with id: {}", id);

        RewardResponseDto response = rewardService.getRewardById(id);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Reward option retrieved successfully");
        body.put("data", response);

        return ResponseEntity.ok(body);
    }

    /**
     * Get all active reward options for a specific card.
     *
     * @param cardId the card ID
     * @return list of matching reward options
     */
    @GetMapping("/card/{cardId}")
    @Operation(summary = "Get reward options by card ID", description = "Retrieve all active reward redemption options for a specific credit card")
    public ResponseEntity<Map<String, Object>> getRewardsByCardId(@PathVariable Long cardId) {
        log.info("Received request to get reward options for cardId: {}", cardId);

        List<RewardResponseDto> rewards = rewardService.getRewardsByCardId(cardId);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Reward options retrieved successfully");
        body.put("data", rewards);
        body.put("totalElements", rewards.size());

        return ResponseEntity.ok(body);
    }

    /**
     * Update a reward option by ID.
     *
     * @param id the reward option ID
     * @param request the reward update request
     * @return the updated reward response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a reward option", description = "Update an existing reward redemption option in the catalog")
    public ResponseEntity<Map<String, Object>> updateReward(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRewardRequestDto request) {
        log.info("Received request to update reward option with id: {}", id);

        RewardResponseDto response = rewardService.updateReward(id, request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Reward option updated successfully");
        body.put("data", response);

        return ResponseEntity.ok(body);
    }

    /**
     * Soft delete a reward option by ID.
     *
     * @param id the reward option ID
     * @return the deletion response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reward option", description = "Soft delete a reward redemption option from the catalog")
    public ResponseEntity<Map<String, String>> deleteReward(@PathVariable Long id) {
        log.info("Received request to delete reward option with id: {}", id);

        rewardService.deleteReward(id);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Reward option deleted successfully");

        return ResponseEntity.ok(body);
    }
}
