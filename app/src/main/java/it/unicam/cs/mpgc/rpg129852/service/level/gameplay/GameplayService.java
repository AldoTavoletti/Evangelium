package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.ProblemType;

public interface GameplayService {

    void submitAnswer(PhaseAnswer answer);
    boolean isLevelWon();
    void completeLevel();
    boolean hasNextPhase();
    LevelPhase getNextPhase();
    int getCurrentPhaseNumber();
    int getTotalNumberOfPhases();
    double getCurrentProblemValue();
    double getMaxProblemValue();
    ProblemType getProblemType();
    String getNpcImagePath();

}