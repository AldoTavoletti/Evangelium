package it.unicam.cs.mpgc.rpg129852.model.level;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.dto.level.LevelScenario;

import java.util.Objects;

/**
 * Represents a fully loaded and playable level in the game.
 * It combines the static metadata (ID, rewards, category) with the dynamic scenario (NPC, dialogue phases).
 *
 * @param metadata the core information and configuration of the level
 * @param scenario the specific narrative and dialogue progression of the level
 */
public record Level(LevelMetadata metadata, LevelScenario scenario) {

    /**
     * Compact constructor to ensure the record is never instantiated with null values.
     *
     * @throws NullPointerException if the metadata or the scenario is null
     */
    public Level {
        Objects.requireNonNull(metadata, "The level metadata must not be null.");
        Objects.requireNonNull(scenario, "The level scenario must not be null.");
    }
}