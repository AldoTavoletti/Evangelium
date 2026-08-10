package it.unicam.cs.mpgc.rpg129852.dto.level;

import it.unicam.cs.mpgc.rpg129852.model.level.ProblemType;

public record Npc(
        String name,
        String imagePath,
        ProblemType problemType,
        double maxProblemValue
) {}
