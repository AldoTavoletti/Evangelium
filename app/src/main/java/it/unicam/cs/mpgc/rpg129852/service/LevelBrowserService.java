package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

import java.util.List;
import java.util.Optional;

public interface LevelBrowserService {
    List<LevelMetadata> getLevelsByCategory(LevelCategory category);
    Optional<Virtues> getScoreForLevel(String levelId);
    boolean isLevelUnlocked(LevelMetadata level);
    List<String> getRequiredBookNames(LevelMetadata level);
}