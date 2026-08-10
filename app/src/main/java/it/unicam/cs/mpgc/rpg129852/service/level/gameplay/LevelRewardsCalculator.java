package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.List;

/**
 * Defines the contract for calculating the final score and rewards of a level.
 * Evaluates the player's performance based on the sequence of answers provided.
 */
public interface LevelRewardsCalculator {

    /**
     * Calculates the final score achieved in the level.
     *
     * @param maxRewards the maximum possible virtues obtainable in the level
     * @param answers    the list of answers submitted by the player during the level phases
     * @return a {@link Score} object containing the earned virtues and the completion state
     * @throws NullPointerException if maxRewards or answers are null
     */
    Score calculate(Virtues maxRewards, List<PhaseAnswer> answers);
}