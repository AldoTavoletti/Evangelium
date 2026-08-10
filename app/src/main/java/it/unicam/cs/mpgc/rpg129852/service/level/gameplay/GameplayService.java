package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.level.PhaseAnswer;
import it.unicam.cs.mpgc.rpg129852.model.level.ProblemType;

/**
 * Defines the contract for managing the core gameplay loop of a single level.
 * It tracks the player's progress, handles answer submissions, and orchestrates
 * the transition between different dialogue phases.
 */
public interface GameplayService {

    /**
     * Submits the player's answer for the current phase, updating the internal progress.
     *
     * @param answer the answer chosen by the player
     */
    void submitAnswer(PhaseAnswer answer);

    /**
     * Checks if the level's win condition has been met (e.g., the problem value reached zero).
     *
     * @return true if the level is won, false otherwise
     */
    boolean isLevelWon();

    /**
     * Finalizes the level, calculates the final score based on the submitted answers,
     * and persists the results.
     */
    void completeLevel();

    /**
     * Checks if there is another phase available in the current level sequence.
     *
     * @return true if a next phase exists, false if the level sequence is over
     */
    boolean hasNextPhase();

    /**
     * Retrieves the next dialogue phase in the sequence.
     *
     * @return the next {@link LevelPhase}
     */
    LevelPhase getNextPhase();

    /**
     * Retrieves the index or logical number of the currently active phase.
     *
     * @return the current phase number
     */
    int getCurrentPhaseNumber();

    /**
     * Retrieves the total count of phases configured for the current level.
     *
     * @return the total number of phases
     */
    int getTotalNumberOfPhases();

    /**
     * Retrieves the current remaining value (or "health") of the NPC's problem.
     *
     * @return the current problem value
     */
    double getCurrentProblemValue();

    /**
     * Retrieves the initial, maximum value of the NPC's problem before any healing.
     *
     * @return the maximum problem value
     */
    double getMaxProblemValue();

    /**
     * Retrieves the type of problem the NPC is facing.
     *
     * @return the {@link ProblemType}
     */
    ProblemType getProblemType();

    /**
     * Retrieves the resource path for the NPC's visual representation.
     *
     * @return the image path as a string
     */
    String getNpcImagePath();

    /**
     * Retrieves the display name of the NPC involved in the current level.
     *
     * @return the NPC's name
     */
    String getNpcName();
}