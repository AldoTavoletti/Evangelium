package it.unicam.cs.mpgc.rpg129852.service.level;

import it.unicam.cs.mpgc.rpg129852.dto.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

import java.util.List;

public interface LevelRewardsCalculator {
    Virtues calculate(Virtues maxRewards, List<PhaseAnswer> answers);
}
