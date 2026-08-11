package it.unicam.cs.mpgc.rpg129852.dto.book;

import it.unicam.cs.mpgc.rpg129852.dto.Resource;
import it.unicam.cs.mpgc.rpg129852.model.virtues.Virtues;

public record Book(
        String id,
        String displayName,
        Virtues price
) implements Resource {
}