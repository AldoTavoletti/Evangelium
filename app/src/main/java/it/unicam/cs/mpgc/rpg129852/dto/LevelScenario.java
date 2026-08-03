package it.unicam.cs.mpgc.rpg129852.dto;

import java.util.List;

public record LevelScenario(
        Npc npc,
        LevelPhase[] phases
) {}