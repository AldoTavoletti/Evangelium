package it.unicam.cs.mpgc.rpg129852.service.level.gameplay;

import it.unicam.cs.mpgc.rpg129852.dto.level.LevelMetadata;
import it.unicam.cs.mpgc.rpg129852.model.disciple.Job;

public interface LevelStarter {
    void start(LevelMetadata metadata, Job discipleJob);
}
