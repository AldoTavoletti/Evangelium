package it.unicam.cs.mpgc.rpg129852.dto.level;

import it.unicam.cs.mpgc.rpg129852.dto.disciple.DiscipleResponse;

public record LevelPhase(
        String npcDialogue,
        DiscipleResponse[] responses
){
}
