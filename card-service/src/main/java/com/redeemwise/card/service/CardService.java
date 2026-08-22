package com.redeemwise.card.service;

import com.redeemwise.card.dto.request.CreateCardRequestDto;
import com.redeemwise.card.dto.request.UpdateCardRequestDto;
import com.redeemwise.card.dto.response.CardResponseDto;
import com.redeemwise.card.entity.Card;
import com.redeemwise.card.entity.Network;
import com.redeemwise.card.entity.RewardType;
import com.redeemwise.card.exception.CardNotFoundException;
import com.redeemwise.card.mapper.CardMapper;
import com.redeemwise.card.repository.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling card management operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Service
public class CardService {

    private static final Logger log = LoggerFactory.getLogger(CardService.class);

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    /**
     * Create a new card.
     *
     * @param request the create card request DTO
     * @return the card response DTO
     */
    @Transactional
    public CardResponseDto createCard(CreateCardRequestDto request) {
        log.info("Creating new card: {} for bank: {}", request.getCardName(), request.getBankName());

        Card card = CardMapper.toEntity(request);
        Card savedCard = cardRepository.save(card);

        log.info("Card created successfully with id: {}", savedCard.getId());
        return CardMapper.toResponse(savedCard);
    }

    /**
     * Get all active cards.
     *
     * @return list of card response DTOs
     */
    @Transactional(readOnly = true)
    public List<CardResponseDto> getAllCards() {
        log.info("Fetching all active cards");

        List<Card> cards = cardRepository.findByActiveTrue();

        log.info("Found {} active cards", cards.size());
        return cards.stream()
                .map(CardMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a card by ID.
     *
     * @param id the card ID
     * @return the card response DTO
     * @throws CardNotFoundException if card not found
     */
    @Transactional(readOnly = true)
    public CardResponseDto getCardById(Long id) {
        log.info("Fetching card with id: {}", id);

        Card card = cardRepository.findById(id)
                .filter(Card::getActive)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        return CardMapper.toResponse(card);
    }

    /**
     * Update a card by ID.
     *
     * @param id the card ID
     * @param request the update card request DTO
     * @return the updated card response DTO
     * @throws CardNotFoundException if card not found
     */
    @Transactional
    public CardResponseDto updateCard(Long id, UpdateCardRequestDto request) {
        log.info("Updating card with id: {}", id);

        Card card = cardRepository.findById(id)
                .filter(Card::getActive)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        Card updatedCard = CardMapper.updateEntity(card, request);
        Card savedCard = cardRepository.save(updatedCard);

        log.info("Card updated successfully with id: {}", savedCard.getId());
        return CardMapper.toResponse(savedCard);
    }

    /**
     * Soft delete a card by ID.
     *
     * @param id the card ID
     * @throws CardNotFoundException if card not found
     */
    @Transactional
    public void deleteCard(Long id) {
        log.info("Soft deleting card with id: {}", id);

        Card card = cardRepository.findById(id)
                .filter(Card::getActive)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));

        card.setActive(false);
        cardRepository.save(card);

        log.info("Card soft deleted successfully with id: {}", id);
    }

    /**
     * Search cards by bank name, network, and/or reward type.
     *
     * @param bankName the bank name filter (optional)
     * @param network the network filter (optional)
     * @param rewardType the reward type filter (optional)
     * @return list of matching card response DTOs
     */
    @Transactional(readOnly = true)
    public List<CardResponseDto> searchCards(String bankName, Network network, RewardType rewardType) {
        log.info("Searching cards with bankName: {}, network: {}, rewardType: {}",
                bankName, network, rewardType);

        List<Card> cards;

        if (bankName != null && network != null && rewardType != null) {
            cards = cardRepository.findByActiveTrueAndBankNameContainingIgnoreCaseAndNetworkAndRewardType(
                    bankName, network, rewardType);
        } else if (bankName != null && network != null) {
            cards = cardRepository.findByActiveTrueAndBankNameContainingIgnoreCaseAndNetwork(
                    bankName, network);
        } else if (bankName != null && rewardType != null) {
            cards = cardRepository.findByActiveTrueAndBankNameContainingIgnoreCaseAndRewardType(
                    bankName, rewardType);
        } else if (network != null && rewardType != null) {
            cards = cardRepository.findByActiveTrueAndNetworkAndRewardType(network, rewardType);
        } else if (bankName != null) {
            cards = cardRepository.findByActiveTrueAndBankNameContainingIgnoreCase(bankName);
        } else if (network != null) {
            cards = cardRepository.findByActiveTrueAndNetwork(network);
        } else if (rewardType != null) {
            cards = cardRepository.findByActiveTrueAndRewardType(rewardType);
        } else {
            cards = cardRepository.findByActiveTrue();
        }

        log.info("Found {} cards matching search criteria", cards.size());
        return cards.stream()
                .map(CardMapper::toResponse)
                .collect(Collectors.toList());
    }
}
