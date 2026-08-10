package it.unicam.cs.mpgc.rpg129852.service.summary;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.LevelCatalog;

import java.util.List;
import java.util.Map;

public class StatsServiceImpl implements StatsService {

    private final GameProvider gameProvider;
    private final LevelCatalog levelCatalog;

    public StatsServiceImpl(GameProvider gameProvider, LevelCatalog levelCatalog) {
        this.gameProvider = gameProvider;
        this.levelCatalog = levelCatalog;
    }

    public int getNumberOfPerfectLevels() {
        List<LevelMetadata> allLevels = levelCatalog.getAllLevelsMetadata();
        Map<String, Score> scoresMap = gameProvider.getCurrentGame().gameState().getAllLevelScores();

        return (int) allLevels.stream()
                .filter(level -> {
                    Score score = scoresMap.get(level.id());
                    return score != null && score.virtues().equals(level.maxRewards());
                })
                .count();
    }

    public int getNumberOfAttempts() {
        return gameProvider.getCurrentGame().gameState().getNumTotalAttempts();
    }

}
