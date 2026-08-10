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

public class ScenarioLoaderImpl implements ScenarioLoader {

    private final Gson gson;

    public ScenarioLoaderImpl(Gson gson) {
        this.gson = gson;
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
            throw new IllegalArgumentException("Il path dello scenario non può essere nullo o vuoto.");
        }
    }

    private LevelScenario parseScenarioFile(String scenarioPath) {
        try (InputStream inputStream = getClass().getResourceAsStream(scenarioPath)) {
            if (inputStream == null) {
                throw new IllegalStateException("File di scenario non trovato al percorso specificato: " + scenarioPath);
            }

            try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                LevelScenario scenario = gson.fromJson(reader, LevelScenario.class);

                if (scenario == null) {
                    throw new IllegalStateException("Il file JSON dello scenario è vuoto o malformato: " + scenarioPath);
                }

                return scenario;
            }
        } catch (IOException | JsonSyntaxException e) {
            throw new RuntimeException("Errore critico durante il caricamento dello scenario: " + scenarioPath, e);
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