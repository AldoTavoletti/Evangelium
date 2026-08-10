package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCompletionState;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.List;

public class LevelRewardsCalculatorImpl implements LevelRewardsCalculator {

    private static final double REQUIRED_HEAL = 1.0;

    @Override
    public Score calculate(Virtues maxRewards, List<PhaseAnswer> answers) {

        double totalHeal = answers.stream()
                .mapToDouble(PhaseAnswer::getHealValue)
                .sum();

        if (totalHeal < REQUIRED_HEAL) {
            return new Score(new Virtues(0, 0, 0), LevelCompletionState.FAILED);
        }

        if (answers.contains(PhaseAnswer.BAD)) {
            return new Score(getHalfRewards(maxRewards), LevelCompletionState.GOOD);
        } else {
            return new Score(maxRewards, LevelCompletionState.PERFECT);

        }
    }

    private Virtues getHalfRewards(Virtues maxRewards) {
        return new Virtues(
                maxRewards.faith() / 2,
                maxRewards.hope() / 2,
                maxRewards.love() / 2
        );
    }
}