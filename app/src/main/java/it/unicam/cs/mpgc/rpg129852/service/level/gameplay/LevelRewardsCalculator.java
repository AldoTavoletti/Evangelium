package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.List;

public interface LevelRewardsCalculator {
    Score calculate(Virtues maxRewards, List<PhaseAnswer> answers);
}
