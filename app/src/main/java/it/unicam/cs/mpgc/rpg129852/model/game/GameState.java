package it.unicam.cs.mpgc.rpg129852.model.game;

import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Inventory;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;

import java.util.Map;
import java.util.Optional;

/**
 * Defines the contract for the current session's game state.
 * It provides operations to track the player's core character data, their inventory,
 * and the scores achieved across different levels.
 */
public interface GameState {

    /**
     * Retrieves the player's inventory.
     *
     * @return the inventory instance
     */
    Inventory getInventory();

    /**
     * Records a new score for a specific level.
     * If a previous score exists, the system should only overwrite it if the new score
     * yields equal or higher rewards, adjusting the player's total virtues accordingly.
     *
     * @param levelId the unique identifier of the level
     * @param score   the score achieved in the level attempt
     * @throws IllegalArgumentException if the levelId is null or blank
     * @throws NullPointerException     if the score is null
     */
    void recordLevelScore(String levelId, Score score);

    /**
     * Retrieves the highest recorded score for a specific level, if any.
     *
     * @param levelId the unique identifier of the level
     * @return an {@link Optional} containing the score, or empty if the level has not been completed
     */
    Optional<Score> getScoreForLevel(String levelId);

    /**
     * Retrieves the player's core character data.
     *
     * @return the disciple data instance
     */
    DiscipleData getDiscipleData();

    /**
     * Retrieves the total number of level attempts the player has made.
     *
     * @return the total number of attempts
     */
    int getNumTotalAttempts();

    /**
     * Retrieves an unmodifiable view of all recorded highest scores.
     *
     * @return a map pairing level IDs with their respective highest scores
     */
    Map<String, Score> getAllLevelScores();

    /**
     * Retrieves the total count of distinct levels the player has completed at least once.
     *
     * @return the number of played levels
     */
    int getNumberOfPlayedLevels();
}