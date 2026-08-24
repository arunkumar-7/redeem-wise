package com.redeemwise.recommendation.service;

import com.redeemwise.recommendation.dto.request.RecommendationRequestDto;
import com.redeemwise.recommendation.dto.response.DashboardResponseDto;
import com.redeemwise.recommendation.dto.response.RecommendationResponseDto;

/**
 * Service interface for recommendation operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public interface RecommendationFacade {

    /**
     * Generate redemption recommendations for a given card and points balance.
     *
     * @param request the recommendation request
     * @return the recommendation response with ranked options
     */
    RecommendationResponseDto getRecommendations(RecommendationRequestDto request);

    /**
     * Generate dashboard summary for a card.
     *
     * @param cardId the card ID
     * @param availablePoints the available reward points
     * @return the dashboard response
     */
    DashboardResponseDto getDashboard(Long cardId, Integer availablePoints);
}
