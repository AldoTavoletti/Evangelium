package it.unicam.cs.mpgc.rpg129852.dto;

import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.VirtueRewards;
import it.unicam.cs.mpgc.rpg129852.persistence.Resource;

public record LevelMetadata(
        String id,
        LevelCategory category,
        String title,
        String description,
        VirtueRewards maxRewards,
        String scenarioPath
) implements Resource {}

