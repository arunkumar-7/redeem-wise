package com.redeemwise.reward.service;

import com.redeemwise.reward.dto.request.CreateRewardRequestDto;
import com.redeemwise.reward.dto.request.UpdateRewardRequestDto;
import com.redeemwise.reward.dto.response.RewardResponseDto;
import com.redeemwise.reward.entity.RewardOption;
import com.redeemwise.reward.exception.RewardNotFoundException;
import com.redeemwise.reward.mapper.RewardMapper;
import com.redeemwise.reward.repository.RewardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling reward option management operations.
 *
 * @author RedeemWise Team
 * @version 1.0
 * @since 2024-01-15
 */
@Service
public class RewardService {

    private static final Logger log = LoggerFactory.getLogger(RewardService.class);

    private final RewardRepository rewardRepository;

    public RewardService(RewardRepository rewardRepository) {
        this.rewardRepository = rewardRepository;
    }

    /**
     * Create a new reward option.
     *
     * @param request the create reward request DTO
     * @return the reward response DTO
     */
    @Transactional
    public RewardResponseDto createReward(CreateRewardRequestDto request) {
        log.info("Creating new reward option for cardId: {}, category: {}",
                request.getCardId(), request.getRedemptionCategory());

        RewardOption rewardOption = RewardMapper.toEntity(request);
        RewardOption savedReward = rewardRepository.save(rewardOption);

        log.info("Reward option created successfully with id: {}", savedReward.getId());
        return RewardMapper.toResponse(savedReward);
    }

    /**
     * Get all active reward options.
     *
     * @return list of reward response DTOs
     */
    @Transactional(readOnly = true)
    public List<RewardResponseDto> getAllRewards() {
        log.info("Fetching all active reward options");

        List<RewardOption> rewards = rewardRepository.findByActiveTrue();

        log.info("Found {} active reward options", rewards.size());
        return rewards.stream()
                .map(RewardMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get a reward option by ID.
     *
     * @param id the reward option ID
     * @return the reward response DTO
     * @throws RewardNotFoundException if reward option not found
     */
    @Transactional(readOnly = true)
    public RewardResponseDto getRewardById(Long id) {
        log.info("Fetching reward option with id: {}", id);

        RewardOption rewardOption = rewardRepository.findById(id)
                .filter(RewardOption::getActive)
                .orElseThrow(() -> new RewardNotFoundException("Reward option not found with id: " + id));

        return RewardMapper.toResponse(rewardOption);
    }

    /**
     * Get all active reward options for a specific card.
     *
     * @param cardId the card ID
     * @return list of reward response DTOs
     */
    @Transactional(readOnly = true)
    public List<RewardResponseDto> getRewardsByCardId(Long cardId) {
        log.info("Fetching active reward options for cardId: {}", cardId);

        List<RewardOption> rewards = rewardRepository.findByActiveTrueAndCardId(cardId);

        log.info("Found {} active reward options for cardId: {}", rewards.size(), cardId);
        return rewards.stream()
                .map(RewardMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Update a reward option by ID.
     *
     * @param id the reward option ID
     * @param request the update reward request DTO
     * @return the updated reward response DTO
     * @throws RewardNotFoundException if reward option not found
     */
    @Transactional
    public RewardResponseDto updateReward(Long id, UpdateRewardRequestDto request) {
        log.info("Updating reward option with id: {}", id);

        RewardOption rewardOption = rewardRepository.findById(id)
                .filter(RewardOption::getActive)
                .orElseThrow(() -> new RewardNotFoundException("Reward option not found with id: " + id));

        RewardOption updatedReward = RewardMapper.updateEntity(rewardOption, request);
        RewardOption savedReward = rewardRepository.save(updatedReward);

        log.info("Reward option updated successfully with id: {}", savedReward.getId());
        return RewardMapper.toResponse(savedReward);
    }

    /**
     * Soft delete a reward option by ID.
     *
     * @param id the reward option ID
     * @throws RewardNotFoundException if reward option not found
     */
    @Transactional
    public void deleteReward(Long id) {
        log.info("Soft deleting reward option with id: {}", id);

        RewardOption rewardOption = rewardRepository.findById(id)
                .filter(RewardOption::getActive)
                .orElseThrow(() -> new RewardNotFoundException("Reward option not found with id: " + id));

        rewardOption.setActive(false);
        rewardRepository.save(rewardOption);

        log.info("Reward option soft deleted successfully with id: {}", id);
    }
}
