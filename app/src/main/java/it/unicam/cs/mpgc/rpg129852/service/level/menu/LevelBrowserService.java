package it.unicam.cs.mpgc.rpg129852.service.level.menu;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.List;
import java.util.Optional;

public interface LevelBrowserService {

    List<LevelMetadata> getLevelsByCategory(LevelCategory category);
    List<String> getRequiredBookNames(LevelMetadata level);
    boolean isLevelUnlocked(LevelMetadata level);
    Optional<Virtues> getScoreForLevel(String levelId);

}