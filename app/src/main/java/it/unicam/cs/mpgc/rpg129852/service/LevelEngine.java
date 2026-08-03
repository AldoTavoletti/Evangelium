package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;

public interface LevelEngine {
    boolean hasNextPhase();
    LevelPhase getNextPhase();
    int getCurrentPhaseNumber();
    int getTotalNumberOfPhases();
}