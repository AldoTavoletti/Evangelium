package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;

import java.util.List;

/**
 * Defines the contract for accessing the catalog of levels' metadata.
 */
public interface LevelCatalog {

    /**
     * Retrieves the metadata of the levels of the specified category.
     *
     * @param category  the category of the levels' metadata to fetch
     * @return          a list of all the {@link LevelMetadata} records of the specified category
     * @throws NullPointerException if the provided category is null
     */
    List<LevelMetadata> getLevelsMetadataByCategory(LevelCategory category);

    /**
     * Retrieves the metadata of all the levels.
     *
     * @return  a list of all the {@link LevelMetadata}
     */
    List<LevelMetadata> getAllLevelsMetadata();

    /**
     * Retrieves the total number of levels in the catalog.
     *
     * @return  the total number of levels in the catalog
     */
    int getTotalNumberOfLevels();
}
