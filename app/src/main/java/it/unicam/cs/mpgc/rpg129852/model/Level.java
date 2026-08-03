package it.unicam.cs.mpgc.rpg129852.model;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.LevelScenario;

public class Level {

    private LevelMetadata metadata;
    private LevelScenario scenario;

    public Level(LevelMetadata metadata, LevelScenario scenario) {
        this.metadata = metadata;
        this.scenario = scenario;
    }

    public LevelMetadata getMetadata() {
        return metadata;
    }

    public LevelScenario getScenario() {
        return scenario;
    }

}
