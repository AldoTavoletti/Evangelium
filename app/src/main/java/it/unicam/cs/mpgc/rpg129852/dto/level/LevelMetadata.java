package it.unicam.cs.mpgc.rpg129852.dto.level;

import it.unicam.cs.mpgc.rpg129852.dto.Resource;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.level.LevelCompletionState;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

import java.util.List;
import java.util.Optional;

public record LevelMetadata(
        String id,
        LevelCategory category,
        String title,
        String description,
        Virtues maxRewards,
        List<String> requiredBookIds,
        String scenarioPath
) implements Resource {
}

