package it.unicam.cs.mpgc.rpg129852.service.level;

import it.unicam.cs.mpgc.rpg129852.dto.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

import java.util.List;

public class LevelRewardsCalculatorImpl implements LevelRewardsCalculator {

    private static final double REQUIRED_HEAL = 1.0;

    @Override
    public Virtues calculate(Virtues maxRewards, List<PhaseAnswer> answers) {

        double totalHeal = answers.stream()
                .mapToDouble(PhaseAnswer::getHealValue)
                .sum();

        if (totalHeal < REQUIRED_HEAL) {
            return new Virtues(0, 0, 0);
        }

        if (answers.contains(PhaseAnswer.BAD)) {
            return getHalfRewards(maxRewards);
        } else {
            return maxRewards;

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