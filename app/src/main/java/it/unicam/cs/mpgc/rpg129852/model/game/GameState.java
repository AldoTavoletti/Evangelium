package it.unicam.cs.mpgc.rpg129852.model.game;

import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Inventory;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Collection;
import java.util.Collections;

public class GameState {

    private DiscipleData discipleData;
    private final Inventory inventory;
    private final Map<String, Score> levelScores;
    private int numTotalAttempts;

    public GameState(DiscipleData discipleData, Inventory inventory) {
        this.discipleData = discipleData;
        this.inventory = inventory;
        this.levelScores = new HashMap<>();
        this.numTotalAttempts = 0;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void recordLevelScore(String levelId, Score score) {
        validateInputs(levelId, score);

        this.numTotalAttempts++;

        Optional<Score> previousScore = getScoreForLevel(levelId);

        if (previousScore.isPresent() && score.isLessThanOrEqualTo(previousScore.get())) {
            return;
        }

        previousScore.ifPresent(previousScoreVal -> discipleData.subtractVirtues(previousScoreVal.virtues()));

        levelScores.put(levelId, score);
        discipleData.addVirtues(score.virtues());
    }

    private void validateInputs(String levelId, Score score) {
        if (levelId == null || levelId.isBlank()) {
            throw new IllegalArgumentException("L'ID del livello non può essere nullo o vuoto.");
        }
        if (score == null) {
            throw new IllegalArgumentException("Lo score non può essere nullo.");
        }
    }

    public Optional<Score> getScoreForLevel(String levelId) {
        return Optional.ofNullable(levelScores.get(levelId));
    }

    public DiscipleData getDiscipleData() {
        return discipleData;
    }

    public int getNumTotalAttempts(){
        return numTotalAttempts;
    }

    public Map<String, Score> getAllLevelScores() {
        return Collections.unmodifiableMap(levelScores);
    }

    public int getNumberOfCompletedLevels() {
        return levelScores.size();
        // todo: voglio che restituisca solo il numero di livelli che ha uno score > 0
    }
}