package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;

public interface GameplayService {

    boolean hasNextPhase();

    LevelPhase getNextPhase();

    int getCurrentPhaseNumber();

    int getTotalNumberOfPhases();

    double getMaxProblemValue();

    String getNpcImagePath();

    void completeLevel(double remainingProblem);

}