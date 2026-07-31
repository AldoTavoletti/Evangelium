package it.unicam.cs.mpgc.rpg129852.model;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.LevelScenario;

public interface LevelData {
    LevelMetadata getMetadata();
    LevelScenario getScenario();
}