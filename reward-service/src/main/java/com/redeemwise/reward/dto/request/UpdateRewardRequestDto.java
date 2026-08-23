package com.redeemwise.reward.dto.request;

import com.redeemwise.reward.entity.RedemptionCategory;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO for reward option update request.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class UpdateRewardRequestDto {

    @NotNull(message = "Card ID is required")
    private Long cardId;

    @NotNull(message = "Redemption category is required")
    private RedemptionCategory redemptionCategory;

    @Size(max = 500, message = "Conversion formula must not exceed 500 characters")
    private String conversionFormula;

    @NotNull(message = "Value per point is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Value per point must be greater than or equal to 0")
    private BigDecimal valuePerPoint;

    @NotNull(message = "Minimum redemption is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Minimum redemption must be greater than or equal to 0")
    private BigDecimal minimumRedemption;

    @Size(max = 150, message = "Transfer partner must not exceed 150 characters")
    private String transferPartner;

    @DecimalMin(value = "0.0", inclusive = true, message = "Transfer ratio must be greater than or equal to 0")
    private BigDecimal transferRatio;

    @NotNull(message = "Priority rank is required")
    @Min(value = 1, message = "Priority rank must be greater than or equal to 1")
    private Integer priorityRank;

    private Boolean recommendedFlag = false;

    public UpdateRewardRequestDto() {}

    public UpdateRewardRequestDto(Long cardId, RedemptionCategory redemptionCategory,
                                   String conversionFormula, BigDecimal valuePerPoint,
                                   BigDecimal minimumRedemption, String transferPartner,
                                   BigDecimal transferRatio, Integer priorityRank,
                                   Boolean recommendedFlag) {
        this.cardId = cardId;
        this.redemptionCategory = redemptionCategory;
        this.conversionFormula = conversionFormula;
        this.valuePerPoint = valuePerPoint;
        this.minimumRedemption = minimumRedemption;
        this.transferPartner = transferPartner;
        this.transferRatio = transferRatio;
        this.priorityRank = priorityRank;
        this.recommendedFlag = recommendedFlag;
    }

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
}
