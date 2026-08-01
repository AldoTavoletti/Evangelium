package it.unicam.cs.mpgc.rpg129852.dto;

import it.unicam.cs.mpgc.rpg129852.persistence.Resource;

public record ScriptureResource(
   String id,
   String text
) implements Resource {}
