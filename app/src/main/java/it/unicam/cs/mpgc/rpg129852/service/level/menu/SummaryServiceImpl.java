package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;

public class SummaryServiceImpl implements SummaryService {

    private final GameProvider gameProvider;
    private final LevelCatalog levelCatalog;
    private boolean summaryShown = false;

    public SummaryServiceImpl (GameProvider gameProvider, LevelCatalog levelCatalog) {
        this.gameProvider = gameProvider;
        this.levelCatalog = levelCatalog;
    }

    public boolean shouldSummaryBeShown() {
        if (summaryShown) {
            return false;
        }

        return areAllLevelsWon();

    }

    public boolean areAllLevelsWon() {
        int totalNumberOfLevels = levelCatalog.getTotalNumberOfLevels();

        int numberOfWonLevels = (int) gameProvider.getCurrentGame().gameState().getAllLevelScores().values().stream()
                .filter(score -> score != null && !score.virtues().isZero())
                .count();

        if (numberOfWonLevels == totalNumberOfLevels) {
            summaryShown = true;
            return true;
        }

        return false;
    }
}
