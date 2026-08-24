package com.redeemwise.recommendation.controller;

import com.redeemwise.recommendation.dto.request.RecommendationRequestDto;
import com.redeemwise.recommendation.dto.response.DashboardResponseDto;
import com.redeemwise.recommendation.dto.response.RecommendationResponseDto;
import com.redeemwise.recommendation.dto.response.RedemptionOptionDto;
import com.redeemwise.recommendation.exception.CardNotFoundException;
import com.redeemwise.recommendation.service.RecommendationFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RecommendationController.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@ExtendWith(MockitoExtension.class)
class RecommendationControllerTest {

    @Mock
    private RecommendationFacade recommendationService;

    @InjectMocks
    private RecommendationController controller;

    private RecommendationResponseDto testRecommendationResponse;
    private DashboardResponseDto testDashboardResponse;

    @BeforeEach
    void setUp() {
        testRecommendationResponse = RecommendationResponseDto.builder()
                .cardId(1L)
                .cardName("HDFC Infinia Metal Edition")
                .bankName("HDFC Bank")
                .network("VISA")
                .rewardType("REWARD_POINTS")
                .availablePoints(5000)
                .totalEstimatedValue(new BigDecimal("5000.00"))
                .bestRecommendation(RedemptionOptionDto.builder()
                        .id(1L).name("SmartBuy Flights").category("FLIGHT")
                        .valuePerPoint(new BigDecimal("1.0")).rank(1).isEligible(true).build())
                .recommendations(Collections.singletonList(
                        RedemptionOptionDto.builder()
                                .id(1L).name("SmartBuy Flights").category("FLIGHT")
                                .valuePerPoint(new BigDecimal("1.0")).rank(1).isEligible(true).build()))
                .ineligibleOptions(Collections.emptyList())
                .generatedAt(LocalDateTime.now())
                .build();

        testDashboardResponse = DashboardResponseDto.builder()
                .cardId(1L)
                .cardName("HDFC Infinia Metal Edition")
                .bankName("HDFC Bank")
                .network("VISA")
                .rewardType("REWARD_POINTS")
                .totalAvailablePoints(5000)
                .estimatedMaxValue(new BigDecimal("5000.00"))
                .averageValuePerPoint(new BigDecimal("0.7500"))
                .totalRedemptionOptions(4)
                .eligibleOptions(3)
                .generatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should return recommendations successfully")
    @SuppressWarnings("unchecked")
    void getRecommendations_WithValidRequest_ReturnsOk() {
        // Arrange
        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .cardId(1L)
                .availablePoints(5000)
                .build();

        when(recommendationService.getRecommendations(any(RecommendationRequestDto.class)))
                .thenReturn(testRecommendationResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.getRecommendations(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Recommendations generated successfully", response.getBody().get("message"));
        assertNotNull(response.getBody().get("data"));

        verify(recommendationService).getRecommendations(any(RecommendationRequestDto.class));
    }

    @Test
    @DisplayName("Should return dashboard successfully")
    @SuppressWarnings("unchecked")
    void getDashboard_WithValidParams_ReturnsOk() {
        // Arrange
        when(recommendationService.getDashboard(1L, 5000))
                .thenReturn(testDashboardResponse);

        // Act
        ResponseEntity<Map<String, Object>> response = controller.getDashboard(1L, 5000);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Dashboard retrieved successfully", response.getBody().get("message"));

        verify(recommendationService).getDashboard(1L, 5000);
    }

    @Test
    @DisplayName("Should return health check successfully")
    @SuppressWarnings("unchecked")
    void healthCheck_ReturnsOk() {
        // Act
        ResponseEntity<Map<String, Object>> response = controller.healthCheck();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UP", response.getBody().get("status"));
        assertEquals("recommendation-service", response.getBody().get("service"));
    }

    @Test
    @DisplayName("Should propagate CardNotFoundException from service")
    @SuppressWarnings("unchecked")
    void getRecommendations_WhenCardNotFound_ThrowsException() {
        // Arrange
        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .cardId(999L)
                .availablePoints(5000)
                .build();

        when(recommendationService.getRecommendations(any(RecommendationRequestDto.class)))
                .thenThrow(new CardNotFoundException("Card not found with id: 999"));

        // Act & Assert
        assertThrows(CardNotFoundException.class,
                () -> controller.getRecommendations(request));
    }
}
