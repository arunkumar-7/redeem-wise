package com.redeemwise.card.repository;

import com.redeemwise.card.entity.Card;
import com.redeemwise.card.entity.Network;
import com.redeemwise.card.entity.RewardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Card entity operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    /**
     * Find all active cards.
     *
     * @return list of active cards
     */
    List<Card> findByActiveTrue();

    /**
     * Find active cards by bank name.
     *
     * @param bankName the bank name to search for
     * @return list of matching active cards
     */
    List<Card> findByActiveTrueAndBankNameContainingIgnoreCase(String bankName);

    /**
     * Find active cards by network.
     *
     * @param network the network to filter by
     * @return list of matching active cards
     */
    List<Card> findByActiveTrueAndNetwork(Network network);

    /**
     * Find active cards by reward type.
     *
     * @param rewardType the reward type to filter by
     * @return list of matching active cards
     */
    List<Card> findByActiveTrueAndRewardType(RewardType rewardType);

    /**
     * Find active cards by bank name and network.
     *
     * @param bankName the bank name to search for
     * @param network the network to filter by
     * @return list of matching active cards
     */
    List<Card> findByActiveTrueAndBankNameContainingIgnoreCaseAndNetwork(String bankName, Network network);

    /**
     * Find active cards by bank name and reward type.
     *
     * @param bankName the bank name to search for
     * @param rewardType the reward type to filter by
     * @return list of matching active cards
     */
    List<Card> findByActiveTrueAndBankNameContainingIgnoreCaseAndRewardType(String bankName, RewardType rewardType);

    /**
     * Find active cards by network and reward type.
     *
     * @param network the network to filter by
     * @param rewardType the reward type to filter by
     * @return list of matching active cards
     */
    List<Card> findByActiveTrueAndNetworkAndRewardType(Network network, RewardType rewardType);

    /**
     * Find active cards by bank name, network, and reward type.
     *
     * @param bankName the bank name to search for
     * @param network the network to filter by
     * @param rewardType the reward type to filter by
     * @return list of matching active cards
     */
    List<Card> findByActiveTrueAndBankNameContainingIgnoreCaseAndNetworkAndRewardType(
            String bankName, Network network, RewardType rewardType);
}
