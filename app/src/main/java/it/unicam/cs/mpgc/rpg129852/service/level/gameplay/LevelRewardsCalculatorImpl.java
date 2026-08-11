package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCompletionState;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of the {@link LevelRewardsCalculator}.
 * It computes the final rewards applying specific gameplay rules:
 * - If the total healing value is below the required threshold, the level is failed (no rewards).
 * - If the player gave at least one 'BAD' answer, they receive half of the rewards (partial success).
 * - Otherwise, the player receives the maximum rewards (perfect success).
 */
public class LevelRewardsCalculatorImpl implements LevelRewardsCalculator {

    private static final double REQUIRED_HEAL = 1.0;
    private static final Virtues ZERO_REWARDS = new Virtues(0, 0, 0);

    @Override
    public Score calculate(Virtues maxRewards, List<PhaseAnswer> answers) {
        Objects.requireNonNull(maxRewards, "The maximum rewards must not be null.");
        Objects.requireNonNull(answers, "The list of answers must not be null.");

        double totalHeal = calculateTotalHeal(answers);

        if (totalHeal < REQUIRED_HEAL) {
            return new Score(ZERO_REWARDS, LevelCompletionState.FAILED);
        }

        if (answers.contains(PhaseAnswer.BAD)) {
            return new Score(getHalfRewards(maxRewards), LevelCompletionState.GOOD);
        }

        return new Score(maxRewards, LevelCompletionState.PERFECT);
    }

    private double calculateTotalHeal(List<PhaseAnswer> answers) {
        return answers.stream()
                .mapToDouble(PhaseAnswer::getHealValue)
                .sum();
    }

    private Virtues getHalfRewards(Virtues maxRewards) {
        return new Virtues(
                maxRewards.faith() / 2,
                maxRewards.hope() / 2,
                maxRewards.love() / 2
        );
    }
}