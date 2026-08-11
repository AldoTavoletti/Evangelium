package it.unicam.cs.mpgc.rpg129852.service.summary;

/**
 * Defines the contract for retrieving game statistics and player performance metrics.
 * It provides read-only data useful for summary screens or achievements.
 */
public interface StatsService {

    /**
     * Calculates the total number of levels the player has completed with a perfect score.
     * A score is considered perfect if the obtained virtues match the maximum possible rewards for that level.
     *
     * @return the count of perfectly completed levels
     */
    int getNumberOfPerfectLevels();

    /**
     * Retrieves the total number of attempts the player has made across all levels.
     *
     * @return the total number of attempts
     */
    int getNumberOfAttempts();
}