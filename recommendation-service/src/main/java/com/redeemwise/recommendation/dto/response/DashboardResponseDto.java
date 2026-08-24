package com.redeemwise.recommendation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for dashboard summary response.
 * Provides an aggregated view of a card's reward potential.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class DashboardResponseDto {

    private Long cardId;
    private String cardName;
    private String bankName;
    private String network;
    private String rewardType;
    private Integer totalAvailablePoints;
    private BigDecimal estimatedMaxValue;
    private BigDecimal averageValuePerPoint;
    private Integer totalRedemptionOptions;
    private Integer eligibleOptions;
    private RedemptionOptionDto bestOption;
    private LocalDateTime generatedAt;

    public DashboardResponseDto() {}

    public DashboardResponseDto(Long cardId, String cardName, String bankName, String network,
                                 String rewardType, Integer totalAvailablePoints,
                                 BigDecimal estimatedMaxValue, BigDecimal averageValuePerPoint,
                                 Integer totalRedemptionOptions, Integer eligibleOptions,
                                 RedemptionOptionDto bestOption, LocalDateTime generatedAt) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.bankName = bankName;
        this.network = network;
        this.rewardType = rewardType;
        this.totalAvailablePoints = totalAvailablePoints;
        this.estimatedMaxValue = estimatedMaxValue;
        this.averageValuePerPoint = averageValuePerPoint;
        this.totalRedemptionOptions = totalRedemptionOptions;
        this.eligibleOptions = eligibleOptions;
        this.bestOption = bestOption;
        this.generatedAt = generatedAt;
    }

    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }

    public String getRewardType() { return rewardType; }
    public void setRewardType(String rewardType) { this.rewardType = rewardType; }

    public Integer getTotalAvailablePoints() { return totalAvailablePoints; }
    public void setTotalAvailablePoints(Integer totalAvailablePoints) { this.totalAvailablePoints = totalAvailablePoints; }

    public BigDecimal getEstimatedMaxValue() { return estimatedMaxValue; }
    public void setEstimatedMaxValue(BigDecimal estimatedMaxValue) { this.estimatedMaxValue = estimatedMaxValue; }

    public BigDecimal getAverageValuePerPoint() { return averageValuePerPoint; }
    public void setAverageValuePerPoint(BigDecimal averageValuePerPoint) { this.averageValuePerPoint = averageValuePerPoint; }

    public Integer getTotalRedemptionOptions() { return totalRedemptionOptions; }
    public void setTotalRedemptionOptions(Integer totalRedemptionOptions) { this.totalRedemptionOptions = totalRedemptionOptions; }

    public Integer getEligibleOptions() { return eligibleOptions; }
    public void setEligibleOptions(Integer eligibleOptions) { this.eligibleOptions = eligibleOptions; }

    public RedemptionOptionDto getBestOption() { return bestOption; }
    public void setBestOption(RedemptionOptionDto bestOption) { this.bestOption = bestOption; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long cardId;
        private String cardName;
        private String bankName;
        private String network;
        private String rewardType;
        private Integer totalAvailablePoints;
        private BigDecimal estimatedMaxValue;
        private BigDecimal averageValuePerPoint;
        private Integer totalRedemptionOptions;
        private Integer eligibleOptions;
        private RedemptionOptionDto bestOption;
        private LocalDateTime generatedAt;

        public Builder cardId(Long cardId) { this.cardId = cardId; return this; }
        public Builder cardName(String cardName) { this.cardName = cardName; return this; }
        public Builder bankName(String bankName) { this.bankName = bankName; return this; }
        public Builder network(String network) { this.network = network; return this; }
        public Builder rewardType(String rewardType) { this.rewardType = rewardType; return this; }
        public Builder totalAvailablePoints(Integer totalAvailablePoints) { this.totalAvailablePoints = totalAvailablePoints; return this; }
        public Builder estimatedMaxValue(BigDecimal estimatedMaxValue) { this.estimatedMaxValue = estimatedMaxValue; return this; }
        public Builder averageValuePerPoint(BigDecimal averageValuePerPoint) { this.averageValuePerPoint = averageValuePerPoint; return this; }
        public Builder totalRedemptionOptions(Integer totalRedemptionOptions) { this.totalRedemptionOptions = totalRedemptionOptions; return this; }
        public Builder eligibleOptions(Integer eligibleOptions) { this.eligibleOptions = eligibleOptions; return this; }
        public Builder bestOption(RedemptionOptionDto bestOption) { this.bestOption = bestOption; return this; }
        public Builder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }

        public DashboardResponseDto build() {
            return new DashboardResponseDto(cardId, cardName, bankName, network, rewardType,
                    totalAvailablePoints, estimatedMaxValue, averageValuePerPoint,
                    totalRedemptionOptions, eligibleOptions, bestOption, generatedAt);
        }
    }
}
