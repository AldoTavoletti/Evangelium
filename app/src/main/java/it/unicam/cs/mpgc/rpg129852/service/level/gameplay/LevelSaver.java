package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.model.level.Score;

public interface LevelSaver {
    void save(String levelId, Score score);
}
