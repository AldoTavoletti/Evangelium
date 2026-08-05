package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.ProblemType;

public interface GameplayService {

    void completeLevel();

    void submitAnswer(PhaseAnswer answer);

    boolean isLevelWon();

    boolean hasNextPhase();

    double getCurrentProblemValue();

    double getMaxProblemValue();

    LevelPhase getNextPhase();

    ProblemType getProblemType();

    int getCurrentPhaseNumber();

    int getTotalNumberOfPhases();

    String getNpcImagePath();
}