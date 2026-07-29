package it.unicam.cs.mpgc.rpg129852.asset;

public record LevelMetadata(
        String id,
        LevelCategory category,
        String title,
        String description,
        VirtueRewards maxRewards,
        String levelScenarioPath
) implements Resource {}

