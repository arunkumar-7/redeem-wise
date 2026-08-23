package com.redeemwise.reward.dto.response;

import com.redeemwise.reward.entity.RedemptionCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for reward option response.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class RewardResponseDto {

    private Long id;
    private Long cardId;
    private RedemptionCategory redemptionCategory;
    private String conversionFormula;
    private BigDecimal valuePerPoint;
    private BigDecimal minimumRedemption;
    private String transferPartner;
    private BigDecimal transferRatio;
    private Integer priorityRank;
    private Boolean recommendedFlag;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RewardResponseDto() {}

    public RewardResponseDto(Long id, Long cardId, RedemptionCategory redemptionCategory,
                              String conversionFormula, BigDecimal valuePerPoint,
                              BigDecimal minimumRedemption, String transferPartner,
                              BigDecimal transferRatio, Integer priorityRank,
                              Boolean recommendedFlag, Boolean active,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.cardId = cardId;
        this.redemptionCategory = redemptionCategory;
        this.conversionFormula = conversionFormula;
        this.valuePerPoint = valuePerPoint;
        this.minimumRedemption = minimumRedemption;
        this.transferPartner = transferPartner;
        this.transferRatio = transferRatio;
        this.priorityRank = priorityRank;
        this.recommendedFlag = recommendedFlag;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }

    public RedemptionCategory getRedemptionCategory() { return redemptionCategory; }
    public void setRedemptionCategory(RedemptionCategory redemptionCategory) { this.redemptionCategory = redemptionCategory; }

    public String getConversionFormula() { return conversionFormula; }
    public void setConversionFormula(String conversionFormula) { this.conversionFormula = conversionFormula; }

    public BigDecimal getValuePerPoint() { return valuePerPoint; }
    public void setValuePerPoint(BigDecimal valuePerPoint) { this.valuePerPoint = valuePerPoint; }

    public BigDecimal getMinimumRedemption() { return minimumRedemption; }
    public void setMinimumRedemption(BigDecimal minimumRedemption) { this.minimumRedemption = minimumRedemption; }

    public String getTransferPartner() { return transferPartner; }
    public void setTransferPartner(String transferPartner) { this.transferPartner = transferPartner; }

    public BigDecimal getTransferRatio() { return transferRatio; }
    public void setTransferRatio(BigDecimal transferRatio) { this.transferRatio = transferRatio; }

    public Integer getPriorityRank() { return priorityRank; }
    public void setPriorityRank(Integer priorityRank) { this.priorityRank = priorityRank; }

    public Boolean getRecommendedFlag() { return recommendedFlag; }
    public void setRecommendedFlag(Boolean recommendedFlag) { this.recommendedFlag = recommendedFlag; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long cardId;
        private RedemptionCategory redemptionCategory;
        private String conversionFormula;
        private BigDecimal valuePerPoint;
        private BigDecimal minimumRedemption;
        private String transferPartner;
        private BigDecimal transferRatio;
        private Integer priorityRank;
        private Boolean recommendedFlag;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder cardId(Long cardId) { this.cardId = cardId; return this; }
        public Builder redemptionCategory(RedemptionCategory redemptionCategory) { this.redemptionCategory = redemptionCategory; return this; }
        public Builder conversionFormula(String conversionFormula) { this.conversionFormula = conversionFormula; return this; }
        public Builder valuePerPoint(BigDecimal valuePerPoint) { this.valuePerPoint = valuePerPoint; return this; }
        public Builder minimumRedemption(BigDecimal minimumRedemption) { this.minimumRedemption = minimumRedemption; return this; }
        public Builder transferPartner(String transferPartner) { this.transferPartner = transferPartner; return this; }
        public Builder transferRatio(BigDecimal transferRatio) { this.transferRatio = transferRatio; return this; }
        public Builder priorityRank(Integer priorityRank) { this.priorityRank = priorityRank; return this; }
        public Builder recommendedFlag(Boolean recommendedFlag) { this.recommendedFlag = recommendedFlag; return this; }
        public Builder active(Boolean active) { this.active = active; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public RewardResponseDto build() {
            return new RewardResponseDto(id, cardId, redemptionCategory, conversionFormula,
                    valuePerPoint, minimumRedemption, transferPartner, transferRatio,
                    priorityRank, recommendedFlag, active, createdAt, updatedAt);
        }
    }
}
