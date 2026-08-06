package it.unicam.cs.mpgc.rpg129852.context.game;

import it.unicam.cs.mpgc.rpg129852.model.game.Game;

/**
 * Manages the lifecycle and state mutation of the active game session.
 *
 * This interface extends {@link GameProvider} to provide write access,
 * allowing core services (like loaders or starters) to alter the global
 * session state by setting a new game or clearing the current one.
 */
public interface GameSessionManager extends GameProvider {

    /**
     * Sets the specified game as the currently active session.
     *
     * Any previously active game will be replaced in the current context.
     *
     * @param game the {@link Game} instance to activate. Must not be null.
     */
    void setCurrentGame(Game game);

    /**
     * Clears the current game session, releasing the active game from memory.
     *
     * After this method is executed, {@link #hasActiveGame()} will return false.
     */
    void clearSession();
}