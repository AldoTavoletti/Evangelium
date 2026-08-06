package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.DiscipleResponse;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;
import it.unicam.cs.mpgc.rpg129852.model.level.Level;
import it.unicam.cs.mpgc.rpg129852.context.level.LevelSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;
import it.unicam.cs.mpgc.rpg129852.persistence.level.ScenarioLoader;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LevelStarterImpl implements  LevelStarter {

    private final LevelSessionManager levelContext;
    private final ScenarioLoader scenarioLoader;

    public LevelStarterImpl(LevelSessionManager levelContext, ScenarioLoader scenarioLoader){
        this.levelContext = levelContext;
        this.scenarioLoader = scenarioLoader;
    }

    @Override
    public void start(LevelMetadata metadata, Job discipleJob) {
        LevelScenario scenario = scenarioLoader.loadScenario(metadata.scenarioPath(), discipleJob);

        Level level = new Level(metadata, scenario);
        levelContext.setCurrentLevel(level);
    }
}
