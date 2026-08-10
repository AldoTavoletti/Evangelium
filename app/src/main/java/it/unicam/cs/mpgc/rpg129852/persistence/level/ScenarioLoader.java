package it.unicam.cs.mpgc.rpg129852.persistence.level;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

public interface ScenarioLoader {
    LevelScenario loadScenario(String scenarioPath, Job discipleJob);
}