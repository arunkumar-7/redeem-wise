package com.redeemwise.recommendation.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redeemwise.recommendation.client.CardServiceClient;
import com.redeemwise.recommendation.client.RewardServiceClient;
import com.redeemwise.recommendation.dto.external.CardResponseDto;
import com.redeemwise.recommendation.dto.external.RewardResponseDto;
import com.redeemwise.recommendation.dto.request.RecommendationRequestDto;
import com.redeemwise.recommendation.dto.response.DashboardResponseDto;
import com.redeemwise.recommendation.dto.response.RecommendationResponseDto;
import com.redeemwise.recommendation.engine.RecommendationEngine;
import com.redeemwise.recommendation.exception.CardNotFoundException;
import com.redeemwise.recommendation.exception.RewardNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RecommendationService.
 * Only interfaces (Feign clients) are mocked; real engine and ObjectMapper are used.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private CardServiceClient cardServiceClient;

    @Mock
    private RewardServiceClient rewardServiceClient;

    @InjectMocks
    private RecommendationService recommendationService;

    private CardResponseDto testCard;
    private RewardResponseDto testRewardOption;

    @BeforeEach
    void setUp() {
        // Inject real engine and ObjectMapper via reflection since @InjectMocks
        // only injects @Mock fields. We set them manually.
        RecommendationEngine realEngine = new RecommendationEngine();
        ObjectMapper realObjectMapper = new ObjectMapper();

        try {
            var engineField = RecommendationService.class.getDeclaredField("recommendationEngine");
            engineField.setAccessible(true);
            engineField.set(recommendationService, realEngine);

            var mapperField = RecommendationService.class.getDeclaredField("objectMapper");
            mapperField.setAccessible(true);
            mapperField.set(recommendationService, realObjectMapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject real dependencies", e);
        }

        testCard = CardResponseDto.builder()
                .id(1L)
                .cardName("HDFC Infinia Metal Edition")
                .bankName("HDFC Bank")
                .network("VISA")
                .rewardType("REWARD_POINTS")
                .annualFee(new BigDecimal("12500"))
                .joiningFee(new BigDecimal("12500"))
                .active(true)
                .build();

        testRewardOption = RewardResponseDto.builder()
                .id(1L)
                .cardId(1L)
                .redemptionCategory("FLIGHT")
                .conversionFormula("SmartBuy Flights")
                .valuePerPoint(new BigDecimal("1.0"))
                .minimumRedemption(BigDecimal.ONE)
                .transferPartner("")
                .transferRatio(null)
                .priorityRank(1)
                .recommendedFlag(true)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should generate recommendations successfully")
    void getRecommendations_WithValidRequest_ReturnsRecommendations() {
        // Arrange
        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .cardId(1L)
                .availablePoints(5000)
                .build();

        Map<String, Object> cardResponse = new HashMap<>();
        cardResponse.put("data", testCard);

        Map<String, Object> rewardResponse = new HashMap<>();
        rewardResponse.put("data", Collections.singletonList(testRewardOption));

        when(cardServiceClient.getCardById(1L)).thenReturn(cardResponse);
        when(rewardServiceClient.getRewardsByCardId(1L)).thenReturn(rewardResponse);

        // Act
        RecommendationResponseDto response = recommendationService.getRecommendations(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getCardId());
        assertEquals("HDFC Infinia Metal Edition", response.getCardName());
        assertEquals("HDFC Bank", response.getBankName());
        assertEquals(5000, response.getAvailablePoints());
        assertNotNull(response.getRecommendations());
        assertFalse(response.getRecommendations().isEmpty());
        assertNotNull(response.getBestRecommendation());
        assertEquals("FLIGHT", response.getBestRecommendation().getCategory());
        assertNotNull(response.getGeneratedAt());

        verify(cardServiceClient).getCardById(1L);
        verify(rewardServiceClient).getRewardsByCardId(1L);
    }

    @Test
    @DisplayName("Should throw CardNotFoundException when card not found")
    void getRecommendations_WhenCardNotFound_ThrowsCardNotFoundException() {
        // Arrange
        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .cardId(999L)
                .availablePoints(5000)
                .build();

        when(cardServiceClient.getCardById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(CardNotFoundException.class,
                () -> recommendationService.getRecommendations(request));
    }

    @Test
    @DisplayName("Should throw RewardNotFoundException when no rewards found")
    void getRecommendations_WhenNoRewards_ThrowsRewardNotFoundException() {
        // Arrange
        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .cardId(1L)
                .availablePoints(5000)
                .build();

        Map<String, Object> cardResponse = new HashMap<>();
        cardResponse.put("data", testCard);

        when(cardServiceClient.getCardById(1L)).thenReturn(cardResponse);
        when(rewardServiceClient.getRewardsByCardId(1L)).thenReturn(null);

        // Act & Assert
        assertThrows(RewardNotFoundException.class,
                () -> recommendationService.getRecommendations(request));
    }

    @Test
    @DisplayName("Should apply category filter correctly")
    void getRecommendations_WithCategoryFilter_FiltersCorrectly() {
        // Arrange
        RewardResponseDto flightOption = RewardResponseDto.builder()
                .id(1L).cardId(1L).redemptionCategory("FLIGHT")
                .conversionFormula("SmartBuy Flights").valuePerPoint(new BigDecimal("1.0"))
                .minimumRedemption(BigDecimal.ONE).priorityRank(1).recommendedFlag(true).active(true)
                .build();

        RewardResponseDto voucherOption = RewardResponseDto.builder()
                .id(2L).cardId(1L).redemptionCategory("VOUCHER")
                .conversionFormula("Brand Vouchers").valuePerPoint(new BigDecimal("0.5"))
                .minimumRedemption(BigDecimal.valueOf(500)).priorityRank(2).recommendedFlag(false).active(true)
                .build();

        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .cardId(1L)
                .availablePoints(5000)
                .categoryFilter("FLIGHT")
                .build();

        Map<String, Object> cardResponse = new HashMap<>();
        cardResponse.put("data", testCard);

        Map<String, Object> rewardResponse = new HashMap<>();
        rewardResponse.put("data", Arrays.asList(flightOption, voucherOption));

        when(cardServiceClient.getCardById(1L)).thenReturn(cardResponse);
        when(rewardServiceClient.getRewardsByCardId(1L)).thenReturn(rewardResponse);

        // Act
        RecommendationResponseDto response = recommendationService.getRecommendations(request);

        // Assert
        assertNotNull(response);
        // Only FLIGHT option should be processed (after filtering)
        assertEquals(1, response.getRecommendations().size());
        assertEquals("FLIGHT", response.getRecommendations().get(0).getCategory());
    }

    @Test
    @DisplayName("Should generate dashboard successfully")
    void getDashboard_WithValidCardId_ReturnsDashboard() {
        // Arrange
        Map<String, Object> cardResponse = new HashMap<>();
        cardResponse.put("data", testCard);

        Map<String, Object> rewardResponse = new HashMap<>();
        rewardResponse.put("data", Collections.singletonList(testRewardOption));

        when(cardServiceClient.getCardById(1L)).thenReturn(cardResponse);
        when(rewardServiceClient.getRewardsByCardId(1L)).thenReturn(rewardResponse);

        // Act
        DashboardResponseDto response = recommendationService.getDashboard(1L, 5000);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getCardId());
        assertEquals("HDFC Infinia Metal Edition", response.getCardName());
        assertEquals(5000, response.getTotalAvailablePoints());
        assertEquals(1, response.getTotalRedemptionOptions());
        assertNotNull(response.getGeneratedAt());
    }

    @Test
    @DisplayName("Should handle multiple reward options correctly")
    void getRecommendations_WithMultipleOptions_RanksCorrectly() {
        // Arrange
        RewardResponseDto highVpp = RewardResponseDto.builder()
                .id(1L).cardId(1L).redemptionCategory("HOTEL_POINTS_TRANSFER")
                .conversionFormula("Accor Transfer").valuePerPoint(new BigDecimal("2.4"))
                .minimumRedemption(BigDecimal.valueOf(500)).priorityRank(1).recommendedFlag(true).active(true)
                .build();

        RewardResponseDto midVpp = RewardResponseDto.builder()
                .id(2L).cardId(1L).redemptionCategory("FLIGHT")
                .conversionFormula("Travel Portal").valuePerPoint(new BigDecimal("1.0"))
                .minimumRedemption(BigDecimal.valueOf(500)).priorityRank(3).recommendedFlag(true).active(true)
                .build();

        RewardResponseDto lowVpp = RewardResponseDto.builder()
                .id(3L).cardId(1L).redemptionCategory("STATEMENT_CREDIT")
                .conversionFormula("Statement Credit").valuePerPoint(new BigDecimal("0.2"))
                .minimumRedemption(BigDecimal.valueOf(2500)).priorityRank(5).recommendedFlag(false).active(true)
                .build();

        RecommendationRequestDto request = RecommendationRequestDto.builder()
                .cardId(1L)
                .availablePoints(5000)
                .build();

        Map<String, Object> cardResponse = new HashMap<>();
        cardResponse.put("data", testCard);

        Map<String, Object> rewardResponse = new HashMap<>();
        rewardResponse.put("data", Arrays.asList(lowVpp, highVpp, midVpp));

        when(cardServiceClient.getCardById(1L)).thenReturn(cardResponse);
        when(rewardServiceClient.getRewardsByCardId(1L)).thenReturn(rewardResponse);

        // Act
        RecommendationResponseDto response = recommendationService.getRecommendations(request);

        // Assert
        assertNotNull(response);
        // Best should be HOTEL_POINTS_TRANSFER (highest VPP)
        assertEquals("HOTEL_POINTS_TRANSFER", response.getBestRecommendation().getCategory());
        assertEquals(0, new BigDecimal("2.4").compareTo(response.getBestRecommendation().getValuePerPoint()));

        // All options should be in recommendations (all have min 500 or 2500, we have 5000)
        assertEquals(3, response.getRecommendations().size());

        // First should be highest VPP
        assertEquals("HOTEL_POINTS_TRANSFER", response.getRecommendations().get(0).getCategory());
        // Second should be mid VPP
        assertEquals("FLIGHT", response.getRecommendations().get(1).getCategory());
        // Third should be lowest VPP
        assertEquals("STATEMENT_CREDIT", response.getRecommendations().get(2).getCategory());
    }
}
