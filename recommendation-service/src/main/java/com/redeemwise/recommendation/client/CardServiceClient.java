package com.redeemwise.recommendation.client;

import com.redeemwise.recommendation.dto.external.CardResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * OpenFeign client for communicating with Card Service.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@FeignClient(name = "card-service", path = "/api/cards")
public interface CardServiceClient {

    /**
     * Retrieve a card by its ID from Card Service.
     *
     * @param id the card ID
     * @return the card response wrapped in a standard API response map
     */
    @GetMapping("/{id}")
    Map<String, Object> getCardById(@PathVariable("id") Long id);
}
