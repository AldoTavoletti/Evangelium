package it.unicam.cs.mpgc.rpg129852.persistence;

import com.google.gson.Gson;
import it.unicam.cs.mpgc.rpg129852.dto.LevelScenario;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public class ScenarioLoaderImpl implements ScenarioLoader {

    private final Gson gson;

    public ScenarioLoaderImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public LevelScenario loadScenario(String scenarioPath) {
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

                return scenario;
            }

        } catch (Exception e) {
            throw new RuntimeException("Errore critico durante il caricamento dello scenario: " + scenarioPath, e);
        }
    }
}