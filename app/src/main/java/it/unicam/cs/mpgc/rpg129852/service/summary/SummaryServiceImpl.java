package it.unicam.cs.mpgc.rpg129852.service.summary;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.service.level.menu.LevelCatalog;

public class SummaryServiceImpl implements SummaryService {

    private final GameProvider gameProvider;
    private final LevelCatalog levelCatalog;

    private boolean summaryShown = false;

    public SummaryServiceImpl (GameProvider gameProvider, LevelCatalog levelCatalog) {
        this.gameProvider = gameProvider;
        this.levelCatalog = levelCatalog;
    }

    public boolean areAllLevelsWon() {
        int totalNumberOfLevels = levelCatalog.getTotalNumberOfLevels();

        int numberOfWonLevels = (int) gameProvider.getCurrentGame().gameState().getAllLevelScores().values().stream()
                .filter(score -> isLevelWon(score))
                .count();

        return numberOfWonLevels == totalNumberOfLevels;
    }

    public void setSummaryShown(boolean summaryShown) {
        this.summaryShown = summaryShown;
    }

    private boolean isLevelWon(Score score) {
        return score != null && !score.virtues().isZero();
    }
}
