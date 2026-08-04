package it.unicam.cs.mpgc.rpg129852.dto;

import it.unicam.cs.mpgc.rpg129852.model.LevelCategory;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.Resource;

import java.awt.print.Book;
import java.util.List;

public record LevelMetadata(
        String id,
        LevelCategory category,
        String title,
        String description,
        Virtues maxRewards,
        List<String> requiredBooksIds,
        String scenarioPath
) implements Resource {}

