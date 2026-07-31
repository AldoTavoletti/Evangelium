package it.unicam.cs.mpgc.rpg129852.dto;

import it.unicam.cs.mpgc.rpg129852.model.ProblemType;

public record Npc(
        String name,
        String imagePath,
        ProblemType problemType,
        int maxProblemValue
) {}
