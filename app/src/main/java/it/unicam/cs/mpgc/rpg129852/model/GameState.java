package it.unicam.cs.mpgc.rpg129852.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameState {

    private DiscipleData discipleData;
    private final Map<String, Virtues> levelScores;

    public GameState(DiscipleData discipleData) {
        this.discipleData = discipleData;
        this.levelScores = new HashMap<>();
    }

    public GameState(DiscipleData discipleData, Map<String, Virtues> savedScores) {
        this.discipleData = discipleData;
        this.levelScores = new HashMap<>(savedScores);
    }

    public DiscipleData getDiscipleData() {
        return discipleData;
    }

    public void recordLevelScore(String levelId, Virtues score) {
        if (levelId == null || levelId.isBlank()) {
            throw new IllegalArgumentException("L'ID del livello non può essere nullo o vuoto.");
        }
        if (score == null) {
            throw new IllegalArgumentException("Lo score non può essere nullo.");
        }

        Virtues previousScore = levelScores.get(levelId);

        if (previousScore != null) {
            if (score.compareTo(previousScore) <= 0)
                return;
            discipleData.subtractVirtues(levelScores.get(levelId));
        }

        levelScores.put(levelId, score);
        discipleData.addVirtues(score);
    }

    public Optional<Virtues> getScoreForLevel(String levelId) {
        return Optional.ofNullable(levelScores.get(levelId));
    }

    public boolean hasCompletedLevel(String levelId) {
        return levelScores.containsKey(levelId);
    }

    public Map<String, Virtues> getAllScores() {
        return Collections.unmodifiableMap(levelScores);
    }
}