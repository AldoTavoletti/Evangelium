package it.unicam.cs.mpgc.rpg129852.persistence.level;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

/**
 * Defines the contract for loading a level's scenario data from a persistent source.
 * It handles the retrieval of dialogue phases and dynamically filters the available choices
 * based on the player's current job.
 */
public interface ScenarioLoader {

    /**
     * Loads a level scenario from the specified path and filters the available responses
     * according to the disciple's job.
     *
     * @param scenarioPath the resource path where the scenario data is located
     * @param discipleJob  the player's current job used to unlock or filter specific dialogue options (can be null if no job is assigned)
     * @return the fully loaded and filtered {@link LevelScenario}
     * @throws IllegalArgumentException if the scenario path is null or blank
     * @throws IllegalStateException    if the scenario file cannot be found or is malformed
     * @throws RuntimeException         if a critical I/O error occurs during loading
     */
    LevelScenario loadScenario(String scenarioPath, Job discipleJob);
}