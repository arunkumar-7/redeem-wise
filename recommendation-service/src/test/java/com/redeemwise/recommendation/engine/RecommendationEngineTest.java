package com.redeemwise.recommendation.engine;

import com.redeemwise.recommendation.dto.external.RewardResponseDto;
import com.redeemwise.recommendation.dto.response.RedemptionOptionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RecommendationEngine.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
class RecommendationEngineTest {

    private RecommendationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new RecommendationEngine();
    }

    @Test
    @DisplayName("Should process options and return sorted list with eligibility")
    void processOptions_WithMixedOptions_ReturnsSortedList() {
        // Arrange
        RewardResponseDto highValue = createRewardOption(1L, "FLIGHT", "SmartBuy Flights",
                new BigDecimal("1.0"), 1, 1);
        RewardResponseDto lowValue = createRewardOption(2L, "STATEMENT_CREDIT", "Statement Credit",
                new BigDecimal("0.25"), 2000, 2);
        RewardResponseDto midValue = createRewardOption(3L, "VOUCHER", "Brand Vouchers",
                new BigDecimal("0.5"), 500, 1);

        List<RewardResponseDto> options = Arrays.asList(lowValue, highValue, midValue);
        Integer availablePoints = 1000;

        // Act
        List<RedemptionOptionDto> result = engine.processOptions(options, availablePoints);

        // Assert
        assertEquals(3, result.size());

        // High value option should be first (eligible, highest VPP)
        assertEquals("FLIGHT", result.get(0).getCategory());
        assertTrue(result.get(0).getIsEligible());

        // Mid value option should be second
        assertEquals("VOUCHER", result.get(1).getCategory());
        assertTrue(result.get(1).getIsEligible());

        // Low value option should be last (ineligible - needs 2000 points)
        assertEquals("STATEMENT_CREDIT", result.get(2).getCategory());
        assertFalse(result.get(2).getIsEligible());
        assertNotNull(result.get(2).getIneligibilityReason());
    }

    @Test
    @DisplayName("Should find best recommendation from eligible options")
    void findBestRecommendation_WithEligibleOptions_ReturnsBestOption() {
        // Arrange
        RedemptionOptionDto option1 = createProcessedOption(1L, "FLIGHT", "1.0", true);
        RedemptionOptionDto option2 = createProcessedOption(2L, "VOUCHER", "0.5", true);
        RedemptionOptionDto option3 = createProcessedOption(3L, "STATEMENT_CREDIT", "0.25", true);

        List<RedemptionOptionDto> options = Arrays.asList(option1, option2, option3);

        // Act
        RedemptionOptionDto best = engine.findBestRecommendation(options);

        // Assert
        assertNotNull(best);
        assertEquals("FLIGHT", best.getCategory());
    }

    @Test
    @DisplayName("Should return null when no eligible options exist")
    void findBestRecommendation_NoEligibleOptions_ReturnsNull() {
        // Arrange
        RedemptionOptionDto option1 = createProcessedOption(1L, "FLIGHT", "1.0", false);

        List<RedemptionOptionDto> options = Collections.singletonList(option1);

        // Act
        RedemptionOptionDto best = engine.findBestRecommendation(options);

        // Assert
        assertNull(best);
    }

    @Test
    @DisplayName("Should calculate average VPP correctly")
    void calculateAverageVpp_WithEligibleOptions_ReturnsCorrectAverage() {
        // Arrange
        RedemptionOptionDto option1 = createProcessedOption(1L, "FLIGHT", "1.0", true);
        RedemptionOptionDto option2 = createProcessedOption(2L, "VOUCHER", "0.5", true);

        List<RedemptionOptionDto> options = Arrays.asList(option1, option2);

        // Act
        BigDecimal avgVpp = engine.calculateAverageVpp(options);

        // Assert
        assertEquals(new BigDecimal("0.7500"), avgVpp);
    }

    @Test
    @DisplayName("Should return zero for empty options list")
    void calculateAverageVpp_EmptyList_ReturnsZero() {
        // Act
        BigDecimal avgVpp = engine.calculateAverageVpp(Collections.emptyList());

        // Assert
        assertEquals(BigDecimal.ZERO, avgVpp);
    }

    @Test
    @DisplayName("Should calculate max value correctly")
    void calculateMaxValue_WithEligibleOptions_ReturnsMaxValue() {
        // Arrange
        RedemptionOptionDto option1 = createProcessedOption(1L, "FLIGHT", "1.0", true);
        option1.setEstimatedValue(new BigDecimal("1000.00"));
        RedemptionOptionDto option2 = createProcessedOption(2L, "VOUCHER", "0.5", true);
        option2.setEstimatedValue(new BigDecimal("500.00"));

        List<RedemptionOptionDto> options = Arrays.asList(option1, option2);

        // Act
        BigDecimal maxValue = engine.calculateMaxValue(options);

        // Assert
        assertEquals(new BigDecimal("1000.00"), maxValue);
    }

    @Test
    @DisplayName("Should return zero max value for empty list")
    void calculateMaxValue_EmptyList_ReturnsZero() {
        // Act
        BigDecimal maxValue = engine.calculateMaxValue(Collections.emptyList());

        // Assert
        assertEquals(BigDecimal.ZERO, maxValue);
    }

    @Test
    @DisplayName("Should calculate estimated value correctly")
    void processOptions_CalculatesEstimatedValueCorrectly() {
        // Arrange
        RewardResponseDto option = createRewardOption(1L, "FLIGHT", "SmartBuy Flights",
                new BigDecimal("0.5"), 500, 1);

        List<RewardResponseDto> options = Collections.singletonList(option);
        Integer availablePoints = 1000;

        // Act
        List<RedemptionOptionDto> result = engine.processOptions(options, availablePoints);

        // Assert
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("500.00"), result.get(0).getEstimatedValue());
    }

    // Helper methods

    private RewardResponseDto createRewardOption(Long id, String category, String formula,
                                                  BigDecimal vpp, int minRedemption, int priorityRank) {
        return RewardResponseDto.builder()
                .id(id)
                .cardId(1L)
                .redemptionCategory(category)
                .conversionFormula(formula)
                .valuePerPoint(vpp)
                .minimumRedemption(BigDecimal.valueOf(minRedemption))
                .transferPartner("")
                .transferRatio(null)
                .priorityRank(priorityRank)
                .recommendedFlag(priorityRank <= 2)
                .active(true)
                .build();
    }

    private RedemptionOptionDto createProcessedOption(Long id, String category, String vpp,
                                                       boolean isEligible) {
        return RedemptionOptionDto.builder()
                .id(id)
                .name("Test Option " + id)
                .category(category)
                .valuePerPoint(new BigDecimal(vpp))
                .pointsRequired(isEligible ? 500 : 2000)
                .estimatedValue(isEligible ? new BigDecimal("500.00") : new BigDecimal("0.00"))
                .minimumRedemption(BigDecimal.valueOf(isEligible ? 500 : 2000))
                .rank(1)
                .isRecommended(true)
                .isEligible(isEligible)
                .build();
    }
}
