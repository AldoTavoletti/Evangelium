package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.persistence.ResourceRegistry;

import java.util.List;

public class LevelCatalogImpl implements LevelCatalog {

    private final ResourceRegistry<LevelMetadata> registry;

    public LevelCatalogImpl(ResourceRegistry<LevelMetadata> registry) {
        this.registry = registry;
    }

    public List<LevelMetadata> getLevelsByCategory(LevelCategory category) {
        return registry.getAllResources().stream()
                .filter(level -> level.category() == category)
                .toList();
    }
}