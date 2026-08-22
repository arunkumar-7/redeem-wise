package com.redeemwise.card.controller;

import com.redeemwise.card.dto.request.CreateCardRequestDto;
import com.redeemwise.card.dto.request.UpdateCardRequestDto;
import com.redeemwise.card.dto.response.CardResponseDto;
import com.redeemwise.card.entity.Network;
import com.redeemwise.card.entity.RewardType;
import com.redeemwise.card.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for card management operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@RestController
@RequestMapping("/api/cards")
@Tag(name = "Card Management", description = "Credit card catalog management operations")
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     * Create a new card.
     *
     * @param request the card creation request
     * @return the created card response
     */
    @PostMapping
    @Operation(summary = "Create a new card", description = "Add a new credit card to the catalog")
    public ResponseEntity<Map<String, Object>> createCard(
            @Valid @RequestBody CreateCardRequestDto request) {
        log.info("Received request to create card: {} for bank: {}",
                request.getCardName(), request.getBankName());

        CardResponseDto response = cardService.createCard(request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Card created successfully");
        body.put("data", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    /**
     * Get all active cards.
     *
     * @return list of all active cards
     */
    @GetMapping
    @Operation(summary = "Get all cards", description = "Retrieve all active credit cards from the catalog")
    public ResponseEntity<Map<String, Object>> getAllCards() {
        log.info("Received request to get all cards");

        List<CardResponseDto> cards = cardService.getAllCards();

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Cards retrieved successfully");
        body.put("data", cards);
        body.put("totalElements", cards.size());

        return ResponseEntity.ok(body);
    }

    /**
     * Get a card by ID.
     *
     * @param id the card ID
     * @return the card response
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get card by ID", description = "Retrieve a credit card by its unique identifier")
    public ResponseEntity<Map<String, Object>> getCardById(@PathVariable Long id) {
        log.info("Received request to get card with id: {}", id);

        CardResponseDto response = cardService.getCardById(id);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Card retrieved successfully");
        body.put("data", response);

        return ResponseEntity.ok(body);
    }

    /**
     * Update a card by ID.
     *
     * @param id the card ID
     * @param request the card update request
     * @return the updated card response
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a card", description = "Update an existing credit card in the catalog")
    public ResponseEntity<Map<String, Object>> updateCard(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCardRequestDto request) {
        log.info("Received request to update card with id: {}", id);

        CardResponseDto response = cardService.updateCard(id, request);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Card updated successfully");
        body.put("data", response);

        return ResponseEntity.ok(body);
    }

    /**
     * Soft delete a card by ID.
     *
     * @param id the card ID
     * @return the deletion response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a card", description = "Soft delete a credit card from the catalog")
    public ResponseEntity<Map<String, String>> deleteCard(@PathVariable Long id) {
        log.info("Received request to delete card with id: {}", id);

        cardService.deleteCard(id);

        Map<String, String> body = new HashMap<>();
        body.put("message", "Card deleted successfully");

        return ResponseEntity.ok(body);
    }

    /**
     * Search cards by filters.
     *
     * @param bankName the bank name filter (optional)
     * @param network the network filter (optional)
     * @param rewardType the reward type filter (optional)
     * @return list of matching cards
     */
    @GetMapping("/search")
    @Operation(summary = "Search cards", description = "Search credit cards by bank name, network, and/or reward type")
    public ResponseEntity<Map<String, Object>> searchCards(
            @RequestParam(required = false) String bankName,
            @RequestParam(required = false) Network network,
            @RequestParam(required = false) RewardType rewardType) {
        log.info("Received search request with bankName: {}, network: {}, rewardType: {}",
                bankName, network, rewardType);

        List<CardResponseDto> cards = cardService.searchCards(bankName, network, rewardType);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Cards retrieved successfully");
        body.put("data", cards);
        body.put("totalElements", cards.size());

        return ResponseEntity.ok(body);
    }
}
