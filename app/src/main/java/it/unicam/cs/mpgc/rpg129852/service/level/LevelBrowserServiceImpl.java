package it.unicam.cs.mpgc.rpg129852.service.level;

import it.unicam.cs.mpgc.rpg129852.context.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.service.BookCatalog;

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
    public Optional<Virtues> getScoreForLevel(String levelId) {
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