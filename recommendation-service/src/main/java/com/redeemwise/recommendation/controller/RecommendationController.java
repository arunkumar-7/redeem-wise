package com.redeemwise.recommendation.controller;

import com.redeemwise.recommendation.dto.request.RecommendationRequestDto;
import com.redeemwise.recommendation.dto.response.DashboardResponseDto;
import com.redeemwise.recommendation.dto.response.RecommendationResponseDto;
import com.redeemwise.recommendation.service.RecommendationFacade;
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
 * REST Controller for recommendation and dashboard operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@RestController
@Tag(name = "Recommendation Management", description = "Redemption value calculation and recommendation engine operations")
public class RecommendationController {

    private static final Logger log = LoggerFactory.getLogger(RecommendationController.class);

    private final RecommendationFacade recommendationService;

    public RecommendationController(RecommendationFacade recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Generate redemption recommendations for a given card and points balance.
     *
     * @param request the recommendation request containing cardId and availablePoints
     * @return the recommendation response with ranked options
     */
    @PostMapping("/api/recommendations")
    @Operation(summary = "Get recommendations",
            description = "Generate ranked redemption recommendations based on card and available points")
    public ResponseEntity<Map<String, Object>> getRecommendations(
            @Valid @RequestBody RecommendationRequestDto request) {
        log.info("Received recommendation request for cardId: {}, points: {}",
                request.getCardId(), request.getAvailablePoints());

        RecommendationResponseDto response = recommendationService.getRecommendations(request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Recommendations generated successfully");
        body.put("data", response);

        return ResponseEntity.ok(body);
    }

    /**
     * Get a quick dashboard summary for a card.
     *
     * @param cardId the card ID
     * @param availablePoints the available reward points
     * @return the dashboard response with aggregated summary
     */
    @GetMapping("/api/recommendations/dashboard")
    @Operation(summary = "Get dashboard",
            description = "Get a dashboard summary with redemption potential for a card")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @RequestParam Long cardId,
            @RequestParam Integer availablePoints) {
        log.info("Received dashboard request for cardId: {}, points: {}", cardId, availablePoints);

        DashboardResponseDto response = recommendationService.getDashboard(cardId, availablePoints);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Dashboard retrieved successfully");
        body.put("data", response);

        return ResponseEntity.ok(body);
    }

    /**
     * Health check endpoint for the recommendation service.
     *
     * @return the health status
     */
    @GetMapping("/api/recommendations/health")
    @Operation(summary = "Health check", description = "Check the health of the Recommendation Service")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "UP");
        body.put("service", "recommendation-service");
        body.put("message", "Recommendation Service is running");

        return ResponseEntity.ok(body);
    }
}
