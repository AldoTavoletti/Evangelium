package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.context.GameSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.model.*;
import it.unicam.cs.mpgc.rpg129852.persistence.GameRepository;

public class LevelEngineImpl implements LevelEngine {

    private int phaseIndex = 0;
    private LevelPhase[] phases;

    public LevelEngineImpl(LevelPhase[] phases) {
        this.phases = phases;
    }

    public boolean hasNextPhase() {
        return phaseIndex < getTotalNumberOfPhases();
    }

    public LevelPhase getNextPhase() {

        LevelPhase nextPhase = phases[phaseIndex];
        phaseIndex++;

        return nextPhase;
    }

    @Override
    public int getCurrentPhaseNumber() {
        return phaseIndex;
    }

    @Override
    public int getTotalNumberOfPhases() {
        return phases.length;
    }

}