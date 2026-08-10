package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.model.level.Score;

/**
 * Defines the contract for persisting the outcome and score of a completed level.
 * It ensures that the player's progress is safely recorded in the game state.
 */
public interface LevelSaver {

    /**
     * Saves the score achieved by the player for a specific level.
     *
     * @param levelId the unique identifier of the completed level
     * @param score   the final score and completion state achieved by the player
     * @throws IllegalArgumentException if the levelId is null or blank
     * @throws NullPointerException if the score is null
     */
    void save(String levelId, Score score);
}