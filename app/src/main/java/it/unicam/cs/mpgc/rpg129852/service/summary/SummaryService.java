package it.unicam.cs.mpgc.rpg129852.service.summary;

/**
 * Defines the contract for tracking the overall completion status of the game.
 * It provides methods to verify if the player has won all available levels
 * and manages the state of the summary presentation.
 */
public interface SummaryService {

    /**
     * Checks if the player has successfully won all the levels in the game.
     * A level is considered won if the player has obtained a score greater than zero.
     *
     * @return true if all levels are won, false otherwise
     */
    boolean areAllLevelsWon();

    /**
     * Sets a flag indicating whether the final summary screen has been shown to the player.
     *
     * @param summaryShown true if the summary has been shown, false otherwise
     */
    void setSummaryShown(boolean summaryShown);
}