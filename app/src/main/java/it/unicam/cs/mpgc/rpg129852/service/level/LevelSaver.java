package it.unicam.cs.mpgc.rpg129852.service.level;

import it.unicam.cs.mpgc.rpg129852.model.Virtues;

public interface LevelSaver {
    void save(String levelId, Virtues obtainedVirtues);
}
