package it.unicam.cs.mpgc.rpg129852.context.level;

import it.unicam.cs.mpgc.rpg129852.model.level.Level;

/**
 * Provides read-only access to the currently active level.
 */
public interface LevelProvider {

    /**
     * Retrieves the current level.
     *
     * @return the currently active level
     * @throws IllegalStateException if no level is currently active
     */
    Level getCurrentLevel();
}