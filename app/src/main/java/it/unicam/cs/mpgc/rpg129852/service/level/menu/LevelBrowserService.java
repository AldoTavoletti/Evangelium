package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;

import java.util.List;
import java.util.Optional;

/**
 * It defines the contract for browsing the levels in the player menu.
 */
public interface LevelBrowserService {

    /**
     * Retrieve the levels' metadata filtering them by category.
     *
     * @param category the category to filter the levels' metadata.
     * @return a list of {@link LevelMetadata} for the specified category, or an empty list if no such level exists.
     */
    List<LevelMetadata> getLevelsMetadataByCategory(LevelCategory category);

    /**
     * Retrieve the books required to play the specified level
     * @param levelMetadata the level metadata for which the required book names are needed
     * @return a list of book names, or an empty list if no books are required for that level
     */
    List<String> getRequiredBookNames(LevelMetadata levelMetadata);

    /**
     * Retrieve the score saved for the specified level, or null if no saved score is there.
     * @param levelId the id of the level
     * @return an {@link Optional} for the score
     */
    Optional<Score> getScoreForLevel(String levelId);

    /**
     * Checks if the user has all the required books to play the specified levels.
     * @param levelMetadata the metadata of the level
     * @return {@code true} if the user has all the required books, {@code false} otherwise
     */
    boolean isLevelUnlocked(LevelMetadata levelMetadata);
}