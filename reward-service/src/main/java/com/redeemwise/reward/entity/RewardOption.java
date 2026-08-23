package com.redeemwise.reward.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * RewardOption entity representing the master catalog of reward redemption options
 * available for credit cards.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Entity
@Table(name = "reward_options")
public class RewardOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id", nullable = false)
    private Long cardId;

    @Enumerated(EnumType.STRING)
    @Column(name = "redemption_category", nullable = false, length = 30)
    private RedemptionCategory redemptionCategory;

    @Column(name = "conversion_formula", length = 500)
    private String conversionFormula;

    @Column(name = "value_per_point", nullable = false, precision = 10, scale = 4)
    private BigDecimal valuePerPoint;

    @Column(name = "minimum_redemption", nullable = false, precision = 10, scale = 2)
    private BigDecimal minimumRedemption;

    @Column(name = "transfer_partner", length = 150)
    private String transferPartner;

    @Column(name = "transfer_ratio", precision = 10, scale = 4)
    private BigDecimal transferRatio;

    @Column(name = "priority_rank", nullable = false)
    private Integer priorityRank;

    @Column(name = "recommended_flag", nullable = false)
    private Boolean recommendedFlag = false;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public RewardOption() {}

    public RewardOption(Long id, Long cardId, RedemptionCategory redemptionCategory,
                        String conversionFormula, BigDecimal valuePerPoint,
                        BigDecimal minimumRedemption, String transferPartner,
                        BigDecimal transferRatio, Integer priorityRank,
                        Boolean recommendedFlag, Boolean active) {
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

        public RewardOption build() {
            return new RewardOption(id, cardId, redemptionCategory, conversionFormula,
                    valuePerPoint, minimumRedemption, transferPartner, transferRatio,
                    priorityRank, recommendedFlag, active);
        }
    }
}
