package it.unicam.cs.mpgc.rpg129852.service.level;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;

import java.util.List;

public interface LevelCatalog {
    public List<LevelMetadata> getLevelsByCategory(LevelCategory category);
}
