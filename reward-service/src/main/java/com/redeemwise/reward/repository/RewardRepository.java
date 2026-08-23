package com.redeemwise.reward.repository;

import com.redeemwise.reward.entity.RewardOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for RewardOption entity operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Repository
public interface RewardRepository extends JpaRepository<RewardOption, Long> {

    /**
     * Find all active reward options.
     *
     * @return list of active reward options
     */
    List<RewardOption> findByActiveTrue();

    /**
     * Find active reward options by card ID.
     *
     * @param cardId the card ID to filter by
     * @return list of matching active reward options
     */
    List<RewardOption> findByActiveTrueAndCardId(Long cardId);
}
