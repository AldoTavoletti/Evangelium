package it.unicam.cs.mpgc.rpg129852.model;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.LevelScenario;

public class LevelDataImpl implements LevelData {

    private LevelMetadata metadata;
    private LevelScenario scenario;

    public LevelDataImpl(LevelMetadata metadata, LevelScenario scenario) {
        this.metadata = metadata;
        this.scenario = scenario;
    }

    public LevelMetadata getMetadata() {
        return metadata;
    }

    @Override
    public LevelScenario getScenario() {
        return null;
    }
}
