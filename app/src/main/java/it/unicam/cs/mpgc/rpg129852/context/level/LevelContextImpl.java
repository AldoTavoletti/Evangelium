package it.unicam.cs.mpgc.rpg129852.context.level;

import it.unicam.cs.mpgc.rpg129852.model.level.Level;

public class LevelContextImpl implements LevelSessionManager {

    private Level level;

    public void setCurrentLevel(Level level) {
        this.level = level;
    }

    @Override
    public void clearSession() {
        this.level = null;
    }

    @Override
    public Level getCurrentLevel() {
        return level;
    }

    @Override
    public boolean hasActiveLevel() {
        return level != null;
    }
}
