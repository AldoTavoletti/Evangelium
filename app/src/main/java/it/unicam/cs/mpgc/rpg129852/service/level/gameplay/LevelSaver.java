package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

public interface LevelSaver {
    void save(String levelId, Virtues obtainedVirtues);
}
