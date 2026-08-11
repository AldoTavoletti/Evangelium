package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

import java.util.List;
import java.util.Objects;

/**
 * Concrete implementation of the {@link LevelCatalog} interface.
 * It uses a {@link ResourceRegistry} to access the underlying levels' metadata.
 */
public class LevelCatalogImpl implements LevelCatalog {

    private final ResourceRegistry<LevelMetadata> registry;

    /**
     * Constructs a new catalog using the specified resource registry.
     *
     * @param registry the data source containing the book entities
     * @throws NullPointerException if the provided registry is null
     */
    public LevelCatalogImpl(ResourceRegistry<LevelMetadata> registry) {
        this.registry = Objects.requireNonNull(registry,  "The resource registry must not be null.");
    }

    public List<LevelMetadata> getLevelsMetadataByCategory(LevelCategory category) {
        Objects.requireNonNull(category, "The category of the levels must not be null");

        return registry.getAllResources().stream()
                .filter(level -> level.category() == category)
                .toList();
    }

    public List<LevelMetadata> getAllLevelsMetadata() {
        return registry.getAllResources();
    }

    public int getTotalNumberOfLevels() {
        return registry.getAllResources().size();
    }
}