package it.unicam.cs.mpgc.rpg129852.model.game;

import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Inventory;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameState {

    private DiscipleData discipleData;
    private final Inventory inventory;
    private final Map<String, Virtues> levelScores;

    public GameState(DiscipleData discipleData, Inventory inventory) {
        this.discipleData = discipleData;
        this.inventory = inventory;
        this.levelScores = new HashMap<>();
    }

    public GameState(DiscipleData discipleData, Map<String, Virtues> savedScores, Inventory inventory) {
        this.discipleData = discipleData;
        this.levelScores = new HashMap<>(savedScores);
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void recordLevelScore(String levelId, Virtues score) {
        validateInputs(levelId, score);

        Optional<Virtues> previousScore = getScoreForLevel(levelId);

        if (previousScore.isPresent() && score.isLessThanOrEqualTo(previousScore.get())) {
            return;
        }

        previousScore.ifPresent(discipleData::subtractVirtues);

        levelScores.put(levelId, score);
        discipleData.addVirtues(score);
    }

    private void validateInputs(String levelId, Virtues score) {
        if (levelId == null || levelId.isBlank()) {
            throw new IllegalArgumentException("L'ID del livello non può essere nullo o vuoto.");
        }
        if (score == null) {
            throw new IllegalArgumentException("Lo score non può essere nullo.");
        }
    }

    public Optional<Virtues> getScoreForLevel(String levelId) {
        return Optional.ofNullable(levelScores.get(levelId));
    }

    public boolean hasCompletedLevel(String levelId) {
        return levelScores.containsKey(levelId);
    }

    public DiscipleData getDiscipleData() {
        return discipleData;
    }

}