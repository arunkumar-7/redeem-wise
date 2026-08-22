package com.redeemwise.card.dto.response;

import com.redeemwise.card.entity.Network;
import com.redeemwise.card.entity.RewardType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for card response.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class CardResponseDto {

    private Long id;
    private String cardName;
    private String bankName;
    private Network network;
    private RewardType rewardType;
    private BigDecimal annualFee;
    private BigDecimal joiningFee;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CardResponseDto() {}

    public CardResponseDto(Long id, String cardName, String bankName, Network network,
                           RewardType rewardType, BigDecimal annualFee, BigDecimal joiningFee,
                           Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.cardName = cardName;
        this.bankName = bankName;
        this.network = network;
        this.rewardType = rewardType;
        this.annualFee = annualFee;
        this.joiningFee = joiningFee;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String cardName;
        private String bankName;
        private Network network;
        private RewardType rewardType;
        private BigDecimal annualFee;
        private BigDecimal joiningFee;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder cardName(String cardName) { this.cardName = cardName; return this; }
        public Builder bankName(String bankName) { this.bankName = bankName; return this; }
        public Builder network(Network network) { this.network = network; return this; }
        public Builder rewardType(RewardType rewardType) { this.rewardType = rewardType; return this; }
        public Builder annualFee(BigDecimal annualFee) { this.annualFee = annualFee; return this; }
        public Builder joiningFee(BigDecimal joiningFee) { this.joiningFee = joiningFee; return this; }
        public Builder active(Boolean active) { this.active = active; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public CardResponseDto build() {
            return new CardResponseDto(id, cardName, bankName, network, rewardType,
                    annualFee, joiningFee, active, createdAt, updatedAt);
        }
    }
}
