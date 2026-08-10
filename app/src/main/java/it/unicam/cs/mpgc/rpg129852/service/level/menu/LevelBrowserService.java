package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.level.Score;

import java.util.List;
import java.util.Optional;

public interface LevelBrowserService {
    List<LevelMetadata> getLevelsMetadataByCategory(LevelCategory category);
    List<String> getRequiredBookNames(LevelMetadata level);
    Optional<Score> getScoreForLevel(String levelId);
    boolean isLevelUnlocked(LevelMetadata level);
}