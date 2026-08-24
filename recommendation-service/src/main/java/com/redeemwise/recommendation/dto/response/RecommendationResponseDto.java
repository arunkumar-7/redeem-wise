package com.redeemwise.recommendation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for recommendation response.
 * Contains card info, ranked redemption options, and the best recommendation.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class RecommendationResponseDto {

    private Long cardId;
    private String cardName;
    private String bankName;
    private String network;
    private String rewardType;
    private Integer availablePoints;
    private BigDecimal totalEstimatedValue;
    private RedemptionOptionDto bestRecommendation;
    private List<RedemptionOptionDto> recommendations;
    private List<RedemptionOptionDto> ineligibleOptions;
    private LocalDateTime generatedAt;

    public RecommendationResponseDto() {}

    public RecommendationResponseDto(Long cardId, String cardName, String bankName, String network,
                                      String rewardType, Integer availablePoints,
                                      BigDecimal totalEstimatedValue,
                                      RedemptionOptionDto bestRecommendation,
                                      List<RedemptionOptionDto> recommendations,
                                      List<RedemptionOptionDto> ineligibleOptions,
                                      LocalDateTime generatedAt) {
        this.cardId = cardId;
        this.cardName = cardName;
        this.bankName = bankName;
        this.network = network;
        this.rewardType = rewardType;
        this.availablePoints = availablePoints;
        this.totalEstimatedValue = totalEstimatedValue;
        this.bestRecommendation = bestRecommendation;
        this.recommendations = recommendations;
        this.ineligibleOptions = ineligibleOptions;
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

    public Integer getAvailablePoints() { return availablePoints; }
    public void setAvailablePoints(Integer availablePoints) { this.availablePoints = availablePoints; }

    public BigDecimal getTotalEstimatedValue() { return totalEstimatedValue; }
    public void setTotalEstimatedValue(BigDecimal totalEstimatedValue) { this.totalEstimatedValue = totalEstimatedValue; }

    public RedemptionOptionDto getBestRecommendation() { return bestRecommendation; }
    public void setBestRecommendation(RedemptionOptionDto bestRecommendation) { this.bestRecommendation = bestRecommendation; }

    public List<RedemptionOptionDto> getRecommendations() { return recommendations; }
    public void setRecommendations(List<RedemptionOptionDto> recommendations) { this.recommendations = recommendations; }

    public List<RedemptionOptionDto> getIneligibleOptions() { return ineligibleOptions; }
    public void setIneligibleOptions(List<RedemptionOptionDto> ineligibleOptions) { this.ineligibleOptions = ineligibleOptions; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long cardId;
        private String cardName;
        private String bankName;
        private String network;
        private String rewardType;
        private Integer availablePoints;
        private BigDecimal totalEstimatedValue;
        private RedemptionOptionDto bestRecommendation;
        private List<RedemptionOptionDto> recommendations;
        private List<RedemptionOptionDto> ineligibleOptions;
        private LocalDateTime generatedAt;

        public Builder cardId(Long cardId) { this.cardId = cardId; return this; }
        public Builder cardName(String cardName) { this.cardName = cardName; return this; }
        public Builder bankName(String bankName) { this.bankName = bankName; return this; }
        public Builder network(String network) { this.network = network; return this; }
        public Builder rewardType(String rewardType) { this.rewardType = rewardType; return this; }
        public Builder availablePoints(Integer availablePoints) { this.availablePoints = availablePoints; return this; }
        public Builder totalEstimatedValue(BigDecimal totalEstimatedValue) { this.totalEstimatedValue = totalEstimatedValue; return this; }
        public Builder bestRecommendation(RedemptionOptionDto bestRecommendation) { this.bestRecommendation = bestRecommendation; return this; }
        public Builder recommendations(List<RedemptionOptionDto> recommendations) { this.recommendations = recommendations; return this; }
        public Builder ineligibleOptions(List<RedemptionOptionDto> ineligibleOptions) { this.ineligibleOptions = ineligibleOptions; return this; }
        public Builder generatedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; return this; }

        public RecommendationResponseDto build() {
            return new RecommendationResponseDto(cardId, cardName, bankName, network, rewardType,
                    availablePoints, totalEstimatedValue, bestRecommendation, recommendations,
                    ineligibleOptions, generatedAt);
        }
    }
}
