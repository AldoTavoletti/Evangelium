package it.unicam.cs.mpgc.rpg129852.persistence.level;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.unicam.cs.mpgc.rpg129852.dto.level.DiscipleResponse;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * Concrete implementation of the {@link ScenarioLoader}.
 * It parses JSON files from the application's classpath using Gson and filters the
 * dialogue options within each phase to ensure the player only sees responses available
 * to their current job classification.
 */
public class ScenarioLoaderImpl implements ScenarioLoader {

    private final Gson gson;

    /**
     * Constructs a new scenario loader.
     *
     * @param gson the configured Gson instance used for JSON deserialization
     * @throws NullPointerException if the Gson instance is null
     */
    public ScenarioLoaderImpl(Gson gson) {
        this.gson = Objects.requireNonNull(gson, "The Gson instance must not be null.");
    }

    @Override
    public LevelScenario loadScenario(String scenarioPath, Job discipleJob) {
        validatePath(scenarioPath);

        LevelScenario rawScenario = parseScenarioFile(scenarioPath);

        if (discipleJob == null) {
            return rawScenario;
        }

        return filterScenarioByJob(rawScenario, discipleJob);
    }

    private void validatePath(String scenarioPath) {
        if (scenarioPath == null || scenarioPath.isBlank()) {
            throw new IllegalArgumentException("The scenario path cannot be null or blank.");
        }
    }

    private LevelScenario parseScenarioFile(String scenarioPath) {
        try (InputStream inputStream = getClass().getResourceAsStream(scenarioPath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Scenario file not found at the specified path: " + scenarioPath);
            }

            try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                LevelScenario scenario = gson.fromJson(reader, LevelScenario.class);

                if (scenario == null) {
                    throw new IllegalStateException("The JSON scenario file is empty or malformed: " + scenarioPath);
                }

                return scenario;
            }
        } catch (IOException | JsonSyntaxException e) {
            throw new RuntimeException("Critical error while loading the scenario: " + scenarioPath, e);
        }
    }

    private LevelScenario filterScenarioByJob(LevelScenario scenario, Job discipleJob) {
        LevelPhase[] filteredPhases = Arrays.stream(scenario.phases())
                .map(phase -> filterPhase(phase, discipleJob))
                .toArray(LevelPhase[]::new);

        return new LevelScenario(scenario.npc(), filteredPhases);
    }

    private LevelPhase filterPhase(LevelPhase phase, Job discipleJob) {
        DiscipleResponse[] filteredResponses = Arrays.stream(phase.responses())
                .filter(response -> isResponseAvailableForJob(response, discipleJob))
                .toArray(DiscipleResponse[]::new);

        return new LevelPhase(phase.npcDialogue(), filteredResponses);
    }

    private boolean isResponseAvailableForJob(DiscipleResponse response, Job discipleJob) {
        return response.requiredJob() == Job.NONE || response.requiredJob() == discipleJob;
    }
}