package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;

import java.util.List;

public interface LevelCatalog {
    public List<LevelMetadata> getLevelsByCategory(LevelCategory category);
    public List<LevelMetadata> getAllLevels();
    public int getTotalNumberOfLevels();
}
