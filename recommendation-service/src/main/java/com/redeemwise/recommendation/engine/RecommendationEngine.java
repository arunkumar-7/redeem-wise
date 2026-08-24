package com.redeemwise.recommendation.engine;

import com.redeemwise.recommendation.dto.external.RewardResponseDto;
import com.redeemwise.recommendation.dto.response.RedemptionOptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Core recommendation engine that calculates redemption values and ranks options.
 *
 * The engine evaluates each reward option based on:
 * 1. Value Per Point (VPP) - primary ranking metric
 * 2. Priority Rank - secondary ranking metric (lower is better)
 * 3. Recommended Flag - tertiary ranking metric (true is better)
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Component
public class RecommendationEngine {

    private static final Logger log = LoggerFactory.getLogger(RecommendationEngine.class);

    /**
     * Process reward options and generate ranked redemption option DTOs.
     *
     * Evaluates eligibility, calculates estimated redemption value, and ranks
     * options from best to worst value.
     *
     * @param rewardOptions the list of reward options from Reward Service
     * @param availablePoints the user's available reward points
     * @return a sorted list of RedemptionOptionDto (eligible first, then by rank)
     */
    public List<RedemptionOptionDto> processOptions(List<RewardResponseDto> rewardOptions,
                                                     Integer availablePoints) {
        log.info("Processing {} reward options with {} available points",
                rewardOptions.size(), availablePoints);

        List<RedemptionOptionDto> processedOptions = new ArrayList<>();

        for (RewardResponseDto option : rewardOptions) {
            RedemptionOptionDto processed = processSingleOption(option, availablePoints);
            processedOptions.add(processed);
        }

        // Sort: eligible options first, then by ranking criteria
        processedOptions.sort((a, b) -> {
            // 1. Eligible options first
            int eligibleCompare = Boolean.compare(b.getIsEligible(), a.getIsEligible());
            if (eligibleCompare != 0) return eligibleCompare;

            // 2. Higher value per point first
            int vppCompare = b.getValuePerPoint().compareTo(a.getValuePerPoint());
            if (vppCompare != 0) return vppCompare;

            // 3. Lower priority rank first
            return Integer.compare(a.getRank(), b.getRank());
        });

        // Assign final rank positions
        for (int i = 0; i < processedOptions.size(); i++) {
            processedOptions.get(i).setRank(i + 1);
        }

        log.info("Processed {} options, {} eligible",
                processedOptions.size(),
                processedOptions.stream().filter(RedemptionOptionDto::getIsEligible).count());

        return processedOptions;
    }

    /**
     * Process a single reward option and determine eligibility.
     *
     * @param option the reward option from Reward Service
     * @param availablePoints the user's available reward points
     * @return the processed RedemptionOptionDto
     */
    private RedemptionOptionDto processSingleOption(RewardResponseDto option,
                                                     Integer availablePoints) {
        BigDecimal valuePerPoint = option.getValuePerPoint();
        BigDecimal minimumRedemption = option.getMinimumRedemption();

        // Calculate estimated redemption value for the available points
        BigDecimal estimatedValue = calculateEstimatedValue(valuePerPoint, availablePoints);

        // Determine eligibility
        boolean isEligible = availablePoints >= minimumRedemption.intValue();
        String ineligibilityReason = null;

        if (!isEligible) {
            int pointsNeeded = minimumRedemption.intValue() - availablePoints;
            ineligibilityReason = String.format(
                    "Need %d more points (minimum redemption: %d points)",
                    pointsNeeded, minimumRedemption.intValue());
        }

        // Use priority rank from the database as initial rank
        Integer rank = option.getPriorityRank() != null ? option.getPriorityRank() : 999;

        return RedemptionOptionDto.builder()
                .id(option.getId())
                .name(option.getConversionFormula())
                .category(option.getRedemptionCategory())
                .valuePerPoint(valuePerPoint)
                .pointsRequired(minimumRedemption.intValue())
                .estimatedValue(estimatedValue)
                .minimumRedemption(minimumRedemption)
                .conversionFormula(option.getConversionFormula())
                .transferPartner(option.getTransferPartner())
                .rank(rank)
                .isRecommended(option.getRecommendedFlag() != null && option.getRecommendedFlag())
                .isEligible(isEligible)
                .ineligibilityReason(ineligibilityReason)
                .build();
    }

    /**
     * Calculate estimated redemption value for available points.
     *
     * Formula: estimatedValue = availablePoints * valuePerPoint
     *
     * @param valuePerPoint the value per point for this option
     * @param availablePoints the number of points available
     * @return the estimated monetary value
     */
    private BigDecimal calculateEstimatedValue(BigDecimal valuePerPoint, Integer availablePoints) {
        return valuePerPoint
                .multiply(BigDecimal.valueOf(availablePoints))
                .setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Find the best recommendation from processed options.
     * Returns the first eligible option with the highest value per point.
     *
     * @param processedOptions the sorted list of processed options
     * @return the best RedemptionOptionDto, or null if no eligible options
     */
    public RedemptionOptionDto findBestRecommendation(List<RedemptionOptionDto> processedOptions) {
        return processedOptions.stream()
                .filter(RedemptionOptionDto::getIsEligible)
                .findFirst()
                .orElse(null);
    }

    /**
     * Calculate average value per point across all eligible options.
     *
     * @param eligibleOptions the list of eligible redemption options
     * @return the average VPP, or BigDecimal.ZERO if no eligible options
     */
    public BigDecimal calculateAverageVpp(List<RedemptionOptionDto> eligibleOptions) {
        if (eligibleOptions.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalVpp = eligibleOptions.stream()
                .map(RedemptionOptionDto::getValuePerPoint)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalVpp.divide(BigDecimal.valueOf(eligibleOptions.size()), 4, RoundingMode.HALF_UP);
    }

    /**
     * Calculate maximum estimated value across all eligible options.
     *
     * @param eligibleOptions the list of eligible redemption options
     * @return the maximum estimated value, or BigDecimal.ZERO if no eligible options
     */
    public BigDecimal calculateMaxValue(List<RedemptionOptionDto> eligibleOptions) {
        return eligibleOptions.stream()
                .map(RedemptionOptionDto::getEstimatedValue)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }
}
