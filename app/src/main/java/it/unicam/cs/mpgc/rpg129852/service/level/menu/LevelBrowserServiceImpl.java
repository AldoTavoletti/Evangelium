package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.game.GameState;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalog;

import java.util.List;
import java.util.Optional;

public class LevelBrowserServiceImpl implements LevelBrowserService {

    private final LevelCatalog levelCatalog;
    private final BookCatalog bookCatalog;
    private final GameProvider gameProvider;

    public LevelBrowserServiceImpl(LevelCatalog levelCatalog,
                                   BookCatalog bookCatalog,
                                   GameProvider gameProvider) {
        this.levelCatalog = levelCatalog;
        this.bookCatalog = bookCatalog;
        this.gameProvider = gameProvider;
    }

    @Override
    public List<LevelMetadata> getLevelsByCategory(LevelCategory category) {
        return levelCatalog.getLevelsByCategory(category);
    }

    @Override
    public Optional<Score> getScoreForLevel(String levelId) {
        return gameProvider.getCurrentGame().gameState().getScoreForLevel(levelId);
    }

    @Override
    public boolean isLevelUnlocked(LevelMetadata level) {
        List<String> requiredBooks = level.requiredBookIds();

        if (requiredBooks == null || requiredBooks.isEmpty()) {
            return true;
        }

        return gameProvider.getCurrentGame().gameState().getInventory().contains(requiredBooks);
    }

    @Override
    public List<String> getRequiredBookNames(LevelMetadata level) {
        List<String> requiredBooks = level.requiredBookIds();

        if (requiredBooks == null || requiredBooks.isEmpty()) {
            return List.of();
        }

        return bookCatalog.getBookNamesFromIds(requiredBooks);
    }
}