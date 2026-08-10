package it.unicam.cs.mpgc.rpg129852.model.level;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;

public record Level(LevelMetadata metadata, LevelScenario scenario) {
}