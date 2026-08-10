package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.context.game.GameProvider;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;
import it.unicam.cs.mpgc.rpg129852.service.book.BookCatalog;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Concrete implementation of the {@link LevelBrowserService} interface.
 * It provides the data needed to browse the levels in the player menu.
 */
public class LevelBrowserServiceImpl implements LevelBrowserService {

    private final LevelCatalog levelCatalog;
    private final BookCatalog bookCatalog;
    private final GameProvider gameProvider;

    /**
     * Construct a level browser service.
     *
     * @param levelCatalog the catalog to retrieve the levels
     * @param bookCatalog the catalog to retrieve the books
     * @param gameProvider the game provider to retrieve the current context
     */
    public LevelBrowserServiceImpl(LevelCatalog levelCatalog,
                                   BookCatalog bookCatalog,
                                   GameProvider gameProvider) {
        this.levelCatalog = Objects.requireNonNull(levelCatalog, "The level catalog must not be null.");
        this.bookCatalog = Objects.requireNonNull(bookCatalog, "The book catalog must not be null.");
        this.gameProvider = Objects.requireNonNull(gameProvider, "The game provider must not be null.");
    }

    @Override
    public List<LevelMetadata> getLevelsMetadataByCategory(LevelCategory category) {
        return levelCatalog.getLevelsMetadataByCategory(category);
    }

    @Override
    public Optional<Score> getScoreForLevel(String levelId) {
        return gameProvider.getCurrentGame().gameState().getScoreForLevel(levelId);
    }

    @Override
    public boolean isLevelUnlocked(LevelMetadata levelMetadata) {
        Objects.requireNonNull(levelMetadata, "The provided level metadata must not be null.");

        List<String> requiredBooks = levelMetadata.requiredBookIds();

        if (requiredBooks == null || requiredBooks.isEmpty()) {
            return true;
        }

        return gameProvider.getCurrentGame().gameState().getInventory().contains(requiredBooks);
    }

    @Override
    public List<String> getRequiredBookNames(LevelMetadata levelMetadata) {
        Objects.requireNonNull(levelMetadata, "The provided level metadata must not be null.");

        List<String> requiredBooks = levelMetadata.requiredBookIds();

        if (requiredBooks == null || requiredBooks.isEmpty()) {
            return List.of();
        }

        return bookCatalog.getBookNamesFromIds(requiredBooks);
    }
}