package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.DiscipleData;
import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

import java.util.List;
import java.util.Optional;

public interface PlayerMenuService {
    void validateSession();
    DiscipleData getCurrentDiscipleData();
    int getTotalVirtues();
    List<LevelMetadata> getLevelsByCategory(LevelCategory category);
    void startLevel(LevelMetadata level);
    Optional<Virtues> getScoreForLevel(String levelId);

    boolean hasRequiredBooks(List<String> requiredBookIds);

    String getFormattedRequiredBookNames(List<String> requiredBookIds);
}