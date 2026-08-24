package com.redeemwise.recommendation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for recommendation request.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public class RecommendationRequestDto {

    @NotNull(message = "Card ID is required")
    private Long cardId;

    @NotNull(message = "Available points is required")
    @Min(value = 1, message = "Available points must be at least 1")
    private Integer availablePoints;

    private String categoryFilter;

    public RecommendationRequestDto() {}

    public RecommendationRequestDto(Long cardId, Integer availablePoints, String categoryFilter) {
        this.cardId = cardId;
        this.availablePoints = availablePoints;
        this.categoryFilter = categoryFilter;
    }

    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }

    public Integer getAvailablePoints() { return availablePoints; }
    public void setAvailablePoints(Integer availablePoints) { this.availablePoints = availablePoints; }

    public String getCategoryFilter() { return categoryFilter; }
    public void setCategoryFilter(String categoryFilter) { this.categoryFilter = categoryFilter; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long cardId;
        private Integer availablePoints;
        private String categoryFilter;

        public Builder cardId(Long cardId) { this.cardId = cardId; return this; }
        public Builder availablePoints(Integer availablePoints) { this.availablePoints = availablePoints; return this; }
        public Builder categoryFilter(String categoryFilter) { this.categoryFilter = categoryFilter; return this; }

        public RecommendationRequestDto build() {
            return new RecommendationRequestDto(cardId, availablePoints, categoryFilter);
        }
    }
}
