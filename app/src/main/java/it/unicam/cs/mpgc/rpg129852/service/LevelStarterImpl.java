package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.model.LevelData;
import it.unicam.cs.mpgc.rpg129852.model.LevelDataImpl;
import it.unicam.cs.mpgc.rpg129852.context.LevelSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.LevelScenario;
import it.unicam.cs.mpgc.rpg129852.persistence.ScenarioLoader;

public class LevelStarterImpl implements  LevelStarter {

    private final LevelSessionManager levelContext;
    private final ScenarioLoader scenarioLoader;

    public LevelStarterImpl(LevelSessionManager levelContext, ScenarioLoader scenarioLoader){
        this.levelContext = levelContext;
        this.scenarioLoader = scenarioLoader;
    }

    @Override
    public void start(LevelMetadata metadata) {
        LevelScenario scenario = scenarioLoader.loadScenario(metadata.scenarioPath());
        LevelData levelData = new LevelDataImpl(metadata, scenario);
        LevelEngine levelEngine = new LevelEngineImpl(levelData);
        levelContext.setEngine(levelEngine);
    }
}
