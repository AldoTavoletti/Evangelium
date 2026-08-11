package it.unicam.cs.mpgc.rpg129852.context.level;

import it.unicam.cs.mpgc.rpg129852.model.level.Level;

import java.util.Objects;

/**
 * Concrete implementation of the {@link LevelSessionManager}.
 * It holds the runtime state of the active level in memory during a gameplay session.
 */
public class LevelContextImpl implements LevelSessionManager {

    private Level level;

    @Override
    public void setCurrentLevel(Level level) {
        this.level = Objects.requireNonNull(level, "The level to set must not be null.");
    }

    @Override
    public Level getCurrentLevel() {
        if (this.level == null) {
            throw new IllegalStateException("No active level in the context.");
        }
        return this.level;
    }
}