package it.unicam.cs.mpgc.rpg129852.context;

import it.unicam.cs.mpgc.rpg129852.dto.LevelPhase;
import it.unicam.cs.mpgc.rpg129852.model.Level;
import it.unicam.cs.mpgc.rpg129852.model.Virtues;

public class LevelContextImpl implements LevelSessionManager {

    private Level level;

    public void setCurrentLevel(Level level) {
        this.level = level;
    }

    @Override
    public String getCurrentLevelId() {
        return level.getMetadata().id();
    }

    public Virtues getMaxRewards() {
        return level.getMetadata().maxRewards();
    }

    public double getMaxProblemValue() {
        return level.getScenario().npc().maxProblemValue();
    }

    public LevelPhase[] getPhases(){
        return level.getScenario().phases();
    }

    @Override
    public String getNpcImagePath() {
         return level.getScenario().npc().imagePath();
    }

}
