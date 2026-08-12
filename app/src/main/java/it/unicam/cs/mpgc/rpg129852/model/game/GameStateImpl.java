package it.unicam.cs.mpgc.rpg129852.model.game;

import it.unicam.cs.mpgc.rpg129852.model.disciple.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Inventory;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Concrete implementation of the {@link GameState}.
 * It tracks the player's progression, handling the logic to update total virtues
 * safely whenever a level score is improved.
 */
public class GameStateImpl implements GameState {

    private final DiscipleData discipleData;
    private final Inventory inventory;
    private final Map<String, Score> levelScores;
    private int numTotalAttempts;

    /**
     * Constructs a new game state with the provided disciple data and inventory.
     *
     * @param discipleData the core stats and information of the player's character
     * @param inventory    the collection of items owned by the player
     * @throws NullPointerException if the disciple data or inventory is null
     */
    public GameStateImpl(DiscipleData discipleData, Inventory inventory) {
        this.discipleData = Objects.requireNonNull(discipleData, "The disciple data must not be null.");
        this.inventory = Objects.requireNonNull(inventory, "The inventory must not be null.");
        this.levelScores = new HashMap<>();
        this.numTotalAttempts = 0;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
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
            throw new IllegalArgumentException("The level ID must not be null or empty.");
        }
        Objects.requireNonNull(score, "The score must not be null.");
    }

    @Override
    public Optional<Score> getScoreForLevel(String levelId) {
        return Optional.ofNullable(levelScores.get(levelId));
    }

    @Override
    public DiscipleData getDiscipleData() {
        return discipleData;
    }

    @Override
    public int getNumTotalAttempts() {
        return numTotalAttempts;
    }

    @Override
    public Map<String, Score> getAllLevelScores() {
        return Collections.unmodifiableMap(levelScores);
    }

}