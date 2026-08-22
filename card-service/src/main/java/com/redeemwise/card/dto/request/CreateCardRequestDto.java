package com.redeemwise.card.dto.request;

import com.redeemwise.card.entity.Network;
import com.redeemwise.card.entity.RewardType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO for card creation request.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class CreateCardRequestDto {

    @NotBlank(message = "Card name is required")
    @Size(min = 2, max = 150, message = "Card name must be between 2 and 150 characters")
    private String cardName;

    @NotBlank(message = "Bank name is required")
    @Size(min = 2, max = 150, message = "Bank name must be between 2 and 150 characters")
    private String bankName;

    @NotNull(message = "Network is required")
    private Network network;

    @NotNull(message = "Reward type is required")
    private RewardType rewardType;

    @NotNull(message = "Annual fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Annual fee must be greater than or equal to 0")
    private BigDecimal annualFee;

    @NotNull(message = "Joining fee is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Joining fee must be greater than or equal to 0")
    private BigDecimal joiningFee;

    public CreateCardRequestDto() {}

    public CreateCardRequestDto(String cardName, String bankName, Network network,
                                RewardType rewardType, BigDecimal annualFee,
                                BigDecimal joiningFee) {
        this.cardName = cardName;
        this.bankName = bankName;
        this.network = network;
        this.rewardType = rewardType;
        this.annualFee = annualFee;
        this.joiningFee = joiningFee;
    }

    public String getCardName() { return cardName; }
    public void setCardName(String cardName) { this.cardName = cardName; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public Network getNetwork() { return network; }
    public void setNetwork(Network network) { this.network = network; }

    public RewardType getRewardType() { return rewardType; }
    public void setRewardType(RewardType rewardType) { this.rewardType = rewardType; }

    public BigDecimal getAnnualFee() { return annualFee; }
    public void setAnnualFee(BigDecimal annualFee) { this.annualFee = annualFee; }

    public BigDecimal getJoiningFee() { return joiningFee; }
    public void setJoiningFee(BigDecimal joiningFee) { this.joiningFee = joiningFee; }
}
