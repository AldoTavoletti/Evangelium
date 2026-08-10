package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

/**
 * Defines the contract for initiating a new level session.
 * It handles the preparation of the required game data before the gameplay loop begins.
 */
public interface LevelStarter {

    /**
     * Starts a level by loading its specific scenario based on the provided metadata
     * and the player's current job, and registers it as the active session.
     *
     * @param metadata    the core information and configuration of the level to start
     * @param discipleJob the player's current job, which may influence the loaded scenario
     * @throws NullPointerException if the metadata or the disciple job is null
     */
    void start(LevelMetadata metadata, Job discipleJob);
}