package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;

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