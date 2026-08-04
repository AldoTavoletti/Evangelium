package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.ProblemType;

public interface GameplayService {

    void saveAnswer(PhaseAnswer answer);

    boolean hasNextPhase();

    LevelPhase getNextPhase();

    int getCurrentPhaseNumber();

    int getTotalNumberOfPhases();

    double getMaxProblemValue();

    String getNpcImagePath();

    ProblemType getProblemType();

    void completeLevel(double remainingProblem);

}