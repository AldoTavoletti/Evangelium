package it.unicam.cs.mpgc.rpg129852.service.summary;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.LevelCatalog;

import java.util.Objects;

/**
 * Concrete implementation of the {@link SummaryService}.
 * It evaluates the player's progress by comparing the total number of levels
 * against the recorded scores in the current game state.
 */
public class SummaryServiceImpl implements SummaryService {

    private final GameProvider gameProvider;
    private final LevelCatalog levelCatalog;

    private boolean summaryShown = false;

    /**
     * Constructs a new summary service with the required dependencies.
     *
     * @param gameProvider the provider granting access to the player's current game session
     * @param levelCatalog the catalog containing the static metadata of all game levels
     * @throws NullPointerException if any of the dependencies are null
     */
    public SummaryServiceImpl(GameProvider gameProvider, LevelCatalog levelCatalog) {
        this.gameProvider = Objects.requireNonNull(gameProvider, "The game provider must not be null.");
        this.levelCatalog = Objects.requireNonNull(levelCatalog, "The level catalog must not be null.");
    }

    @Override
    public boolean areAllLevelsWon() {
        int totalNumberOfLevels = levelCatalog.getTotalNumberOfLevels();

        int numberOfWonLevels = (int) gameProvider.getCurrentGame().gameState().getAllLevelScores().values().stream()
                .filter(this::isLevelWon)
                .count();

        return numberOfWonLevels == totalNumberOfLevels;
    }

    @Override
    public void setSummaryShown(boolean summaryShown) {
        this.summaryShown = summaryShown;
    }

    private boolean isLevelWon(Score score) {
        return score != null && !score.virtues().isZero();
    }
}