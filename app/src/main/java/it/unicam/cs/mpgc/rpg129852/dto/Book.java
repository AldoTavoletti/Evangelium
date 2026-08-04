package it.unicam.cs.mpgc.rpg129852.dto;

import it.unicam.cs.mpgc.rpg129852.model.Virtues;
import it.unicam.cs.mpgc.rpg129852.persistence.Resource;

public record Book (
        String id,
        String displayName,
        Virtues price
) implements Resource {}