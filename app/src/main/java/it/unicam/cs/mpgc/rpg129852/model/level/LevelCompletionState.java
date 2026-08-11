package it.unicam.cs.mpgc.rpg129852.model.level;

/**
 * Represents the various states of completion for a level attempt,
 * used to determine the quality of the player's performance.
 */
public enum LevelCompletionState {

    /**
     * The level has not been attempted or no score has been recorded yet.
     */
    NONE,

    /**
     * The level was attempted, but the player failed to meet the minimum required healing threshold.
     */
    FAILED,

    /**
     * The level was completed successfully, but the player made some mistakes (e.g., negative answers).
     */
    GOOD,

    /**
     * The level was completed flawlessly, granting the maximum possible rewards.
     */
    PERFECT
}