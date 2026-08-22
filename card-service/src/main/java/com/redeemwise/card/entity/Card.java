package com.redeemwise.card.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Card entity representing the master catalog of supported credit cards.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_name", nullable = false, length = 150)
    private String cardName;

    @Column(name = "bank_name", nullable = false, length = 150)
    private String bankName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Network network;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", nullable = false, length = 20)
    private RewardType rewardType;

    @Column(name = "annual_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal annualFee;

    @Column(name = "joining_fee", nullable = false, precision = 10, scale = 2)
    private BigDecimal joiningFee;

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

    public Card() {}

    public Card(Long id, String cardName, String bankName, Network network,
                RewardType rewardType, BigDecimal annualFee, BigDecimal joiningFee,
                Boolean active) {
        this.id = id;
        this.cardName = cardName;
        this.bankName = bankName;
        this.network = network;
        this.rewardType = rewardType;
        this.annualFee = annualFee;
        this.joiningFee = joiningFee;
        this.active = active;
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

        public Builder id(Long id) { this.id = id; return this; }
        public Builder cardName(String cardName) { this.cardName = cardName; return this; }
        public Builder bankName(String bankName) { this.bankName = bankName; return this; }
        public Builder network(Network network) { this.network = network; return this; }
        public Builder rewardType(RewardType rewardType) { this.rewardType = rewardType; return this; }
        public Builder annualFee(BigDecimal annualFee) { this.annualFee = annualFee; return this; }
        public Builder joiningFee(BigDecimal joiningFee) { this.joiningFee = joiningFee; return this; }
        public Builder active(Boolean active) { this.active = active; return this; }

        public Card build() {
            return new Card(id, cardName, bankName, network, rewardType,
                    annualFee, joiningFee, active);
        }
    }
}
