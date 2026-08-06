package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.ProblemType;

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