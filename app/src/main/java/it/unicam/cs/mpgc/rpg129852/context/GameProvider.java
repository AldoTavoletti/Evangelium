package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.Game;

/**
 * Provides read-only access to the currently active game session.
 *
 * This interface adheres to the Interface Segregation Principle, allowing
 * components (such as UI controllers or gameplay mechanics) to query the
 * current game state without having the authority to modify or clear
 * the session itself.
 */
public interface GameProvider {

    /**
     * Retrieves the currently active game instance.
     *
     * It is highly recommended to verify the presence of an active game
     * via {@link #hasActiveGame()} before calling this method to avoid
     * unexpected behaviors or exceptions.
     *
     * @return the current {@link Game} instance.
     */
    Game getCurrentGame();

    /**
     * Checks whether a game session is currently active and loaded in memory.
     *
     * @return true if a game is currently active, false otherwise.
     */
    boolean hasActiveGame();
}