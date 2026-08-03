package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.model.Level;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

public interface LevelSessionManager {
    void setCurrentLevel(Level level);
    String getCurrentLevelId();
    Virtues getMaxRewards();
    double getMaxProblemValue();
    String getNpcImagePath();
    LevelPhase[] getPhases();
}
