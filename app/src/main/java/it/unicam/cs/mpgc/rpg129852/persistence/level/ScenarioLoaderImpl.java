package it.unicam.cs.mpgc.rpg129852.persistence.level;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.rpg129852.dto.level.DiscipleResponse;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioLoaderImpl implements ScenarioLoader {

    private final Gson gson;

    public ScenarioLoaderImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public LevelScenario loadScenario(String scenarioPath, Job discipleJob) {
        if (scenarioPath == null || scenarioPath.isBlank()) {
            throw new IllegalArgumentException("Il path dello scenario non può essere nullo o vuoto.");
        }

        try (InputStream inputStream = getClass().getResourceAsStream(scenarioPath)) {

            if (inputStream == null) {
                throw new IllegalStateException("File di scenario non trovato al percorso specificato: " + scenarioPath);
            }

            try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                LevelScenario scenario = gson.fromJson(reader, LevelScenario.class);

                if (scenario == null) {
                    throw new IllegalStateException("Il file JSON dello scenario è vuoto o malformato: " + scenarioPath);
                }

                if (discipleJob == null) {
                    return scenario;
                }

                LevelPhase[] filteredPhases = Arrays.stream(scenario.phases())
                        .map(phase -> {
                            DiscipleResponse[] filteredResponses = Arrays.stream(phase.responses())
                                    .filter(r -> r.requiredJob() == Job.NONE || r.requiredJob() == discipleJob)
                                    .toArray(DiscipleResponse[]::new);

                            return new LevelPhase(phase.npcDialogue(), filteredResponses);
                        })
                        .toArray(LevelPhase[]::new);

                return new LevelScenario(scenario.npc(), filteredPhases);
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore critico durante il caricamento dello scenario: " + scenarioPath, e);
        }
    }
}