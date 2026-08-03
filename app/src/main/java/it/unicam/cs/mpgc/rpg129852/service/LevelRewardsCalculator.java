package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.UserPerformanceDetails;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

public interface LevelRewardsCalculator {
    Virtues calculate(Virtues maxRewards, int numberOfPhasesInLevel, UserPerformanceDetails performanceDetails);
}
