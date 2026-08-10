package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;

/**
 * Defines the contract for navigating through the sequential phases of a level.
 * It acts as an iterator-like mechanism to manage the progression of the gameplay.
 */
public interface LevelEngine {

    /**
     * Checks if there are any remaining phases to be played in the current level.
     *
     * @return true if there is at least one more phase, false otherwise
     */
    boolean hasNextPhase();

    /**
     * Retrieves the next available phase and advances the internal progression cursor.
     *
     * @return the next {@link LevelPhase}
     * @throws java.util.NoSuchElementException if there are no more phases available
     */
    LevelPhase getNextPhase();

    /**
     * Retrieves the current position of the phase cursor.
     *
     * @return the current phase index
     */
    int getCurrentPhaseNumber();

    /**
     * Retrieves the total number of phases defined for this level.
     *
     * @return the total count of phases
     */
    int getTotalNumberOfPhases();
}