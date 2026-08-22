package com.redeemwise.card.mapper;

import com.redeemwise.card.dto.request.CreateCardRequestDto;
import com.redeemwise.card.dto.request.UpdateCardRequestDto;
import com.redeemwise.card.dto.response.CardResponseDto;
import com.redeemwise.card.entity.Card;

/**
 * Mapper class for converting between Card entity and DTOs.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
public final class CardMapper {

    private CardMapper() {
        // Private constructor to prevent instantiation
    }

    /**
     * Convert CreateCardRequestDto to Card entity.
     *
     * @param dto the create card request DTO
     * @return the Card entity
     */
    public static Card toEntity(CreateCardRequestDto dto) {
        return Card.builder()
                .cardName(dto.getCardName())
                .bankName(dto.getBankName())
                .network(dto.getNetwork())
                .rewardType(dto.getRewardType())
                .annualFee(dto.getAnnualFee())
                .joiningFee(dto.getJoiningFee())
                .active(true)
                .build();
    }

    /**
     * Update Card entity from UpdateCardRequestDto.
     *
     * @param entity the existing Card entity
     * @param dto the update card request DTO
     * @return the updated Card entity
     */
    public static Card updateEntity(Card entity, UpdateCardRequestDto dto) {
        entity.setCardName(dto.getCardName());
        entity.setBankName(dto.getBankName());
        entity.setNetwork(dto.getNetwork());
        entity.setRewardType(dto.getRewardType());
        entity.setAnnualFee(dto.getAnnualFee());
        entity.setJoiningFee(dto.getJoiningFee());
        return entity;
    }

    /**
     * Convert Card entity to CardResponseDto.
     *
     * @param card the Card entity
     * @return the CardResponseDto
     */
    public static CardResponseDto toResponse(Card card) {
        return CardResponseDto.builder()
                .id(card.getId())
                .cardName(card.getCardName())
                .bankName(card.getBankName())
                .network(card.getNetwork())
                .rewardType(card.getRewardType())
                .annualFee(card.getAnnualFee())
                .joiningFee(card.getJoiningFee())
                .active(card.getActive())
                .createdAt(card.getCreatedAt())
                .updatedAt(card.getUpdatedAt())
                .build();
    }
}
