package it.unicam.cs.mpgc.rpg129852.dto;

import it.unicam.cs.mpgc.rpg129852.persistence.Resource;

public record DiscipleAsset(
        String id,
        String gifPath,
        String imagePath
) implements Resource {}