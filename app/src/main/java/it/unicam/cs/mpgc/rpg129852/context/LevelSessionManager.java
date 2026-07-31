package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.service.LevelEngine;

public interface LevelSessionManager {
    void setEngine(LevelEngine engine);
    LevelEngine getEngine();
}
