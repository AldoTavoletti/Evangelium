package it.unicam.cs.mpgc.rpg129852.service.game;

import it.unicam.cs.mpgc.rpg129852.service.NewGameRequest;

/**
 * Orchestrates the initialization phase of a new game.
 *
 * This service acts as a facade that bridges the domain creation logic,
 * the persistence layer, and the session context. It ensures that a newly
 * created game is properly instantiated, safely saved to the storage,
 * and immediately set as the active session.
 */
public interface GameStarter {

    /**
     * Creates, saves, and activates a new game session.
     *
     * @param request the data transfer object containing the initial player choices
     *                (e.g., disciple name, job, dto ID, and desired save name).
     * @throws IllegalStateException if a save file with the resolved name already exists,
     *                               preventing accidental data loss.
     * @throws IllegalArgumentException if the provided save name violates the syntax rules
     *                                  or contains forbidden characters.
     */
    void startNewGame(NewGameRequest request);

    /**
     * Creates, saves, and activates a new game session, deliberately overwriting
     * any pre-existing save data that shares the same resolved name.
     *
     * @param request the data transfer object containing the initial player choices.
     * @throws IllegalArgumentException if the provided save name violates the syntax rules
     *                                  or contains forbidden characters.
     */
    void overwriteAndStartNewGame(NewGameRequest request);
}