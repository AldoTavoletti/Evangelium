package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;

public interface LevelEngine {
    boolean hasNextPhase();
    LevelPhase getNextPhase();
    int getCurrentPhaseNumber();
    int getTotalNumberOfPhases();
}