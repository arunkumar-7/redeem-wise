package com.redeemwise.reward.mapper;

import com.redeemwise.reward.dto.request.CreateRewardRequestDto;
import com.redeemwise.reward.dto.request.UpdateRewardRequestDto;
import com.redeemwise.reward.dto.response.RewardResponseDto;
import com.redeemwise.reward.entity.RewardOption;

/**
 * Mapper class for converting between RewardOption entity and DTOs.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public final class RewardMapper {

    private RewardMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Convert CreateRewardRequestDto to RewardOption entity.
     *
     * @param dto the create reward request DTO
     * @return the RewardOption entity
     */
    public static RewardOption toEntity(CreateRewardRequestDto dto) {
        return RewardOption.builder()
                .cardId(dto.getCardId())
                .redemptionCategory(dto.getRedemptionCategory())
                .conversionFormula(dto.getConversionFormula())
                .valuePerPoint(dto.getValuePerPoint())
                .minimumRedemption(dto.getMinimumRedemption())
                .transferPartner(dto.getTransferPartner())
                .transferRatio(dto.getTransferRatio())
                .priorityRank(dto.getPriorityRank())
                .recommendedFlag(dto.getRecommendedFlag() != null ? dto.getRecommendedFlag() : false)
                .active(true)
                .build();
    }

    /**
     * Update RewardOption entity from UpdateRewardRequestDto.
     *
     * @param entity the existing RewardOption entity
     * @param dto the update reward request DTO
     * @return the updated RewardOption entity
     */
    public static RewardOption updateEntity(RewardOption entity, UpdateRewardRequestDto dto) {
        entity.setCardId(dto.getCardId());
        entity.setRedemptionCategory(dto.getRedemptionCategory());
        entity.setConversionFormula(dto.getConversionFormula());
        entity.setValuePerPoint(dto.getValuePerPoint());
        entity.setMinimumRedemption(dto.getMinimumRedemption());
        entity.setTransferPartner(dto.getTransferPartner());
        entity.setTransferRatio(dto.getTransferRatio());
        entity.setPriorityRank(dto.getPriorityRank());
        entity.setRecommendedFlag(dto.getRecommendedFlag() != null ? dto.getRecommendedFlag() : false);
        return entity;
    }

    /**
     * Convert RewardOption entity to RewardResponseDto.
     *
     * @param rewardOption the RewardOption entity
     * @return the RewardResponseDto
     */
    public static RewardResponseDto toResponse(RewardOption rewardOption) {
        return RewardResponseDto.builder()
                .id(rewardOption.getId())
                .cardId(rewardOption.getCardId())
                .redemptionCategory(rewardOption.getRedemptionCategory())
                .conversionFormula(rewardOption.getConversionFormula())
                .valuePerPoint(rewardOption.getValuePerPoint())
                .minimumRedemption(rewardOption.getMinimumRedemption())
                .transferPartner(rewardOption.getTransferPartner())
                .transferRatio(rewardOption.getTransferRatio())
                .priorityRank(rewardOption.getPriorityRank())
                .recommendedFlag(rewardOption.getRecommendedFlag())
                .active(rewardOption.getActive())
                .createdAt(rewardOption.getCreatedAt())
                .updatedAt(rewardOption.getUpdatedAt())
                .build();
    }
}
