package it.unicam.cs.mpgc.rpg129852.dto;

import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.VirtueRewards;

public record LevelMetadata(
        String id,
        LevelCategory category,
        String title,
        String description,
        VirtueRewards maxRewards,
        String levelScenarioPath
) implements Resource {}

