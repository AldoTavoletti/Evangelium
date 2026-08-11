package it.unicam.cs.mpgc.rpg129852.context.level;

import it.unicam.cs.mpgc.rpg129852.model.level.Level;

/**
 * Manages the lifecycle of the active level, allowing to set a new level
 * or modify the current gameplay context.
 */
public interface LevelSessionManager extends LevelProvider {

    /**
     * Sets the specified level as the currently active one.
     *
     * @param level the level to set
     * @throws NullPointerException if the provided level is null
     */
    void setCurrentLevel(Level level);
}