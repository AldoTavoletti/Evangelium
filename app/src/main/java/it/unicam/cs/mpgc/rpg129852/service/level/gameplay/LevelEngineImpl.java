package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Concrete implementation of the {@link LevelEngine}.
 * Manages the sequential iteration over an array of level phases.
 */
public class LevelEngineImpl implements LevelEngine {

    private final LevelPhase[] phases;
    private int phaseIndex = 0;

    /**
     * Constructs a new level engine with the specified array of phases.
     *
     * @param phases the array of phases representing the level's progression
     * @throws NullPointerException if the provided array is null
     */
    public LevelEngineImpl(LevelPhase[] phases) {
        Objects.requireNonNull(phases, "The phases array must not be null.");

        // creates a defensive copy to prevent modification from the outside
        this.phases = phases.clone();
    }

    @Override
    public boolean hasNextPhase() {
        return phaseIndex < getTotalNumberOfPhases();
    }

    @Override
    public LevelPhase getNextPhase() {
        if (!hasNextPhase()) {
            throw new NoSuchElementException("No more phases available in this level.");
        }

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