package it.unicam.cs.mpgc.rpg129852.service;

import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.model.LevelData;

public class LevelEngineImpl implements LevelEngine {

    private final LevelData levelData;
    private int phaseIndex = 0;

    public LevelEngineImpl(LevelData levelData) {
        this.levelData = levelData;
    }

    public String getNpcImagePath() {
        return levelData.getScenario().npc().imagePath();
    }

    public boolean hasNextPhase() {
        return phaseIndex < levelData.getScenario().phases().length;
    }

    public LevelPhase getNextPhase(){
        return levelData.getScenario().phases()[phaseIndex++];
    }

    public double getMaxProblemValue(){
        return levelData.getScenario().npc().maxProblemValue();
    }

}