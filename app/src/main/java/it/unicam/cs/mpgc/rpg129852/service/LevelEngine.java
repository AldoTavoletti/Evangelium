package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;

public interface LevelEngine {
    String getNpcImagePath();
    boolean hasNextPhase();
    LevelPhase getNextPhase();
    double getMaxProblemValue();
}