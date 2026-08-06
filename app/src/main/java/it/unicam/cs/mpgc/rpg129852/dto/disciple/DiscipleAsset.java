package it.unicam.cs.mpgc.rpg129852.dto.disciple;

import it.unicam.cs.mpgc.rpg129852.dto.Resource;

public record DiscipleAsset(
        String id,
        String gifPath,
        String imagePath
) implements Resource {}