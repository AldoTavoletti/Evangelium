package it.unicam.cs.mpgc.rpg129852.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameState {

    private DiscipleData discipleData;
    private final Map<String, LevelScore> levelScores;

    public GameState(DiscipleData discipleData) {
        this.discipleData = discipleData;
        this.levelScores = new HashMap<>();
    }

    public GameState(DiscipleData discipleData, Map<String, LevelScore> savedScores) {
        this.discipleData = discipleData;
        this.levelScores = new HashMap<>(savedScores);
    }

    public DiscipleData getDiscipleData() {
        return discipleData;
    }

    public void recordLevelScore(String levelId, LevelScore score) {
        if (levelId == null || levelId.isBlank()) {
            throw new IllegalArgumentException("L'ID del livello non può essere nullo o vuoto.");
        }
        if (score == null) {
            throw new IllegalArgumentException("Lo score non può essere nullo.");
        }

        levelScores.put(levelId, score);
    }

    public Optional<LevelScore> getScoreForLevel(String levelId) {
        return Optional.ofNullable(levelScores.get(levelId));
    }

    public boolean hasCompletedLevel(String levelId) {
        return levelScores.containsKey(levelId);
    }

    public Map<String, LevelScore> getAllScores() {
        return Collections.unmodifiableMap(levelScores);
    }
}