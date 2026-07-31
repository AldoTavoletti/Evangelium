package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.service.LevelEngine;

public class LevelContextImpl implements LevelSessionManager {

    private LevelEngine engine;

    @Override
    public void setEngine(LevelEngine engine) {
        this.engine = engine;
    }

    public LevelEngine getEngine() {
        return engine;
    }
}
