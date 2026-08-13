package it.unicam.cs.mpgc.rpg129852.service.summary;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCompletionState;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.LevelCatalog;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Concrete implementation of the {@link StatsService}.
 * It aggregates data from the current game state and the level catalog to compute statistics.
 */
public class StatsServiceImpl implements StatsService {

    private final GameProvider gameProvider;
    private final LevelCatalog levelCatalog;

    /**
     * Constructs a new stats service with the required dependencies.
     *
     * @param gameProvider the provider granting access to the player's current game session
     * @param levelCatalog the catalog containing the static metadata of all game levels
     * @throws NullPointerException if any of the dependencies are null
     */
    public StatsServiceImpl(GameProvider gameProvider, LevelCatalog levelCatalog) {
        this.gameProvider = Objects.requireNonNull(gameProvider, "The game provider must not be null.");
        this.levelCatalog = Objects.requireNonNull(levelCatalog, "The level catalog must not be null.");
    }

    @Override
    public int getNumberOfPerfectLevels() {
        List<LevelMetadata> allLevels = levelCatalog.getAllLevelsMetadata();
        Map<String, Score> scoresMap = gameProvider.getCurrentGame().gameState().getAllLevelScores();

        return (int) allLevels.stream()
                .filter(level -> {
                    Score score = scoresMap.get(level.id());
                    return isScorePerfect(score);
                })
                .count();
    }

    @Override
    public int getNumberOfAttempts() {
        return gameProvider.getCurrentGame().gameState().getNumTotalAttempts();
    }

    private boolean isScorePerfect(Score score) {
        return score != null && score.completionState().equals(LevelCompletionState.PERFECT);
    }
}