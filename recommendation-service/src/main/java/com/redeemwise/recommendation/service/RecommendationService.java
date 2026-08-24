package com.redeemwise.recommendation.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redeemwise.recommendation.client.CardServiceClient;
import com.redeemwise.recommendation.client.RewardServiceClient;
import com.redeemwise.recommendation.dto.external.CardResponseDto;
import com.redeemwise.recommendation.dto.external.RewardResponseDto;
import com.redeemwise.recommendation.dto.request.RecommendationRequestDto;
import com.redeemwise.recommendation.dto.response.DashboardResponseDto;
import com.redeemwise.recommendation.dto.response.RecommendationResponseDto;
import com.redeemwise.recommendation.dto.response.RedemptionOptionDto;
import com.redeemwise.recommendation.engine.RecommendationEngine;
import com.redeemwise.recommendation.exception.CardNotFoundException;
import com.redeemwise.recommendation.exception.RewardNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for handling recommendation operations.
 * Orchestrates calls to Card Service and Reward Service via OpenFeign,
 * then uses the RecommendationEngine to calculate and rank options.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Service
public class RecommendationService implements RecommendationFacade {

    private static final Logger log = LoggerFactory.getLogger(RecommendationService.class);

    private final CardServiceClient cardServiceClient;
    private final RewardServiceClient rewardServiceClient;
    private final RecommendationEngine recommendationEngine;
    private final ObjectMapper objectMapper;

    public RecommendationService(CardServiceClient cardServiceClient,
                                  RewardServiceClient rewardServiceClient,
                                  RecommendationEngine recommendationEngine,
                                  ObjectMapper objectMapper) {
        this.cardServiceClient = cardServiceClient;
        this.rewardServiceClient = rewardServiceClient;
        this.recommendationEngine = recommendationEngine;
        this.objectMapper = objectMapper;
    }

    /**
     * Generate recommendations for a given card and available points.
     *
     * Flow:
     * 1. Fetch card details from Card Service
     * 2. Fetch reward options for the card from Reward Service
     * 3. Process options through the RecommendationEngine
     * 4. Build and return the recommendation response
     *
     * @param request the recommendation request containing cardId and availablePoints
     * @return the recommendation response with ranked options
     * @throws CardNotFoundException if the card is not found or inactive
     * @throws RewardNotFoundException if no reward options exist for the card
     */
    public RecommendationResponseDto getRecommendations(RecommendationRequestDto request) {
        log.info("Generating recommendations for cardId: {}, availablePoints: {}",
                request.getCardId(), request.getAvailablePoints());

        // Step 1: Fetch card details from Card Service
        CardResponseDto card = fetchCardDetails(request.getCardId());

        // Step 2: Fetch reward options from Reward Service
        List<RewardResponseDto> rewardOptions = fetchRewardOptions(request.getCardId());

        // Step 3: Apply category filter if provided
        if (request.getCategoryFilter() != null && !request.getCategoryFilter().isEmpty()) {
            rewardOptions = rewardOptions.stream()
                    .filter(r -> request.getCategoryFilter().equalsIgnoreCase(r.getRedemptionCategory()))
                    .collect(Collectors.toList());
        }

        // Step 4: Process options through the engine
        List<RedemptionOptionDto> allOptions = recommendationEngine.processOptions(
                rewardOptions, request.getAvailablePoints());

        // Step 5: Separate eligible and ineligible options
        List<RedemptionOptionDto> eligibleOptions = allOptions.stream()
                .filter(RedemptionOptionDto::getIsEligible)
                .collect(Collectors.toList());

        List<RedemptionOptionDto> ineligibleOptions = allOptions.stream()
                .filter(r -> !r.getIsEligible())
                .collect(Collectors.toList());

        // Step 6: Find the best recommendation
        RedemptionOptionDto bestRecommendation = recommendationEngine.findBestRecommendation(eligibleOptions);

        // Step 7: Calculate maximum redeemable value
        BigDecimal totalEstimatedValue = bestRecommendation != null
                ? bestRecommendation.getEstimatedValue()
                : BigDecimal.ZERO;

        log.info("Generated {} eligible recommendations for cardId: {}",
                eligibleOptions.size(), request.getCardId());

        return RecommendationResponseDto.builder()
                .cardId(card.getId())
                .cardName(card.getCardName())
                .bankName(card.getBankName())
                .network(card.getNetwork())
                .rewardType(card.getRewardType())
                .availablePoints(request.getAvailablePoints())
                .totalEstimatedValue(totalEstimatedValue)
                .bestRecommendation(bestRecommendation)
                .recommendations(eligibleOptions)
                .ineligibleOptions(ineligibleOptions)
                .generatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Generate dashboard summary for a card.
     *
     * @param cardId the card ID
     * @param availablePoints the user's available points
     * @return the dashboard response with aggregated summary
     */
    public DashboardResponseDto getDashboard(Long cardId, Integer availablePoints) {
        log.info("Generating dashboard for cardId: {}, availablePoints: {}", cardId, availablePoints);

        // Fetch card details
        CardResponseDto card = fetchCardDetails(cardId);

        // Fetch reward options
        List<RewardResponseDto> rewardOptions = fetchRewardOptions(cardId);

        // Process options
        List<RedemptionOptionDto> allOptions = recommendationEngine.processOptions(
                rewardOptions, availablePoints);

        List<RedemptionOptionDto> eligibleOptions = allOptions.stream()
                .filter(RedemptionOptionDto::getIsEligible)
                .collect(Collectors.toList());

        // Calculate dashboard metrics
        RedemptionOptionDto bestOption = recommendationEngine.findBestRecommendation(eligibleOptions);
        BigDecimal maxValue = recommendationEngine.calculateMaxValue(eligibleOptions);
        BigDecimal avgVpp = recommendationEngine.calculateAverageVpp(eligibleOptions);

        return DashboardResponseDto.builder()
                .cardId(card.getId())
                .cardName(card.getCardName())
                .bankName(card.getBankName())
                .network(card.getNetwork())
                .rewardType(card.getRewardType())
                .totalAvailablePoints(availablePoints)
                .estimatedMaxValue(maxValue)
                .averageValuePerPoint(avgVpp)
                .totalRedemptionOptions(allOptions.size())
                .eligibleOptions(eligibleOptions.size())
                .bestOption(bestOption)
                .generatedAt(LocalDateTime.now())
                .build();
    }

    /**
     * Fetch card details from Card Service via OpenFeign.
     *
     * @param cardId the card ID to fetch
     * @return the card response DTO
     * @throws CardNotFoundException if the card is not found
     */
    @SuppressWarnings("unchecked")
    private CardResponseDto fetchCardDetails(Long cardId) {
        log.info("Fetching card details for cardId: {} from Card Service", cardId);

        try {
            Map<String, Object> response = cardServiceClient.getCardById(cardId);

            if (response == null || !response.containsKey("data")) {
                throw new CardNotFoundException("Card not found with id: " + cardId);
            }

            Object data = response.get("data");
            return objectMapper.convertValue(data, CardResponseDto.class);

        } catch (CardNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching card details for cardId: {}: {}", cardId, e.getMessage());
            throw new CardNotFoundException("Unable to retrieve card with id: " + cardId
                    + ". Card Service may be unavailable.");
        }
    }

    /**
     * Fetch reward options for a card from Reward Service via OpenFeign.
     *
     * @param cardId the card ID to fetch rewards for
     * @return the list of reward option DTOs
     * @throws RewardNotFoundException if no reward options are found
     */
    @SuppressWarnings("unchecked")
    private List<RewardResponseDto> fetchRewardOptions(Long cardId) {
        log.info("Fetching reward options for cardId: {} from Reward Service", cardId);

        try {
            Map<String, Object> response = rewardServiceClient.getRewardsByCardId(cardId);

            if (response == null || !response.containsKey("data")) {
                throw new RewardNotFoundException(
                        "No reward options found for cardId: " + cardId);
            }

            Object data = response.get("data");
            return objectMapper.convertValue(data, new TypeReference<List<RewardResponseDto>>() {});

        } catch (RewardNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error fetching reward options for cardId: {}: {}", cardId, e.getMessage());
            throw new RewardNotFoundException(
                    "Unable to retrieve reward options for cardId: " + cardId
                    + ". Reward Service may be unavailable.");
        }
    }
}
