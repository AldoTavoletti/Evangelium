package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.UserPerformanceDetails;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

public class LevelRewardsCalculatorImpl implements LevelRewardsCalculator {

    @Override
    public Virtues calculate(Virtues maxRewards, int numberOfPhasesInLevel,UserPerformanceDetails performanceDetails) {

        if (performanceDetails.problemProgress() > 0.0) {
            return new Virtues(0, 0, 0);
        }

        if (performanceDetails.phasesNeeded() < numberOfPhasesInLevel) {
            return maxRewards;
        } else {
            return getHalfRewards(maxRewards);
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