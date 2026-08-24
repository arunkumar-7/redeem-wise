package com.redeemwise.recommendation.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing card data received from Card Service via OpenFeign.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("cardName")
    private String cardName;

    @JsonProperty("bankName")
    private String bankName;

    @JsonProperty("network")
    private String network;

    @JsonProperty("rewardType")
    private String rewardType;

    @JsonProperty("annualFee")
    private BigDecimal annualFee;

    @JsonProperty("joiningFee")
    private BigDecimal joiningFee;

    @JsonProperty("active")
    private Boolean active;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    public CardResponseDto() {}

    public CardResponseDto(Long id, String cardName, String bankName, String network,
                           String rewardType, BigDecimal annualFee, BigDecimal joiningFee,
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

    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }

    public String getRewardType() { return rewardType; }
    public void setRewardType(String rewardType) { this.rewardType = rewardType; }

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
        private String network;
        private String rewardType;
        private BigDecimal annualFee;
        private BigDecimal joiningFee;
        private Boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder cardName(String cardName) { this.cardName = cardName; return this; }
        public Builder bankName(String bankName) { this.bankName = bankName; return this; }
        public Builder network(String network) { this.network = network; return this; }
        public Builder rewardType(String rewardType) { this.rewardType = rewardType; return this; }
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
