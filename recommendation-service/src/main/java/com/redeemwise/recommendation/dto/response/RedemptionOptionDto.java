package com.redeemwise.recommendation.dto.response;

import java.math.BigDecimal;

/**
 * DTO representing a single redemption option within a recommendation response.
 * Contains the original reward option data plus calculated redemption value.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class RedemptionOptionDto {

    private Long id;
    private String name;
    private String category;
    private BigDecimal valuePerPoint;
    private Integer pointsRequired;
    private BigDecimal estimatedValue;
    private BigDecimal minimumRedemption;
    private String conversionFormula;
    private String transferPartner;
    private Integer rank;
    private Boolean isRecommended;
    private Boolean isEligible;
    private String ineligibilityReason;

    public RedemptionOptionDto() {}

    public RedemptionOptionDto(Long id, String name, String category, BigDecimal valuePerPoint,
                                Integer pointsRequired, BigDecimal estimatedValue,
                                BigDecimal minimumRedemption, String conversionFormula,
                                String transferPartner, Integer rank, Boolean isRecommended,
                                Boolean isEligible, String ineligibilityReason) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.valuePerPoint = valuePerPoint;
        this.pointsRequired = pointsRequired;
        this.estimatedValue = estimatedValue;
        this.minimumRedemption = minimumRedemption;
        this.conversionFormula = conversionFormula;
        this.transferPartner = transferPartner;
        this.rank = rank;
        this.isRecommended = isRecommended;
        this.isEligible = isEligible;
        this.ineligibilityReason = ineligibilityReason;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getValuePerPoint() { return valuePerPoint; }
    public void setValuePerPoint(BigDecimal valuePerPoint) { this.valuePerPoint = valuePerPoint; }

    public Integer getPointsRequired() { return pointsRequired; }
    public void setPointsRequired(Integer pointsRequired) { this.pointsRequired = pointsRequired; }

    public BigDecimal getEstimatedValue() { return estimatedValue; }
    public void setEstimatedValue(BigDecimal estimatedValue) { this.estimatedValue = estimatedValue; }

    public BigDecimal getMinimumRedemption() { return minimumRedemption; }
    public void setMinimumRedemption(BigDecimal minimumRedemption) { this.minimumRedemption = minimumRedemption; }

    public String getConversionFormula() { return conversionFormula; }
    public void setConversionFormula(String conversionFormula) { this.conversionFormula = conversionFormula; }

    public String getTransferPartner() { return transferPartner; }
    public void setTransferPartner(String transferPartner) { this.transferPartner = transferPartner; }

    public Integer getRank() { return rank; }
    public void setRank(Integer rank) { this.rank = rank; }

    public Boolean getIsRecommended() { return isRecommended; }
    public void setIsRecommended(Boolean isRecommended) { this.isRecommended = isRecommended; }

    public Boolean getIsEligible() { return isEligible; }
    public void setIsEligible(Boolean isEligible) { this.isEligible = isEligible; }

    public String getIneligibilityReason() { return ineligibilityReason; }
    public void setIneligibilityReason(String ineligibilityReason) { this.ineligibilityReason = ineligibilityReason; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String name;
        private String category;
        private BigDecimal valuePerPoint;
        private Integer pointsRequired;
        private BigDecimal estimatedValue;
        private BigDecimal minimumRedemption;
        private String conversionFormula;
        private String transferPartner;
        private Integer rank;
        private Boolean isRecommended;
        private Boolean isEligible;
        private String ineligibilityReason;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder category(String category) { this.category = category; return this; }
        public Builder valuePerPoint(BigDecimal valuePerPoint) { this.valuePerPoint = valuePerPoint; return this; }
        public Builder pointsRequired(Integer pointsRequired) { this.pointsRequired = pointsRequired; return this; }
        public Builder estimatedValue(BigDecimal estimatedValue) { this.estimatedValue = estimatedValue; return this; }
        public Builder minimumRedemption(BigDecimal minimumRedemption) { this.minimumRedemption = minimumRedemption; return this; }
        public Builder conversionFormula(String conversionFormula) { this.conversionFormula = conversionFormula; return this; }
        public Builder transferPartner(String transferPartner) { this.transferPartner = transferPartner; return this; }
        public Builder rank(Integer rank) { this.rank = rank; return this; }
        public Builder isRecommended(Boolean isRecommended) { this.isRecommended = isRecommended; return this; }
        public Builder isEligible(Boolean isEligible) { this.isEligible = isEligible; return this; }
        public Builder ineligibilityReason(String ineligibilityReason) { this.ineligibilityReason = ineligibilityReason; return this; }

        public RedemptionOptionDto build() {
            return new RedemptionOptionDto(id, name, category, valuePerPoint, pointsRequired,
                    estimatedValue, minimumRedemption, conversionFormula, transferPartner,
                    rank, isRecommended, isEligible, ineligibilityReason);
        }
    }
}
