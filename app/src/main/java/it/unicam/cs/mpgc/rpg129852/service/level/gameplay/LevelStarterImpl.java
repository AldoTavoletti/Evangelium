package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.context.level.LevelSessionManager;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;
import it.unicam.cs.mpgc.rpg129852.model.level.Level;
import it.unicam.cs.mpgc.rpg129852.persistence.level.ScenarioLoader;

import java.util.Objects;

/**
 * Concrete implementation of the {@link LevelStarter}.
 * It orchestrates the loading of a level's scenario via the {@link ScenarioLoader}
 * and sets the newly constructed level into the {@link LevelSessionManager}.
 */
public class LevelStarterImpl implements LevelStarter {

    private final LevelSessionManager levelContext;
    private final ScenarioLoader scenarioLoader;

    /**
     * Constructs a new level starter.
     *
     * @param levelContext   the session manager holding the currently active level state
     * @param scenarioLoader the loader responsible for fetching scenario and phase data
     * @throws NullPointerException if any of the dependencies are null
     */
    public LevelStarterImpl(LevelSessionManager levelContext, ScenarioLoader scenarioLoader) {
        this.levelContext = Objects.requireNonNull(levelContext, "The level context must not be null.");
        this.scenarioLoader = Objects.requireNonNull(scenarioLoader, "The scenario loader must not be null.");
    }

    @Override
    public void start(LevelMetadata metadata, Job discipleJob) {
        Objects.requireNonNull(metadata, "The level metadata must not be null.");
        Objects.requireNonNull(discipleJob, "The disciple job must not be null.");

        LevelScenario scenario = scenarioLoader.loadScenario(metadata.scenarioPath(), discipleJob);
        Level level = new Level(metadata, scenario);

        levelContext.setCurrentLevel(level);
    }
}